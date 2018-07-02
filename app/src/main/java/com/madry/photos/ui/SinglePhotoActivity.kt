package com.madry.photos.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.madry.photos.R
import com.madry.photos.model.GalleryItem


class SinglePhotoActivity : AppCompatActivity() {
    companion object {
        /*package*/ const val EXTRA_GALLERY_ITEM = "extra_gallery_item"

        fun createInstance(context: Context, item: GalleryItem): Intent {
            val intent = Intent(context, SinglePhotoActivity::class.java)
            intent.putExtra(EXTRA_GALLERY_ITEM, item)
            return intent
        }
    }

    private lateinit var imgvPhoto: ImageView
    private lateinit var txtvTitle: TextView
    private lateinit var galleryItem: GalleryItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_photo)

        imgvPhoto = findViewById(R.id.imgvPhoto)
        txtvTitle = findViewById(R.id.txtvTitle)
        getDataFromBundle(intent.extras)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        getDataFromBundle(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelable(EXTRA_GALLERY_ITEM, galleryItem)
        super.onSaveInstanceState(outState)
    }

    private fun getDataFromBundle(bundle: Bundle?) {
        if (bundle != null && bundle.containsKey(EXTRA_GALLERY_ITEM)) {
            galleryItem = bundle.getParcelable(EXTRA_GALLERY_ITEM)
            loadImage()
        }
    }

    private fun loadImage() {
        Glide.with(this).load(galleryItem.filepath)
                .apply(RequestOptions.fitCenterTransform())
                .into(imgvPhoto)
        txtvTitle.text = galleryItem.title
    }
}
