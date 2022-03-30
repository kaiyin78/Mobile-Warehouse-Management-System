package com.example.externalactivity

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.externalactivity.databinding.FragmentRegisterNewCategoryBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterNewCategory : Fragment() {

    private  lateinit var  binding: FragmentRegisterNewCategoryBinding
    private val nav by lazy { findNavController() }
    private val db= Firebase.firestore
    private var validate: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentRegisterNewCategoryBinding.inflate(inflater, container, false)

        binding.btnAddNewCate.setOnClickListener { insert() }

        return binding.root
    }

    private fun insert() {
        val productPartNo= binding.InputPartNo.text.toString().trim()
        val productCategoryName= binding.InputCateName.text.toString()

        val productData = hashMapOf(
            "partNo" to productPartNo,
            "categoryName" to productCategoryName
        )
        if(productPartNo.isEmpty()){
            binding.InputPartNo.error="Part No Required!"
            binding.InputPartNo.requestFocus()
        } else if(productCategoryName.isEmpty())
        {
            binding.InputCateName.error="Product Category Name Required!"
            binding.InputCateName.requestFocus()
        }
        else{
            db.collection("Product").get()
                .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                    if (!queryDocumentSnapshots.isEmpty) {
                        for (productData in queryDocumentSnapshots) {


                            val validateproductPartNo = productData.getString("partNo").toString()

                            Log.e(ContentValues.TAG, validateproductPartNo.toString());
                            if (validateproductPartNo == productPartNo) {
                                binding.InputPartNo.error = "Duplicate Part No. Please Try Again"
                                binding.InputPartNo.requestFocus()
                                Log.e(ContentValues.TAG, "WTF SUCCESS")
                                validate = true
                                break
                            }else
                            {
                                validate=false
                            }

                        }
                        if(!validate){
                            Log.e(ContentValues.TAG, "WTF Failed")
                            db.collection("Product").document().set(productData)
                                .addOnSuccessListener { toast("Add Product Category Successfully") }
                                .addOnFailureListener{toast("Add Product Category Failed!")}
                        }
                    }
                })



        }




    }
    private fun toast(text: String) {
        Toast.makeText(requireActivity(),text,Toast.LENGTH_SHORT).show()
    }




}
