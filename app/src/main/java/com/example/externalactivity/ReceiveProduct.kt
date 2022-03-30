package com.example.externalactivity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.externalactivity.adapter.ProductAdapter
import com.example.externalactivity.databinding.FragmentReceiveProductBinding
import com.example.externalactivity.model.productclass
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import androidx.annotation.NonNull
import androidx.core.os.bundleOf
import androidx.navigation.Navigation

import com.google.android.gms.tasks.OnFailureListener

import com.google.firebase.firestore.QueryDocumentSnapshot

import com.google.firebase.firestore.QuerySnapshot

import com.google.android.gms.tasks.OnSuccessListener



class ReceiveProduct: Fragment(){

    private lateinit var receiveProductarray: ArrayList<productclass>
    private val db= Firebase.firestore
    private lateinit var binding: FragmentReceiveProductBinding
    private val nav by lazy { findNavController() }
    private lateinit var  receiveProductRV: RecyclerView
    private lateinit var receiveProductAdapter: ProductAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val view = inflater.inflate(R.layout.fragment_receive_product, container, false)

        binding= FragmentReceiveProductBinding.inflate(inflater, container, false)

        binding.btnAddNewCategory.setOnClickListener{nav.navigate(R.id.action_receiveProduct3_to_registerNewCategory)}



        receiveProductarray= arrayListOf<productclass>()
        receiveProductRV = binding.recyclerViewReceiveProduct

        receiveProductRV.layoutManager= LinearLayoutManager(context)
        receiveProductRV.setHasFixedSize(true);

        receiveProductAdapter= ProductAdapter(receiveProductarray)
        receiveProductRV.adapter=receiveProductAdapter
        display()




        return binding.root
    }

    private fun display(){

        db.collection("Product").orderBy("partNo",Query.Direction.ASCENDING).get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    for (productData in queryDocumentSnapshots) {
                        val storeproductData: productclass = productData.toObject(productclass::class.java)
                        receiveProductarray.add(storeproductData)
                        Log.e(TAG, receiveProductarray.toString())
                    }
                    receiveProductRV.adapter=ProductAdapter(receiveProductarray)
                } else {
                    println("Failed!")
                }
            })



    }

}