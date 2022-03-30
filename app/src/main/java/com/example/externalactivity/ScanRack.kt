package com.example.externalactivity
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.externalactivity.databinding.FragmentScanRackBinding
import com.example.externalactivity.model.Rack
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator

class ScanRack : Fragment() {

    private lateinit var binding: FragmentScanRackBinding
    private lateinit var RackList: ArrayList<Rack>
    private val db= Firebase.firestore


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= FragmentScanRackBinding.inflate(inflater,container, false)
        binding.btnRackContact.setOnClickListener{ call("tel:0178488330") }
        binding.btnLocation.setOnClickListener{ map() }
        binding.btnEmail.setOnClickListener{ Email() }
        binding.btnScan.setOnClickListener{ scan() }

        return binding.root
    }

    private fun map() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("geo: 3.1390, 101.6869")

        startActivity(intent)
    }

    private fun scan() {
        val qrscanner = IntentIntegrator.forSupportFragment(this)
        qrscanner.setBeepEnabled(false)
        qrscanner.initiateScan()
    }

    private fun call(uri: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse(uri))
        startActivity(intent)

    }

    private fun Email(){
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:tanking226:gmail.com")

        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null){
            val code = result.contents
            val search = null

            db.collection("Rack").whereEqualTo("rackID", code).get()
                .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                    if (!queryDocumentSnapshots.isEmpty) {
                        for (pRack in queryDocumentSnapshots) {
                            binding.tvResultScan.text = pRack.getString("rackName").toString()
                            binding.tvResultScan2.text = pRack.getString("rackID").toString()
                            binding.tvResultScan3.text = pRack.getString("rackSize")
                            db.collection("ProductList").whereEqualTo( "rackID", code).get()
                                .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots2 ->
                                    if (!queryDocumentSnapshots2.isEmpty) {
                                        for (pRack2 in queryDocumentSnapshots2) {
                                            binding.tvResultScan4.text = pRack2.getString("productQuantity").toString()
                                            binding.tvResultScan5.text = pRack2.getString("productName").toString()
                                            binding.tvResultScan4.text.toString().toInt()
                                            val numSize = binding.tvResultScan3.text.toString().toInt()
                                            val numSize2 = numSize*0.50
                                            val productQuan = binding.tvResultScan4.text.toString().toInt()
                                            if ((numSize2) >= productQuan){
                                                binding.textView45.text = "The number of products on the rack is insufficient, please contact the supervisor."
                                            }
                                        }
                                    }
                                })
                        }
                    } else {
                        println("Failed!")
                    }

                })
        }
    }

}