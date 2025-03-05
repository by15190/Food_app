package com.example.food_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.emptyIntSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.food_app.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var googleSginInClient: GoogleSignInClient


    private val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // initialize firebase auth and database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // initialization of the google sign in client
        val gso =
            GoogleSignInOptions.Builder().requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build()
        googleSginInClient = GoogleSignIn.getClient(this, gso)

        // dont have account button
        binding.donthaveaccbtn.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }


        /// google sign in btn
        binding.googleLoginbtn.setOnClickListener {
            val signinintent = googleSginInClient.signInIntent
            launcher.launch(signinintent)
        }

        // login btn
        binding.Loginbtn.setOnClickListener {
            email = binding.loginemail.text.toString().trim()
            password = binding.loginpassword.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "filled the above", Toast.LENGTH_SHORT).show()
            } else {
                createuser()
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

            }
        }

    }

    private fun createuser() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "sign in successful", Toast.LENGTH_SHORT).show()
                val user: FirebaseUser? = auth.currentUser
                updateui(user)
            } else {
                Toast.makeText(this, "sign in failed: ${task.exception}", Toast.LENGTH_SHORT).show()
            }


        }
    }

    private fun updateui(user: com.google.firebase.auth.FirebaseUser?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result -> // result is the input from the launcher.launch
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)

                    auth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "login success", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this, "google login failed: ${task.exception} ", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "login failed", Toast.LENGTH_SHORT).show()
            }


        }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}