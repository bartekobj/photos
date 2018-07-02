package com.madry.photos.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.madry.photos.R
import com.madry.photos.model.GalleryItem
import com.madry.photos.model.GalleryItemEntity
import org.hamcrest.Matchers.`is`
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SinglePhotoActivityTest {
    private val TITLE = "custom_title"

    @Test
    fun onCreate_dataIsLoaded() {
        val activity = Robolectric.buildActivity(SinglePhotoActivity::class.java,
                createIntent()).create().get()

        val txtvTitle = activity.findViewById<TextView>(R.id.txtvTitle)
        assertEquals(txtvTitle.text.toString(), TITLE)
    }

    @Test
    fun restoreInstanceState_dataIsLoaded() {
        val savedInstanceState = Bundle()
        Robolectric.buildActivity(SinglePhotoActivity::class.java, createIntent())
                .create()
                .restoreInstanceState(savedInstanceState)
                .get()
        assertFalse(savedInstanceState.containsKey(SinglePhotoActivity.EXTRA_GALLERY_ITEM))

        savedInstanceState.putParcelable(SinglePhotoActivity.EXTRA_GALLERY_ITEM, createGalleryItem())
        Robolectric.buildActivity(SinglePhotoActivity::class.java, createIntent())
                .create()
                .restoreInstanceState(savedInstanceState)
                .get()
        assertTrue(savedInstanceState.containsKey(SinglePhotoActivity.EXTRA_GALLERY_ITEM))
        val item = savedInstanceState.getParcelable<GalleryItem>(SinglePhotoActivity.EXTRA_GALLERY_ITEM)
        assertEquals(item!!.title, TITLE)
    }

    private fun createIntent(): Intent {
        val intent = Intent()
        intent.putExtra(SinglePhotoActivity.EXTRA_GALLERY_ITEM, createGalleryItem())

        return intent
    }

    private fun createGalleryItem(): GalleryItem {
        val item = GalleryItemEntity()
        item.title = TITLE

        return item
    }
}