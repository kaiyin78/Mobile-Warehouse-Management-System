package com.example.externalactivity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.externalactivity.databinding.FragmentReceiveProductDetailsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import java.text.SimpleDateFormat
import java.util.*


class ReceiveProductDetails : Fragment() {
    val bundle = Bundle()

    private lateinit var binding: FragmentReceiveProductDetailsBinding

    private val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentReceiveProductDetailsBinding.inflate(inflater, container, false)

        binding.btnUpdate.setOnClickListener { update() }
        binding.imgbtnScanSerialNo.setOnClickListener {
            val qrscanner = IntentIntegrator.forSupportFragment(this)
            qrscanner.setBeepEnabled(false)
            qrscanner.initiateScan()
        }
        val partNo = arguments?.getString("partNo")
//        val CateName=arguments?.getString("categoryName")

        binding.lblPartNo.setText(partNo)
//        binding.lblCateName.setText(CateName)

        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var qrResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (qrResult != null) {
            if (qrResult.contents == null) {
                Toast.makeText(activity, "Cancelled", Toast.LENGTH_LONG).show()
            } else {

                var qrSerialNo = qrResult.contents.toString()
                binding.lblSerialNo.setText(qrSerialNo)
                Toast.makeText(activity, "Scanned: " + qrResult.contents, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            Toast.makeText(requireActivity(), "Failed to scan", Toast.LENGTH_LONG).show()
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun update() {
        val productPartNo = binding.lblPartNo.text.toString().trim()
        val productSerialNo = binding.lblSerialNo.text.toString().trim()
        val productName = binding.lblProductName.text.toString().trim()
        val productQuantity = binding.lblQuantity.text.toString().trim()
        val receiveDate= SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()).toString()
        val productData = hashMapOf(
            "partNo" to productPartNo,
            "productSerialNo" to productSerialNo,
            "productName" to productName,
            "productQuantity" to productQuantity,
            "productStatus" to "Unassigned",
            "receiveDate" to receiveDate
        )

        if(productSerialNo.isEmpty()){
            binding.lblSerialNo.error="Product Serial No Required!"
            binding.lblSerialNo.requestFocus()
        } else if(productName.isEmpty())
        {
            binding.lblProductName.error="Product Name Required!"
            binding.lblProductName.requestFocus()
        }
        else if(productQuantity.isEmpty() || productQuantity == "0")
        {
            binding.lblQuantity.error="Product Quantity cannot be null or 0!"
            binding.lblQuantity.requestFocus()
        }else{
            db.collection("ProductList").document(productSerialNo).get().addOnSuccessListener {
                if(it.exists()){
                    binding.lblSerialNo.error="Duplicate Serial No. Please Try Again"
                    binding.lblSerialNo.requestFocus()
//            Log.e(TAG,"Test1");
                }
                else{
//            Log.e(TAG,"Test2");
                    db.collection("ProductList").document(productSerialNo).set(productData)
                        .addOnSuccessListener { toast("Add Product Successfully") }
                        .addOnFailureListener { toast("Add Product Failed!") }
                }
            }

        }





    }

    private fun toast(text: String) {
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
    }
}