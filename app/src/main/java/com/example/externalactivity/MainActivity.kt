package com.example.externalactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.externalactivity.databinding.ActivityMainBinding


import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var user: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        binding.tvForgotPassword.setOnClickListener(){


            startActivity(
                Intent(
                    this,
                    ForgetPasswordActivity::class.java
                )
            )
            finish()

        }

        binding.btnToRegister.setOnClickListener(){

            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
            finish()

        }

        binding.btnLogin.setOnClickListener {

            loginUser()


        }

    }

    private fun loginUser() {

        val email = binding.etLEmail.text.toString()
        val password = binding.etLPassword.text.toString()

        user.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { mTask ->

                if (mTask.isSuccessful) {

                    startActivity(
                        Intent(
                            this,
                            MenuActivity::class.java
                        )
                    )
                    finish()


                } else {
                    Toast.makeText(
                        this,
                        mTask.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()


                }

            }
    }


}














