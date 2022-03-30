package com.example.externalactivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.get
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.externalactivity.adapter.ProductListAdapter
import com.example.externalactivity.adapter.ProductListDisplayAdapter
import com.example.externalactivity.adapter.ProductStatusAdapter
import com.example.externalactivity.databinding.FragmentProductListBinding
import com.example.externalactivity.databinding.FragmentProductStatusBinding
import com.example.externalactivity.model.ProductListClass
import com.example.externalactivity.model.productclass
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class ProductStatus : Fragment() {

    private lateinit var binding: FragmentProductStatusBinding
    private lateinit var productStatuslistArray: ArrayList<ProductListClass>
    private val db= Firebase.firestore
    private val nav by lazy { findNavController() }
    private  lateinit var  productStatusRV: RecyclerView
    private lateinit var productStsAdapter: ProductStatusAdapter
    private lateinit var storeproductStatusArray: ArrayList<ProductListClass>
    private lateinit var  tempstoreproductListManagementArray: ArrayList<ProductListClass>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProductStatusBinding.inflate(inflater, container, false)
        productStatuslistArray= arrayListOf<ProductListClass>()
        storeproductStatusArray= arrayListOf<ProductListClass>()
        tempstoreproductListManagementArray= arrayListOf<ProductListClass>()
        productStatusRV = binding.recyclerViewProductStatus
        productStatusRV.layoutManager= LinearLayoutManager(context)
        productStatusRV.setHasFixedSize(true);
        productStsAdapter= ProductStatusAdapter(productStatuslistArray)
        productStatusRV.adapter=productStsAdapter

        display()
        return binding.root
    }

    private fun display() {
        db.collection("ProductList").whereEqualTo("productStatus", "Unassigned").get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    for (productData in queryDocumentSnapshots) {
                        val storeData = (ProductListClass
                            (productData.getString("partNo").toString(),
                            productData.getString("productName").toString(),
                            productData.getString("productQuantity").toString().toInt(),
                            productData.getString("productSerialNo").toString(),
                            productData.getString("productStatus").toString(),
                            productData.getString("receiveDate").toString()))

                        productStatuslistArray.add(storeData)
                    }
                    productStatusRV.adapter=ProductStatusAdapter(productStatuslistArray)
                } else {
                    println("Failed!")
                    toast("There Are No Product In This Product Status!!")
                }
            })
    }


    private fun toast(text: String) {
        Toast.makeText(requireActivity(),text, Toast.LENGTH_SHORT).show()
    }


    override fun onStart() {
        super.onStart()
        binding.searchView2.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {

                storeproductStatusArray.addAll(productStatuslistArray)
                if(tempstoreproductListManagementArray.isEmpty())
                {
                    tempstoreproductListManagementArray.addAll(storeproductStatusArray)
                }
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    search(searchText)
                    productStatuslistArray.clear()
                } else {
//                    productStatuslistArray!!.clear()
//                    productStatuslistArray?.addAll(storeproductStatusArray)
                    productStatusRV.adapter=ProductListDisplayAdapter(tempstoreproductListManagementArray)
                    productStatusRV.adapter!!.notifyDataSetChanged()
                }

                return true
            }

        })
    }
    fun search(searchText: String) {
        val partNo= arguments?.getString("partNo")


        db.collection("ProductList").whereEqualTo("productStatus", "Unassigned").get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {

                    for (productData in queryDocumentSnapshots) {
                        if(productData.getString("partNo")!!.lowercase(Locale.getDefault()).contains(searchText)||
                            productData.getString("productName")!!.lowercase(Locale.getDefault()).contains(searchText)||
                            productData.getString("productSerialNo")!!.lowercase(Locale.getDefault()).contains(searchText)||
                            productData.getString("productStatus")!!.lowercase(Locale.getDefault()).contains(searchText)
//                            ||
//                            productData.getString("receiveDate")!!.lowercase(Locale.getDefault()).contains(searchText)
                        ){
                            val storeData = (ProductListClass
                                (productData.getString("partNo").toString(),
                                productData.getString("productName").toString(),
                                productData.getString("productQuantity").toString().toInt(),
                                productData.getString("productSerialNo").toString(),
                                productData.getString("productStatus").toString(),
                                productData.getString("receiveDate").toString()))

                            productStatuslistArray.add(storeData)
                        }
//                        Log.e(TAG,productData.getString("productQuantity").toString());
                    }
                    productStatusRV.adapter= ProductListDisplayAdapter(productStatuslistArray)
                    productStatusRV.adapter!!.notifyDataSetChanged()
                } else {
                    println("Failed!")
                }
            })



    }
}