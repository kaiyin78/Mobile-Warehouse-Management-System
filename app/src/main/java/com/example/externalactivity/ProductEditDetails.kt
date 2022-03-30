package com.example.externalactivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.externalactivity.databinding.FragmentProductEditDetailsBinding
import com.example.externalactivity.databinding.FragmentReceiveProductDetailsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProductEditDetails : Fragment() {
    val bundle = Bundle()
    private lateinit var binding: FragmentProductEditDetailsBinding
    private val db= Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding= FragmentProductEditDetailsBinding.inflate(inflater,container, false)
        binding.btnEditDetails.setOnClickListener {

            update()

        }
        val partNo= arguments?.getString("partNo")
        val productName= arguments?.getString("productName")
        val productQuantity= arguments?.getString("productQuantity").toString()
        val productSerialNo= arguments?.getString("productSerialNo")


        binding.tvEditDetailsPartNo.setText(partNo)
        binding.tvEditDetailsProductName.setText(productName)
        binding.tvEditDetailsQuantity.setText(productQuantity)
        binding.tvEditDetailsSerialNo.setText(productSerialNo)
        return binding.root
    }

    private fun update() {
        val productPartNo= binding.tvEditDetailsPartNo.text.toString().trim()
        val productSerialNo= binding.tvEditDetailsSerialNo.text.toString().trim()
        val productName= binding.tvEditDetailsProductName.text.toString().trim()
        val productQuantity= binding.tvEditDetailsQuantity.text.toString().trim()

        val productData = hashMapOf(
            "partNo" to productPartNo,
            "productSerialNo" to  productSerialNo,
            "productName" to  productName,
            "productQuantity" to  productQuantity
        )
        if(productQuantity.isEmpty() || productQuantity == "0"){
            binding.tvEditDetailsQuantity.error="Product Quantity Cannot Be Empty or Zero(0)!!"
            binding.tvEditDetailsQuantity.requestFocus()
        } else{
            db.collection("ProductList").document(productSerialNo).update(
                mapOf(  "partNo" to productPartNo,
                    "productSerialNo" to  productSerialNo,
                    "productName" to  productName,
                    "productQuantity" to  productQuantity)
            )
                .addOnSuccessListener { toast("Edit Product Successfully") }
                .addOnFailureListener{toast("Edit Product Failed!")}
        }

    }
    private fun toast(text: String) {
        Toast.makeText(requireActivity(),text, Toast.LENGTH_SHORT).show()
    }

}