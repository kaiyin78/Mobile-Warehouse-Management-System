package com.example.externalactivity

import android.R
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.externalactivity.databinding.FragmentRackInBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList
import android.widget.TextView
import androidx.navigation.Navigation


class RackInFragment : Fragment() {

    private lateinit var binding: FragmentRackInBinding
    private val db = Firebase.firestore
    val bundle = Bundle()
    var serialNo = ""
    var prodName = ""
    var prodQuantity = ""
    var rack = ""
    var rackSize = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRackInBinding.inflate(inflater, container, false)

        serialNo = arguments?.getString("SerialNo").toString()

        display()

        getRack()

        rackIn()

        reset()

        return binding.root
    }

    fun display() {
        db.collection("ProductList").whereEqualTo("productSerialNo", serialNo).get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    for (productData in queryDocumentSnapshots) {
                        val serialNo = productData.getString("productSerialNo").toString()
                        prodName = productData.getString("productName").toString()
                        prodQuantity = productData.getString("productQuantity").toString()

                        binding.tvSerialNo.text = serialNo
                        binding.tvProductName.text = prodName
                        binding.tvQuantity.text = prodQuantity
                    }
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun rackIn() {
        binding.btnRackIn.setOnClickListener() {
            val rackInBy = binding.tfStorehand.text.toString().trim()

            if (rack == "") {
                toast("Please Select A Rack!")
            } else if (rackInBy.isEmpty()) {
                binding.tfStorehand.setError("Please Enter Storehand!")
            } else {

                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Rack In Confirmation")
                builder.setMessage(
                    "Are you sure you want to Rack In the following item? \n\n" +
                            "Product Serial No: " + serialNo + "\nProduct Name: " + prodName + "\nQuantity: " + prodQuantity
                )

                builder.setPositiveButton("Rack In") { dialogInterface: DialogInterface, i: Int ->
                    val currentDate = LocalDate.now()
                    val rackInDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(currentDate)

                    if (rackSize < prodQuantity.toInt()) {
                        toast("The Selected Rack Capacity is Not Enough!")
                    } else {
                        db.collection("ProductList").document(serialNo).update(
                            mapOf(
                                "rackInBy" to rackInBy,
                                "rackInDate" to rackInDate,
                                "productStatus" to "Racked In",
                                "rackID" to rack
                            )
                        ).addOnSuccessListener { toast("Rack In Successfully") }
                            .addOnFailureListener { toast("Rack In Failed!") }

                        val rackCapacity = rackSize - prodQuantity.toInt()

                        db.collection("Rack").document(rack).update(
                            mapOf(
                                "rackSize" to rackCapacity.toString()
                            )
                        )

                    Handler().postDelayed({
                        Navigation.findNavController(it).navigate(com.example.externalactivity.R.id.action_rackInFragment_to_menuFragment)
                    }, 3000)

                    }
                }

                builder.setNegativeButton("Cancel", { dialogInterface: DialogInterface, i: Int -> })

                builder.show()
            }
        }

    }

    fun getRack() {
        var rackArray = ArrayList<String>()

        rackArray.add(0, "---Please select a Rack---")

        db.collection("Rack")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    //Log.d(TAG, "${document.id} => ${document.data}")
                    val rackID = document.data.get("rackID").toString()
                    rackArray.add(rackID)
                }
                val arrayAdapter =
                    activity?.let { ArrayAdapter(it, R.layout.simple_spinner_item, rackArray) }
                val spinner: Spinner = binding.spRackID
                spinner.adapter = arrayAdapter

                spinner.onItemSelectedListener = object :

                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (position == 0) {
                            (spinner.selectedView as TextView).error = ""
                            (spinner.selectedView as TextView).setTextColor(Color.GRAY)
                            rack = ""
                        } else {
                            rack = rackArray[position]
                            toast("${rack} selected")

                            db.collection("Rack").whereEqualTo("rackID", rack).get()
                                .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                                    if (!queryDocumentSnapshots.isEmpty) {
                                        for (rackData in queryDocumentSnapshots) {
                                            rackSize =
                                                rackData.getString("rackSize").toString().toInt()
                                        }
                                    }
                                })
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                }
            }
    }

    private fun toast(text: String) {
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
    }

    fun reset() {
        binding.btnReset.setOnClickListener() {
            binding.tfStorehand.setText("")
            binding.spRackID.setSelection(0)
        }
    }

}