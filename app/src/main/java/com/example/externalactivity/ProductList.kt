package com.example.externalactivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.externalactivity.adapter.ProductAdapter
import com.example.externalactivity.adapter.ProductListAdapter
import com.example.externalactivity.databinding.FragmentProductListBinding
import com.example.externalactivity.model.productclass
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProductList : Fragment() {

private lateinit var binding:FragmentProductListBinding

    private lateinit var productList: ArrayList<productclass>
    private val db= Firebase.firestore
    private val nav by lazy { findNavController() }
    private  lateinit var  productRV: RecyclerView
    private lateinit var productAdapter: ProductListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
    binding= FragmentProductListBinding.inflate(inflater, container, false)
        binding.imBtnNewCategory.setOnClickListener { nav.navigate(R.id.action_productList_to_registerNewCategory) }
        productList= arrayListOf<productclass>()
        productRV = binding.productListRecycleView
        productRV.layoutManager= LinearLayoutManager(context)
        productRV.setHasFixedSize(true);
        productAdapter= ProductListAdapter(productList)
        productRV.adapter=productAdapter

        display()
        return binding.root
    }
    private fun display()
    {
        db.collection("Product").orderBy("partNo", Query.Direction.ASCENDING).get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    for (productData in queryDocumentSnapshots) {
                        val storeproductData: productclass = productData.toObject(productclass::class.java)
                        productList.add(storeproductData)
                    }
                    productRV.adapter=ProductListAdapter(productList)
                } else {
                    println("Failed!")

                }
            })
    }

}