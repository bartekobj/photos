package com.madry.photos.ui.gallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.widget.Button
import com.madry.photos.R
import com.madry.photos.model.GalleryItemEntity
import com.madry.photos.ui.SinglePhotoActivity
import com.madry.photos.utils.FileUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowToast
import java.util.ArrayList


@RunWith(RobolectricTestRunner::class)
class GalleryActivityTest {
    private var m_activity: GalleryActivity? = null
    private var m_recyclerView: RecyclerView? = null

    @Before
    @Throws(Exception::class)
    fun setup() {
        m_activity = Robolectric.setupActivity(GalleryActivity::class.java)
        m_recyclerView = m_activity!!.findViewById(R.id.recyclerView)
    }

    @Test
    @Throws(Exception::class)
    fun noCameraAvailable_showToast() {
        val addPhoto = m_activity!!.findViewById<Button>(R.id.btnAddPhoto)
        addPhoto.performClick()
        assertTrue(ShadowToast.showedToast(m_activity!!
                .getString(R.string.error_camera_not_available)))
    }

    @Test
    fun photoArriveFromCamera_itemIsAddedToList() {
        val fileUtils = FileUtils()
        m_activity!!.currentPhoto = fileUtils.createFile(m_activity!!.applicationContext)
        assertEquals(m_recyclerView!!.childCount, 0)
        m_activity!!.onActivityResult(GalleryActivity.REQUEST_IMAGE_CAPTURE, Activity.RESULT_OK, null)
        assertEquals(m_recyclerView!!.childCount, 1)
    }

    @Test
    @Throws(Exception::class)
    fun populateRecyclerView_selectItem() {
        m_activity!!.onSuccessLoadDataFromDatabase(mockList())

        assertEquals(m_recyclerView!!.childCount, 2)
        m_recyclerView!!.findViewHolderForAdapterPosition(0).itemView.performClick()

        val expectedIntent = Intent(m_activity, SinglePhotoActivity::class.java)
        val actual = shadowOf(RuntimeEnvironment.application).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun restoreInstanceState_dataIsLoaded() {
        val savedInstanceState = Bundle()
        val galleryActivity = Robolectric.buildActivity(GalleryActivity::class.java)
                .restoreInstanceState(savedInstanceState)
                .create().get()
        assertEquals(galleryActivity.photoList.size, 0)

        savedInstanceState.putParcelableArrayList(GalleryActivity.EXTRA_PHOTO_LIST, ArrayList<Parcelable>(mockList()))

        val activity = Robolectric.buildActivity(GalleryActivity::class.java)
                .restoreInstanceState(savedInstanceState)
                .create().get()
        assertEquals(activity.photoList.size.toLong(), 2)
    }

    private fun mockList(): List<GalleryItemEntity> {
        val list = ArrayList<GalleryItemEntity>()
        val item1 = GalleryItemEntity()
        item1.title = "title1"
        val item2 = GalleryItemEntity()
        item2.title = "title2"

        list.add(item1)
        list.add(item2)
        return list
    }
}