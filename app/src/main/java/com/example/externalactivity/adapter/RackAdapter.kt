package com.example.externalactivity.adapter

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.externalactivity.R
import com.example.externalactivity.model.Rack
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RackAdapter(private val rackList: List<Rack>) : RecyclerView.Adapter<RackAdapter.MyViewHolder>(){

    private val db= Firebase.firestore
    private val bundle = Bundle()

    class MyViewHolder(rackView: View): RecyclerView.ViewHolder(rackView){
        val pRackID : TextView = rackView.findViewById((R.id.tvRackID))
        val pRackName : TextView = rackView.findViewById((R.id.tvRackName))
        val pRackSize : TextView = rackView.findViewById(R.id.tvRackSize)
        val btnRvDelete: Button = rackView.findViewById((R.id.btnRvDelete))
        val btnRVEdit: Button = rackView.findViewById((R.id.btnRVEdit))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.rackdetail_view, parent, false)

        return  MyViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val  currentRec: Rack = rackList[position]
        holder.pRackID.text = currentRec.rackID
        holder.pRackName.text = currentRec.rackName
        holder.pRackSize.text = currentRec.rackSize.toString()

        holder.btnRvDelete.setOnClickListener{
            Log.e(ContentValues.TAG,holder.pRackID.toString())
            db.collection("Rack").document(holder.pRackID.text.toString()).delete()
            Navigation.findNavController(it).navigate(R.id.action_rackManagement_to_deleteRack)
        }

        holder.btnRVEdit.setOnClickListener{
            bundle.putString("rackID", currentRec.rackID.toString())
            bundle.putString("rackName", currentRec.rackName.toString())
            bundle.putString("rackSize", currentRec.rackSize.toString())

            Navigation.findNavController(it).navigate(R.id.action_rackManagement_to_editRack, bundle)

        }
    }

    override fun getItemCount(): Int {
        return rackList.size
    }
}