package com.example.externalactivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation


class MenuFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_menu, container, false)

        val btnToReport:Button =  v.findViewById(R.id.btnToReport)
        val btnToReceiveProduct:Button =  v.findViewById(R.id.btnToReceiveProduct)
        val btnToRackOut:Button =  v.findViewById(R.id.btnToRackOut)
        val btnToRackmanagent:Button =  v.findViewById(R.id.btnToRackManagement)
        val btnToProductList:Button =  v.findViewById(R.id.btnToProductList)
        val btnToProductStatus:Button= v.findViewById(R.id.btnToRackIn)


        btnToReport.setOnClickListener{
            Navigation.findNavController(v).navigate(R.id.action_menuFragment_to_reportFragment)
        }
        btnToReceiveProduct.setOnClickListener{
            Navigation.findNavController(v).navigate(R.id.action_menuFragment_to_receiveProduct3)
        }
        btnToProductList.setOnClickListener{
            Navigation.findNavController(v).navigate(R.id.action_menuFragment_to_productList2)
        }
        btnToRackmanagent.setOnClickListener{
            Navigation.findNavController(v).navigate(R.id.action_menuFragment_to_rackManagement)
        }
        btnToProductStatus.setOnClickListener{
            Navigation.findNavController(v).navigate(R.id.action_menuFragment_to_productStatus)
        }
        btnToRackOut.setOnClickListener{
            Navigation.findNavController(v).navigate(R.id.action_menuFragment_to_rackOutFragment)
        }

        return v
    }


}