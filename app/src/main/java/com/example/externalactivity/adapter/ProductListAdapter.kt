package com.example.externalactivity.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.externalactivity.R
import com.example.externalactivity.model.ProductListClass
import com.example.externalactivity.model.productclass

class ProductListAdapter(private val productListArray: List<productclass>) : RecyclerView.Adapter<ProductListAdapter.productListViewHolder>(){
    val bundle = Bundle()

    class productListViewHolder (productListView: View): RecyclerView.ViewHolder(productListView){

        val displayProductPartNo : TextView = productListView.findViewById(R.id.displayProductPartNo)
        val displayProductCateName: TextView = productListView.findViewById(R.id.displayProductCateName)
        val btnNextProductManagement: ImageButton =productListView.findViewById(R.id.btnNextProductManagement)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): ProductListAdapter.productListViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_productlist, parent, false)

        return productListViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: productListViewHolder, position: Int) {

        val currentProduct = productListArray[position]
        holder.displayProductPartNo.text = currentProduct.partNo
        holder.displayProductCateName.text = currentProduct.categoryName
        holder.btnNextProductManagement.setOnClickListener{
//            Log.e(TAG,currentProduct.categoryName.toString());
            bundle.putString("partNo", currentProduct.partNo.toString())
            Navigation.findNavController(it).navigate(R.id.action_productList_to_productListManagement,bundle)
        }
    }

    override fun getItemCount(): Int {
        return productListArray.size
    }

}