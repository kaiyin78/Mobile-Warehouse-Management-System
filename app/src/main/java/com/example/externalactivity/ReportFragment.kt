package com.example.externalactivity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filterable
import android.widget.SearchView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.externalactivity.adapter.ReportAdapter
import com.example.externalactivity.model.Report
import com.example.externalactivity.model.productclass
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class ReportFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<Report>
    private lateinit var myAdapter: ReportAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_report, container, false)

        recyclerView = v.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf()


        myAdapter = ReportAdapter(userArrayList)

        recyclerView.adapter = myAdapter

        EventChangeListener()






        return v
    }


    private fun EventChangeListener() {

        db = FirebaseFirestore.getInstance()
        db.collection("ProductList").orderBy("partNo", Query.Direction.ASCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {

                        Log.e("Firestore Error", error.message.toString())
                        return

                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {


                        if (dc.type == DocumentChange.Type.ADDED) {

                            userArrayList.add(dc.document.toObject(Report::class.java))

                        }
                    }

                    myAdapter.notifyDataSetChanged()

                }


            })


    }

}
