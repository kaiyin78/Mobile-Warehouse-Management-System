package com.example.externalactivity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.externalactivity.R
import com.example.externalactivity.model.Report


class ReportAdapter(private var userList: ArrayList<Report>) : RecyclerView.Adapter<ReportAdapter.MyViewHolder>() {

//        var userListFilter = ArrayList<Report>()

//fun setData(userList: ArrayList<Report>){
//    this.userListFilter = userListFilter
//notifyDataSetChanged()
//
//}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)

        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val product: Report = userList[position]
        holder.partNo.text = product.partNo
        holder.productQuantity.text = product.productQuantity
        holder.productSerialNo.text = product.productSerialNo
        holder.productStatus.text = product.productStatus
        holder.rackID.text = product.rackID
        holder.rackInBy.text = product.rackInBy
        holder.rackInDate.text = product.rackInDate
        holder.receiveBy.text = product.receiveBy
        holder.receiveDate.text = product.receiveDate




    }

    override fun getItemCount(): Int {
        return  userList.size


    }


    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val partNo : TextView = itemView.findViewById(R.id.partNo)
        val productQuantity : TextView = itemView.findViewById(R.id.productQuantity)
        val productSerialNo : TextView = itemView.findViewById(R.id.productSerialNo)
        val productStatus : TextView = itemView.findViewById(R.id.productStatus)
        val rackID : TextView = itemView.findViewById(R.id.rackID)
        val rackInBy : TextView = itemView.findViewById(R.id.rackInBy)
        val rackInDate : TextView = itemView.findViewById(R.id.rackInDate)
        val receiveBy : TextView = itemView.findViewById(R.id.receiveBy)
        val receiveDate : TextView = itemView.findViewById(R.id.receiveDate)





    }

//    override fun getFilter(): Filter {
//
//        return object: Filter(){
//            override fun performFiltering(charSequence: CharSequence?): FilterResults {
//                val filterResults = FilterResults()
//                if(charSequence == null || charSequence.length < 0) {
//                    filterResults.count = userListFilter.size
//                    filterResults.values = userListFilter
//                }else {
//
//                    var searchChr = charSequence.toString().toLowerCase()
//
//                    val itemModal = ArrayList<Report>()
//
//                    for (item in userListFilter) {
//
//                        if (item.equals(searchChr)) {
//                            itemModal.add(item)
//                        }
//
//
//                    }
//
//                    filterResults.count = itemModal.size
//                    filterResults.values = itemModal
//                }
//
//            return filterResults
//            }
//
//            override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
//                userList = filterResults!!.values as ArrayList<Report>
//                notifyDataSetChanged()
//            }
//
//
//        }
//    }

}