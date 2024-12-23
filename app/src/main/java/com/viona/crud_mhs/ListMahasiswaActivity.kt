package com.viona.crud_mhs

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.viona.crud_mhs.adapter.MahasiswaAdapter
import com.viona.crud_mhs.model.MahasiswaResponse
import com.viona.crud_mhs.model.ModelMahasiswa
import com.viona.crud_mhs.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListMahasiswaActivity : AppCompatActivity() {
    private lateinit var swipeRefreshLayout : SwipeRefreshLayout
    private lateinit var recycleview  : RecyclerView
    private lateinit var call : Call<MahasiswaResponse>
    private lateinit var mahasiswaAdapter : MahasiswaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_mahasiswa)

        swipeRefreshLayout = findViewById(R.id.refresh)
        recycleview = findViewById(R.id.rv_mahasiswa)

        mahasiswaAdapter = MahasiswaAdapter{modelMahasiswa : ModelMahasiswa ->(modelMahasiswa) }
        recycleview.adapter = mahasiswaAdapter

        recycleview.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL,
            false)

        swipeRefreshLayout.setOnRefreshListener {
            getData() //metho untuk proses ambil data
        }

        getData()



    }

    private fun getData() {
        swipeRefreshLayout.isRefreshing = true
        call = ApiClient.retrofit.getAllMahasiswa()
        call.enqueue(object : Callback<MahasiswaResponse> {
            override fun onResponse(
                call: Call<MahasiswaResponse>,
                response: Response<MahasiswaResponse>
            ) {
                swipeRefreshLayout.isRefreshing = false
                if(response.isSuccessful){
                    mahasiswaAdapter.submitList(response.body()?.data)
                    mahasiswaAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<MahasiswaResponse>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(applicationContext, t.localizedMessage,
                    Toast.LENGTH_SHORT).show()
            }

        })
    }
}