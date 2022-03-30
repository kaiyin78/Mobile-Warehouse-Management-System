package com.example.externalactivity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.externalactivity.adapter.ProductAdapter
import com.example.externalactivity.adapter.ProductListDisplayAdapter
import com.example.externalactivity.databinding.FragmentProductListManagementBinding
import com.example.externalactivity.databinding.FragmentReceiveProductDetailsBinding
import com.example.externalactivity.model.ProductListClass
import com.example.externalactivity.model.productclass
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class ProductListManagement : Fragment() {
    val bundle = Bundle()
    private lateinit var binding: FragmentProductListManagementBinding
    private val db= Firebase.firestore
    private  lateinit var  productListManagementRV: RecyclerView
    private lateinit var productListManagementAdapter: ProductListDisplayAdapter
    private lateinit var productListManagementArray: ArrayList<ProductListClass>
    private lateinit var tempstoreproductListManagementArray: ArrayList<ProductListClass>
    private lateinit var storeProductListManagementArray: ArrayList<ProductListClass>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProductListManagementBinding.inflate(inflater, container, false)
        productListManagementArray= arrayListOf<ProductListClass>()
        storeProductListManagementArray= arrayListOf<ProductListClass>()
        tempstoreproductListManagementArray= arrayListOf<ProductListClass>()
        productListManagementRV = binding.recyclerviewProductShow
        productListManagementRV.layoutManager= LinearLayoutManager(context)
        productListManagementRV.setHasFixedSize(true);
        productListManagementAdapter= ProductListDisplayAdapter(productListManagementArray)
        productListManagementRV.adapter=productListManagementAdapter


        val partNo= arguments?.getString("partNo")
//        val CateName=arguments?.getString("categoryName")



        display()
        return binding.root
    }
    private fun toast(text: String) {
        Toast.makeText(requireActivity(),text, Toast.LENGTH_SHORT).show()
    }
    private fun display() {
        val  productQuantity1:Int=0
        val bundle = Bundle()
        val partNo= arguments?.getString("partNo")
//        Log.e(TAG,partNo.toString());
        db.collection("ProductList").whereEqualTo("partNo", partNo).get()
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

                        productListManagementArray.add(storeData)
//                        Log.e(TAG,productData.getString("productQuantity").toString());
                    }
                    productListManagementRV.adapter=ProductListDisplayAdapter(productListManagementArray)
                } else {
                    println("Failed!")
                    toast("There Are No Product In This Product Category!!")
                }
            })




    }


    override fun onStart() {
        super.onStart()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
//                storeProductListManagementArray.clear()
                storeProductListManagementArray.addAll(productListManagementArray)
                if(tempstoreproductListManagementArray.isEmpty())
                {
                    tempstoreproductListManagementArray.addAll(storeProductListManagementArray)
                }

          val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {

                    search(searchText)
                    productListManagementArray.clear()
                } else {
//                    productListManagementArray!!.clear()
//                    productListManagementArray?.addAll(storeProductListManagementArray)

                    productListManagementRV.adapter=ProductListDisplayAdapter(tempstoreproductListManagementArray)
                    productListManagementRV.adapter!!.notifyDataSetChanged()
                }
                return true
            }

        })
    }
    fun search(searchText: String) {
        val partNo= arguments?.getString("partNo")

        db.collection("ProductList").whereEqualTo("partNo", partNo).get()
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

                            productListManagementArray.add(storeData)
                        }
//                        Log.e(TAG,productData.getString("productQuantity").toString());
                    }

                    productListManagementRV.adapter=ProductListDisplayAdapter(productListManagementArray)

                    productListManagementRV.adapter!!.notifyDataSetChanged()

                } else {
                    println("Failed!")
                }
            })



    }




}