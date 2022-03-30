package com.example.externalactivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth



class ForgetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        val btn_submit: Button = findViewById(R.id.btn_submit)
        val tfEmail: TextView = findViewById(R.id.tfEmail)

        btn_submit.setOnClickListener {
            val email: String = tfEmail.text.toString().trim{ it <= ' '}
            if(email.isEmpty()){
                Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show()
            }
            else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{ task ->
                        if(task.isSuccessful){
                            Toast.makeText(this, "Email sent successfully to reset your password", Toast.LENGTH_LONG).show()
                            finish()
                        }else{
                            Toast.makeText(this, task.exception!!.message.toString(),Toast.LENGTH_LONG).show()
                        }
                    }
            }

        }
    }
}