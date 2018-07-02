package com.madry.photos.ui.gallery

import com.madry.photos.model.GalleryItem

interface GalleryItemClickListener {
    fun onGalleryItemClick(item: GalleryItem)
}