package com.viona.crud_mhs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.viona.crud_mhs.adapter.BeritaAdapter
import com.viona.crud_mhs.model.ModelBerita
import com.viona.crud_mhs.model.ResponseBerita
import com.viona.crud_mhs.service.ApiClient
import com.viona.crud_mhs.sreen.TambahDataUserScreenActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var swipeRefreshLayout : SwipeRefreshLayout
    private lateinit var recycleview  : RecyclerView
    private lateinit var call : Call<ResponseBerita>
    private lateinit var beritaAdapter : BeritaAdapter
    private lateinit var btnTambahData: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        swipeRefreshLayout = findViewById(R.id.refresh_layout)
        recycleview = findViewById(R.id.rv_berita)
        btnTambahData = findViewById(R.id.btnTambahData)

        btnTambahData.setOnClickListener(){
            val  toMain = Intent(this@MainActivity, TambahDataUserScreenActivity::class.java)
            startActivity(toMain)
        }


        beritaAdapter = BeritaAdapter{modelBerita : ModelBerita -> beritaOnClick(modelBerita) }
        recycleview.adapter = beritaAdapter

        recycleview.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL,
            false)

        swipeRefreshLayout.setOnRefreshListener {
            getData() //method untuk proses ambil data
        }

        getData()


    }

    private fun beritaOnClick(modelBerita: ModelBerita) {
        val intent = Intent(this, DetailBeritaAcitvity::class.java).apply {
            putExtra("gambar", modelBerita.gambar_berita)
            putExtra("judul", modelBerita.judul)
            putExtra("isi", modelBerita.isi_berita)
            putExtra("tanggal", modelBerita.tgl_berita)

        }
        startActivity(intent)
    }


    private fun getData() {
        swipeRefreshLayout.isRefreshing = true
        call = ApiClient.retrofit.getAllBerita()
        call.enqueue(object : Callback<ResponseBerita>{
            override fun onResponse(
                call: Call<ResponseBerita>,
                response: Response<ResponseBerita>
            ) {
                swipeRefreshLayout.isRefreshing = false
                if(response.isSuccessful){
                    beritaAdapter.submitList(response.body()?.data)
                    beritaAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ResponseBerita>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(applicationContext, t.localizedMessage,
                    Toast.LENGTH_SHORT).show()
            }

        })
    }
}