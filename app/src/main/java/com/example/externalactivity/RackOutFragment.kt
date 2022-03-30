package com.example.externalactivity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import com.example.externalactivity.databinding.FragmentRackOutBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RackOutFragment : Fragment() {

    private lateinit var binding: FragmentRackOutBinding
    private val db = Firebase.firestore
    var serialNo = ""
    var prodName = ""
    var prodQuantity = ""
    var rackID = ""
    var rackSize = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRackOutBinding.inflate(inflater, container, false)

        val btnImgScan: ImageButton = binding.btnScanSerial

        btnImgScan.setOnClickListener() {
//            autoReset()
            val qrscanner = IntentIntegrator.forSupportFragment(this)
            qrscanner.setBeepEnabled(false)
            qrscanner.initiateScan()
        }

        display()

        rackOut()

        reset()

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if(result.contents==null)
            {
                toast("Failed Scanned!")
            }
            else{
                serialNo = result.contents.toString()
                binding.tvSerialOut.setText(serialNo)
                toast("Scanned Successfully!")
            }

        }
    }

    private fun toast(text: String) {
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
    }

    fun display() {
        val btnSearch: Button = binding.btnSearch

        btnSearch.setOnClickListener() {
            db.collection("ProductList").whereEqualTo("productSerialNo", serialNo).get()
                .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                    if (!queryDocumentSnapshots.isEmpty) {
                        for (productData in queryDocumentSnapshots) {
                            prodName = productData.getString("productName").toString()
                            prodQuantity = productData.getString("productQuantity").toString()
                            rackID = productData.getString("rackID").toString()

                            if (rackID == "null") {
                                binding.tvResult.setText("The product ${serialNo} is not inside the rack.")
                                binding.tvResult.setBackgroundColor(Color.rgb(255, 51, 51))
                                binding.tvResult.visibility = View.VISIBLE
                                rackID = ""
                            } else {
                                binding.titleStorehand.visibility = View.VISIBLE
                                binding.tfRackOutBy.visibility = View.VISIBLE
                                binding.btnRackOut.visibility = View.VISIBLE
                                binding.tvResult.setText(
                                    "Product Name: ${prodName} \n" +
                                            "Quantity: ${prodQuantity} \n" +
                                            "Rack: ${rackID}"
                                )
                                binding.tvResult.setBackgroundColor(Color.rgb(144, 238, 144))

                                binding.tvResult.visibility = View.VISIBLE

                                db.collection("Rack").whereEqualTo("rackID", rackID).get()
                                    .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                                        if (!queryDocumentSnapshots.isEmpty) {
                                            for (rackData in queryDocumentSnapshots) {

                                                rackSize = rackData.getString("rackSize").toString()
                                                    .toInt()
                                            }
                                        }
                                    })

                            }
                        }
                    } else {
                        binding.tvResult.setText("Record not found.")
                        binding.tvResult.setBackgroundColor(Color.rgb(255, 51, 51))
                        binding.tvResult.visibility = View.VISIBLE
                    }
                })
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun rackOut() {
        binding.btnRackOut.setOnClickListener() {

            val rackOutBy = binding.tfRackOutBy.text.toString().trim()

            if (rackOutBy.isEmpty()) {
                binding.tfRackOutBy.setError("Please Enter Storehand!")
            } else if (rackID == "") {
                toast("The Product ${serialNo} Not Found!")
            } else {
                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Rack Out Confirmation")
                builder.setMessage(
                    "Are you sure you want to Rack Out the following item? \n\n" +
                            "Product Serial No: " + serialNo + "\nProduct Name: " + prodName + "\nQuantity: " + prodQuantity
                )

                builder.setPositiveButton("Rack Out") { dialogInterface: DialogInterface, i: Int ->
                    val currentDate = LocalDate.now()
                    val rackOutDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(currentDate)

                    if (rackID == "" || rackID == null) {
                        toast("The Product ${serialNo} not inside the rack!")
                    } else {
                        val rackCapacity = rackSize + prodQuantity.toInt()

                        if (rackID != "") {
                            db.collection("Rack").document(rackID).update(
                                mapOf(
                                    "rackSize" to rackCapacity.toString()
                                )
                            ).addOnSuccessListener {
                                db.collection("ProductList").document(serialNo).update(
                                    mapOf(
                                        "rackOutBy" to rackOutBy,
                                        "rackOutDate" to rackOutDate,
                                        "productStatus" to "Racked Out",
                                        "rackID" to null
                                    )
                                ).addOnSuccessListener { toast("Rack Out Successfully") }
                                    .addOnFailureListener { toast("Rack Out Failed!") }
                            }
                                .addOnFailureListener { toast("Rack Not Found!") }
                        }

                        Handler().postDelayed({
                            Navigation.findNavController(it)
                                .navigate(R.id.action_rackOutFragment_to_menuFragment)
                        }, 3000)

                    }
                }

                builder.setNegativeButton("Cancel", { dialogInterface: DialogInterface, i: Int -> })

                builder.show()
            }
        }
    }

    fun reset() {
        val btnReset: Button = binding.btnResetOut

        btnReset.setOnClickListener() {
            binding.tvSerialOut.setText("")
            binding.tfRackOutBy.setText("")
            binding.tvResult.setText("")
            binding.tvResult.visibility = View.GONE

            binding.titleStorehand.visibility = View.GONE
            binding.tfRackOutBy.visibility = View.GONE
            binding.btnRackOut.visibility = View.GONE

            rackID = ""
            serialNo = ""
        }
    }

    fun autoReset() {
        binding.tvSerialOut.setText("")
        binding.tfRackOutBy.setText("")
        binding.tvResult.setText("")
        binding.tvResult.visibility = View.GONE

        binding.titleStorehand.visibility = View.GONE
        binding.tfRackOutBy.visibility = View.GONE
        binding.btnRackOut.visibility = View.GONE

        rackID = ""
        serialNo = ""
    }

}