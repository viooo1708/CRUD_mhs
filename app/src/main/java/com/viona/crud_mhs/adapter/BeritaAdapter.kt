package com.viona.crud_mhs.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.viona.crud_mhs.DetailBeritaAcitvity
import com.viona.crud_mhs.R
import com.viona.crud_mhs.model.ModelBerita

class BeritaAdapter
    (
    private val onClick: (ModelBerita) -> Unit
) : ListAdapter<ModelBerita, BeritaAdapter.ProdukViewHolder>(ProdukCallBack) {

    class ProdukViewHolder(itemView: View, val onClick: (ModelBerita) -> Unit) :

        RecyclerView.ViewHolder(itemView) {
        private val imgBerita: ImageView = itemView.findViewById(R.id.imgBerita)
        private val title: TextView = itemView.findViewById(R.id.judulBerita)
        private val tglBerita: TextView = itemView.findViewById(R.id.tglBerita)
        val cardBerita: CardView = itemView.findViewById(R.id.cardBerita)


        //cek produk yang saaat ini

        private var currentProduct: ModelBerita? = null

        init {
            itemView.setOnClickListener() {
                currentProduct?.let {
                    onClick(it)
                }
            }
        }

        fun bind(berita: ModelBerita) {
            currentProduct = berita
            //set ke widget
            title.text = berita.judul
            tglBerita.text = berita.tgl_berita


            //untuk menampilkan gambar
            Glide.with(itemView)
                .load("http://10.0.2.2/beritadb/gambar_berita/" + berita.gambar_berita)
                .centerCrop()
                .into(imgBerita)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdukViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.row_item_berita, parent, false
        )
        return ProdukViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ProdukViewHolder, position: Int) {
        val berita = getItem(position)
        holder.bind(berita)

        //detail

        holder.cardBerita.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailBeritaAcitvity::class.java)
            intent.putExtra("title", berita.judul)
            intent.putExtra("isi_berita", berita.isi_berita)
            intent.putExtra("gambar_berita", berita.gambar_berita)
            intent.putExtra("tgl_berita", berita.tgl_berita)

            holder.itemView.context.startActivity(intent)
        }
    }

}


object ProdukCallBack : DiffUtil.ItemCallback<ModelBerita>() {
    override fun areItemsTheSame(oldItem: ModelBerita, newItem: ModelBerita): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ModelBerita, newItem: ModelBerita): Boolean {
        return oldItem.id == newItem.id
    }

}
