package com.example.externalactivity.adapter

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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.externalactivity.R
import com.example.externalactivity.model.productclass

class ProductAdapter(private val productList: List<productclass>) : RecyclerView.Adapter<ProductAdapter.productViewHolder>() {
    val bundle = Bundle()

//    private lateinit var mListener: ProductAdapter.onProductClickListener
//
//    interface onProductClickListener{
//
//        fun onProductClick(
//            tvPartNo:String,
//            tvCateName:String
//        )
//    }
//
//    fun setOnProductClickListener(listener: ProductAdapter.onProductClickListener)
//    {
//        mListener= listener
//    }

    class productViewHolder (productView: View): RecyclerView.ViewHolder(productView){

        val tvPartNo : TextView = productView.findViewById(R.id.displayPartNo)
        val tvCateName: TextView = productView.findViewById(R.id.displayCateName)
        val buttonNext:ImageButton=productView.findViewById(R.id.buttonNext)


//        fun bind(prod: productclass, context: Context)
//        {
//            tvPartNo.text= prod.partNo
//            tvCateName.text=prod.categoryName
//        }
//
//        init {
//            productView.setOnClickListener {
//                listener.onProductClick(
//                    tvPartNo.text.toString(),
//                    tvCateName.text.toString()
//                )
//            }
//        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): ProductAdapter.productViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_receiveproduct, parent, false)

        return productViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: productViewHolder, position: Int) {

        val currentProduct = productList[position]
        holder.tvPartNo.text = currentProduct.partNo
        holder.tvCateName.text = currentProduct.categoryName
        holder.buttonNext.setOnClickListener{
//            Log.e(TAG,currentProduct.categoryName.toString());
            bundle.putString("partNo", currentProduct.partNo.toString())
            Navigation.findNavController(it).navigate(R.id.action_receiveProduct3_to_receiveProductDetails3,bundle)
        }
    }

    override fun getItemCount(): Int {
    return productList.size
    }
}