package com.madry.photos.ui.gallery

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.madry.photos.R
import com.madry.photos.model.GalleryItemEntity

class GalleryAdapter(private val list: MutableList<GalleryItemEntity>,
                     private val itemClickListener: GalleryItemClickListener):
                                            RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list[position])
        holder.itemView.setOnClickListener({
            itemClickListener.onGalleryItemClick(list[position])
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        lateinit var textView :TextView
        lateinit var imageView: ImageView

        fun bindItems(data : GalleryItemEntity){
            textView = itemView.findViewById(R.id.txtvTitle)
            imageView = itemView.findViewById(R.id.imgvPhoto)
            textView.text = data.title
            Glide.with(itemView)
                    .load(data.filepath)
                    .apply(RequestOptions.centerCropTransform())
                    .into(imageView)
        }
    }
}