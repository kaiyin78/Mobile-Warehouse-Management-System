package com.example.externalactivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.externalactivity.adapter.RackAdapter
import com.example.externalactivity.databinding.FragmentRackManagementBinding
import com.example.externalactivity.model.Rack
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RackManagement : Fragment() {
    private lateinit var  rackRV: RecyclerView
    private lateinit var binding: FragmentRackManagementBinding
    private val db= Firebase.firestore

    private val nav by lazy { findNavController() }
    private lateinit var RackList: ArrayList<Rack>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRackManagementBinding.inflate(inflater, container, false)
        binding.btnAddRack.setOnClickListener { nav.navigate(R.id.action_rackManagement_to_addRack) }
        binding.btnCheckRack.setOnClickListener { nav.navigate(R.id.action_rackManagement_to_scanRack) }

        RackList= arrayListOf<Rack>()
        rackRV=binding.rackRecyclerView
        rackRV.adapter = RackAdapter(RackList)
        rackRV.layoutManager = LinearLayoutManager(context)
        rackRV.setHasFixedSize(true)

        display()

        return binding.root
    }

    private fun display() {
        db.collection("Rack").orderBy("rackID", Query.Direction.ASCENDING).get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    for (pRack in queryDocumentSnapshots) {
                        val storeData = (Rack
                            (pRack.getString("rackID").toString(),
                            pRack.getString("rackName").toString(),
                            pRack.getString("rackSize").toString(),
                        ))

                        RackList.add(storeData)
                    }
                    rackRV.adapter= RackAdapter(RackList)
                } else {
                    println("Failed!")
                }
            })
    }



}