package com.example.externalactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.externalactivity.databinding.ActivityRegisterBinding

import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var user: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()
        binding.btnToLogin.setOnClickListener(){

            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
            finish()

        }
        binding.btnRegister.setOnClickListener {

            registerUser()
        }


    }

    private fun registerUser() {

        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {

            user.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "User added successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(
                            Intent(
                                this,
                                MainActivity::class.java
                            )
                        )
                        finish()

                    }
//                    } else {

//                        user.signInWithEmailAndPassword(email, password)
//                            .addOnCompleteListener { mTask ->
//
//                                if (mTask.isSuccessful) {
//
//                                    startActivity(
//                                        Intent(
//                                            this,
//                                            SecondActivity::class.java
//                                        )
//                                    )
//                                    finish()
//
//
//                                } else {
//                                    Toast.makeText(
//                                        this,
//                                        mTask.exception!!.message,
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//
//
//                                }
//
//                            }
//
//                    }
                }
        }else
        {
            Toast.makeText(
                this,
                "Email and password cannot be empty",
                Toast.LENGTH_SHORT
            ).show()

        }

    }
}
