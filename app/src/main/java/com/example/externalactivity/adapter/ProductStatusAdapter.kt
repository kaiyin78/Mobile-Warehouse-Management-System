package com.example.externalactivity.adapter

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.externalactivity.R
import com.example.externalactivity.RackInFragment
import com.example.externalactivity.model.ProductListClass
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ProductStatusAdapter (private val productStatusArray: List<ProductListClass>) : RecyclerView.Adapter<ProductStatusAdapter.productStatusViewHolder>() {
    private lateinit var builder: AlertDialog.Builder
    val bundle = Bundle()
    var GPart="";

    class productStatusViewHolder (productStatusView: View): RecyclerView.ViewHolder(productStatusView){

        val productStatusPartNo : TextView = productStatusView.findViewById(R.id.tvProductStatusPartNo)
        val productStatusSerialNo: TextView = productStatusView.findViewById(R.id.tvProductStatusSerialNo)
        val productStatusProductName: TextView = productStatusView.findViewById(R.id.tvProductStatusName)
        val productStatusQuantity: TextView = productStatusView.findViewById(R.id.tvProductStatusQuantity)
        val productStatusAssign: TextView = productStatusView.findViewById(R.id.tvProductStatusAssign)
        val productStatusReceiveDate: TextView = productStatusView.findViewById(R.id.tvProductStatusReceiveDate)
        val btnAssign: Button = productStatusView.findViewById(R.id.btnAssign)


    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): ProductStatusAdapter.productStatusViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclervew_productstatus, parent, false)

        return ProductStatusAdapter.productStatusViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ProductStatusAdapter.productStatusViewHolder, position: Int) {

        val currentProduct = productStatusArray[position]
        holder.productStatusPartNo.text = currentProduct.partNo
        holder.productStatusSerialNo.text = currentProduct.productSerialNo
        holder.productStatusProductName.text = currentProduct.productName
        holder.productStatusQuantity.text = currentProduct.productQuantity.toString()
        holder.productStatusAssign.text = currentProduct.productStatus
        holder.productStatusReceiveDate.text=currentProduct.receiveDate
        holder.btnAssign.setOnClickListener() {
            //Log.d("Product", "The product" + currentProduct.productSerialNo)
            bundle.putString("SerialNo", currentProduct.productSerialNo)
            Navigation.findNavController(it).navigate(R.id.action_productStatus_to_rackInFragment, bundle)

        }
    }

    override fun getItemCount(): Int {
        return productStatusArray.size
    }

    interface CommunicationFragment {
        fun passData(position: Int, serialNo: String)
    }



}