package com.example.externalactivity

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.navigation.fragment.findNavController
import com.example.externalactivity.databinding.FragmentAddRackBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Thread.sleep

class AddRack : Fragment() {

    private lateinit var binding: FragmentAddRackBinding
    private val nav by lazy { findNavController() }
    private val db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentAddRackBinding.inflate(inflater, container, false)
        binding.btnAdd.setOnClickListener { this.addRack() ; }
        return binding.root
    }

    private fun addRack() {
        val rackID = binding.ettRackID.text.toString().trim()
        val rackName = binding.ettRackName.text.toString()
        val rackSize = binding.ettRS.text.toString()

        val rackData = hashMapOf(
            "rackID" to rackID,
            "rackName" to rackName,
            "rackSize" to rackSize
        )
        if(rackID.isEmpty()){
            binding.ettRackID.error="Rack ID Required!"
            binding.ettRackID.requestFocus()
        }
        else if(rackName.isEmpty())
        {
            binding.ettRackName.error="Rack Name Required!"
            binding.ettRackName.requestFocus()
        }
        else if(!rackSize.isDigitsOnly())
        {
            binding.ettRS.error="Please try again!"
            binding.ettRS.requestFocus()
        }
        else{
            db.collection("Rack").document(rackID).get().addOnSuccessListener {
                if(it.exists()){
                    if(it.exists()){
                        binding.ettRackID.error="Duplicate Rack ID. Please Try Again"
                        binding.ettRackID.requestFocus()
                    }
                }
                else{
                    db.collection("Rack").document(rackID).set(rackData)
                        .addOnSuccessListener { toast("Add Rack Successfully") }
                        .addOnFailureListener{toast("Add Rack Failed!")}
                    sleep(400);
                    nav.navigate(R.id.action_addRack_to_rackManagement)
                }
            }
        }

    }

    private fun toast(text: String) {
        Toast.makeText(requireActivity(),text, Toast.LENGTH_SHORT).show()
    }


}