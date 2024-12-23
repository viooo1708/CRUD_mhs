package com.viona.crud_mhs.sreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.viona.crud_mhs.MainActivity
import com.viona.crud_mhs.R
import com.viona.crud_mhs.model.LoginResponse
import com.viona.crud_mhs.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val etUsername : EditText = findViewById(R.id.txtUsernamelogin)
        val etPassword  : EditText = findViewById(R.id.txtPasswordLogin)
        val btnLogin : Button = findViewById(R.id.btnLogin)
        val register : TextView = findViewById(R.id.txtRegister)

        register.setOnClickListener(){
            val toRegister = Intent(this@LoginActivity, RegisterScreenActivity::class.java)
            startActivity(toRegister)
        }

        btnLogin.setOnClickListener(){
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if(username.isEmpty() || password.isEmpty()){
                Toast.makeText(this@LoginActivity,"Username atau password tidak boleh kosong",
                    Toast.LENGTH_SHORT).show()
            }else{
              ApiClient.retrofit.login(username,password).enqueue(
                  object : Callback<LoginResponse> {
                      override fun onResponse(
                          call: Call<LoginResponse>,
                          response: Response<LoginResponse>
                      ) {
                          if(response.isSuccessful){
                              val loginResponse = response.body()
                              if (loginResponse != null && response.isSuccessful){
                                  //login berhasil
                                  Toast.makeText(this@LoginActivity,response.body()?.message,
                                      Toast.LENGTH_SHORT
                                  ).show()
                                  //mau pindah ke page lain
                                  val toMain = Intent(this@LoginActivity, MainActivity::class.java)
                                  startActivity(toMain)
                              }
                          }else{
                              val errorMessage = response.errorBody()?.string()?: "Unknown Error"
                              Log.e("Register Error" , errorMessage)
                              Toast.makeText(
                                  this@LoginActivity,
                                  "Login Failur",
                                  Toast.LENGTH_SHORT).show()

                          }
                      }

                      override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                          Toast.makeText(this@LoginActivity,t.message,
                              Toast.LENGTH_SHORT).show()
                      }

                  }
              )
            }
        }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}