package com.example.externalactivity.adapter

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.externalactivity.R
import com.example.externalactivity.model.ProductListClass
import com.example.externalactivity.model.productclass
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.StringBuilder

class ProductListDisplayAdapter(private val productListDisplayArray: List<ProductListClass>) : RecyclerView.Adapter<ProductListDisplayAdapter.productListDisplayViewHolder>() {
    private lateinit var builder: AlertDialog.Builder
    val db= Firebase.firestore
    val bundle = Bundle()
    var GPart="";

    class productListDisplayViewHolder (productListDisplayView: View): RecyclerView.ViewHolder(productListDisplayView){

        val productDisplaylistPartNo : TextView = productListDisplayView.findViewById(R.id.productDisplaylistPartNo)
        val productDisplaylistSerialNo: TextView = productListDisplayView.findViewById(R.id.productDisplaylistSerialNo)
        val productDisplaylistProductName: TextView = productListDisplayView.findViewById(R.id.productDisplaylistProductName)
        val productDisplaylistQuantity: TextView = productListDisplayView.findViewById(R.id.productDisplaylistQuantity)
        val productDisplaylistStatus: TextView = productListDisplayView.findViewById(R.id.productDisplaylistStatus)
        val productDisplaylistReceiveDate: TextView= productListDisplayView.findViewById(R.id.productDisplaylistreceiveDate)
        val btnEdit:ImageButton= productListDisplayView.findViewById(R.id.imgbtnEdit)
        val btnDelete:ImageButton=productListDisplayView.findViewById(R.id.imgbtnDelete)
//        val btnNextProductManagement: ImageButton =productListDisplayView.findViewById(R.id.btnNextProductManagement)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): ProductListDisplayAdapter.productListDisplayViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_productdisplaylist, parent, false)

        return ProductListDisplayAdapter.productListDisplayViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ProductListDisplayAdapter.productListDisplayViewHolder, position: Int) {

        val currentProduct = productListDisplayArray[position]
        holder.productDisplaylistPartNo.text = currentProduct.partNo
        holder.productDisplaylistSerialNo.text = currentProduct.productSerialNo
        holder.productDisplaylistProductName.text = currentProduct.productName
        holder.productDisplaylistQuantity.text = currentProduct.productQuantity.toString()
        holder.productDisplaylistStatus.text = currentProduct.productStatus
        holder.productDisplaylistReceiveDate.text=currentProduct.receiveDate.toString()
        GPart=currentProduct.partNo.toString()


        holder.btnEdit.setOnClickListener{
            bundle.putString("partNo", currentProduct.partNo.toString())
            bundle.putString("productName", currentProduct.productName.toString())
            bundle.putString("productQuantity", currentProduct.productQuantity.toString())
            bundle.putString("productSerialNo", currentProduct.productSerialNo.toString())

            Navigation.findNavController(it).navigate(R.id.action_productListManagement_to_productEditDetails, bundle)
        }
        holder.btnDelete.setOnClickListener{

            builder=AlertDialog.Builder(it.context)
            builder.setTitle("Alert").setMessage("Do You Want delete?").setCancelable(true)
                .setPositiveButton("Yes"){dialogInterface,it1->
                    db.collection("ProductList").document(holder.productDisplaylistSerialNo.text.toString()).delete()
                    Navigation.findNavController(it).navigate(R.id.action_productListManagement_to_productList)
                    Toast.makeText(it.context,"Delete Successfully!!",Toast.LENGTH_SHORT).show()

                }
                .setNegativeButton("No"){dialogInterface,it->
                    dialogInterface.cancel()
                }.show()


        }
//        holder.btnNextProductManagement.setOnClickListener{
//          Log.e(TAG,currentProduct.categoryName.toString());
//            bundle.putString("partNo", currentProduct.partNo.toString())
//            Navigation.findNavController(it).navigate(R.id.action_productList_to_productListManagement,bundle)
//        }
    }

    override fun getItemCount(): Int {
        return productListDisplayArray.size
    }
}