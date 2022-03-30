package com.example.externalactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.externalactivity.databinding.FragmentDeleteRackBinding


class DeleteRack : Fragment() {

    private lateinit var binding: FragmentDeleteRackBinding
    private val nav by lazy { findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDeleteRackBinding.inflate(inflater,container,false)
        binding.btnDeleteClose.setOnClickListener { nav.navigate(R.id.action_deleteRack_to_rackManagement)}

        return binding.root
    }




}