package com.madry.photos.ui.gallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.Toast
import com.madry.photos.R
import com.madry.photos.db.DbUtils
import com.madry.photos.model.GalleryItem
import com.madry.photos.model.GalleryItemEntity
import com.madry.photos.ui.SinglePhotoActivity
import com.madry.photos.utils.FileUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.text.SimpleDateFormat
import java.util.*



class GalleryActivity: AppCompatActivity() {
    companion object {
        private const val GRID_LAYOUT_ITEM_COUNT = 3
        /*package*/ const val REQUEST_IMAGE_CAPTURE = 1001
        private const val EXTRA_CURRENT_PHOTO_FILEPATH = "extra_current_photo_filepath"
        /*package*/ const val EXTRA_PHOTO_LIST = "extra_photo_list"
    }
    private lateinit var dbUtils: DbUtils
    private lateinit var adapter: GalleryAdapter
    /*package*/ val photoList = arrayListOf<GalleryItemEntity>()
    private val fileUtils = FileUtils()

    var currentPhoto : File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        dbUtils = DbUtils.getInstance(baseContext)
        setupRecyclerView()
        findViewById<Button>(R.id.btnAddPhoto).setOnClickListener({ tryRequestingCamera() })
        if (savedInstanceState == null) {
            loadDataFromDatabase()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val item = GalleryItemEntity()
            item.title = currentPhoto!!.name
            item.filepath = currentPhoto!!.absolutePath
            photoList.add(item)
            savePhotoToDatabase(item)
            adapter.notifyDataSetChanged()
        }
    }
    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(EXTRA_CURRENT_PHOTO_FILEPATH, currentPhoto?.absolutePath)
        outState?.putParcelableArrayList(EXTRA_PHOTO_LIST, photoList)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState?.getString(EXTRA_CURRENT_PHOTO_FILEPATH) != null) {
            currentPhoto = File(savedInstanceState.getString(EXTRA_CURRENT_PHOTO_FILEPATH))
        }

        val savedPhotos : ArrayList<GalleryItemEntity>? = savedInstanceState?.getParcelableArrayList(EXTRA_PHOTO_LIST)
        if (savedPhotos != null) {
            photoList.clear()
            photoList.addAll(savedPhotos)
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = GalleryAdapter(photoList, object : GalleryItemClickListener {
            override fun onGalleryItemClick(item: GalleryItem) {
                startActivity(SinglePhotoActivity.createInstance(baseContext, item))
            }
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(baseContext, GRID_LAYOUT_ITEM_COUNT)
    }

    private fun tryRequestingCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            currentPhoto = fileUtils.createFile(baseContext)
            if (currentPhoto != null) {
                val photoURI = FileProvider.getUriForFile(this,
                        getString(R.string.fileprovider_path),
                        currentPhoto!!)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } else {
                showToast(R.string.error_file_creation)
            }
        } else {
            showToast(R.string.error_camera_not_available)
        }
    }

    private fun loadDataFromDatabase() {
        dbUtils.loadFromDatabase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::onSuccessLoadDataFromDatabase, ::onFailureLoadDataFromDatabase)
    }

    /*package*/ fun onSuccessLoadDataFromDatabase(items: List<GalleryItemEntity>) {
        photoList.addAll(items)
        adapter.notifyDataSetChanged()
    }

    private fun onFailureLoadDataFromDatabase(throwable: Throwable) {
        showToast(R.string.error_load_database)
    }

    private fun savePhotoToDatabase(item: GalleryItemEntity) {
        dbUtils.savePhotoToDatabase(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, ::onFailureSavePhotoToDatabase)
    }

    private fun onFailureSavePhotoToDatabase(throwable: Throwable) {
        showToast(R.string.error_save_to_database)
    }

    private fun showToast(resource: Int) {
        Toast.makeText(baseContext, resource, Toast.LENGTH_SHORT).show()
    }
}
