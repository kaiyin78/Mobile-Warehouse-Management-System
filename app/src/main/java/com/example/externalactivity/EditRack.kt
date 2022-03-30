package com.example.externalactivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.externalactivity.databinding.FragmentEditRackBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditRack : Fragment() {

    val bundle = Bundle()
    private lateinit var binding: FragmentEditRackBinding
    private val nav by lazy { findNavController() }
    private val db= Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= FragmentEditRackBinding.inflate(inflater,container, false)
        binding.btnEdit.setOnClickListener { update() ; nav.navigate(R.id.action_editRack_to_rackManagement)}
        val rackID= arguments?.getString("rackID")
        val rackName= arguments?.getString("rackName")
        val rackSize= arguments?.getString("rackSize").toString()

        binding.tvEditRackID2.setText(rackID)
        binding.ettEditRackName.setText(rackName)
        binding.ettEditRS.setText(rackSize)
        return binding.root
    }

    private fun update() {
        val rackID= binding.tvEditRackID2.text.toString().trim()
        val rackName= binding.ettEditRackName.text.toString().trim()
        val rackSize= binding.ettEditRS.text.toString().trim()

        val Rack = hashMapOf(
            "rackID" to rackID,
            "rackName" to  rackName,
            "rackSize" to  rackSize,
        )

        db.collection("Rack").document(rackID).update(
            mapOf(  "rackID" to rackID,
                "rackName" to  rackName,
                "rackSize" to  rackSize)
        )
            .addOnSuccessListener { toast("Edit Rack Successfully") }
            .addOnFailureListener{toast("Edit Rack Failed!")}
        Thread.sleep(400);

    }

    private fun toast(text: String) {
        Toast.makeText(requireActivity(),text, Toast.LENGTH_SHORT).show()
    }

}