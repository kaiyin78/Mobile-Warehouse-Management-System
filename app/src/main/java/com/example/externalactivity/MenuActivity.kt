package com.example.externalactivity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.auth.FirebaseAuth
import java.net.URL

class MenuActivity : AppCompatActivity() {
    private lateinit var user: FirebaseAuth
    private val nav by lazy{supportFragmentManager.findFragmentById(R.id.host)!!.findNavController()}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        user = FirebaseAuth.getInstance()
        setupActionBarWithNavController(nav)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        user = FirebaseAuth.getInstance()
        return when(item?.itemId){
            R.id.action_signout->
            {
                user.signOut()
                Toast.makeText(this,"Signed Out", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else->
            {
                return super.onOptionsItemSelected(item)
            }
        }






    }
    override fun onSupportNavigateUp(): Boolean {
        return nav.navigateUp() || super.onSupportNavigateUp()
    }
}
