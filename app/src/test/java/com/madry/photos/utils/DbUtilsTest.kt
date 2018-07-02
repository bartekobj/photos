package com.madry.photos.utils

import com.madry.photos.BuildConfig
import com.madry.photos.db.DbUtils
import com.madry.photos.model.GalleryItemEntity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class DbUtilsTest {
    private lateinit var dbUtils: DbUtils

    @Before
    fun setup() {
        val context = RuntimeEnvironment.application.baseContext
        dbUtils = DbUtils.getInstance(context)
    }

    @Test
    fun insertAndSelectSingleEntity_success() {
        val item = GalleryItemEntity()
        item.title = "title"
        item.filepath = "filepath"

        val entity = dbUtils.savePhotoToDatabase(item).singleOrError().blockingGet()
        assert(entity.title == item.title)
        assert(entity.filepath == item.filepath)
        assert(dbUtils.loadFromDatabase().blockingGet().count() == 1)
        val fromDb = dbUtils.loadFromDatabase().blockingGet().get(0)
        assert(item.title == fromDb.title)
        assert(item.filepath == fromDb.filepath)
    }

    @Test
    fun insertAndSelectMultipleSameEntity_success() {
        val item = GalleryItemEntity()
        item.title = "title"
        item.filepath = "filepath"

        dbUtils.savePhotoToDatabase(item).singleOrError().blockingGet()
        assert(dbUtils.loadFromDatabase().blockingGet().count() == 1)
        dbUtils.savePhotoToDatabase(item).singleOrError().blockingGet()
        assert(dbUtils.loadFromDatabase().blockingGet().count() == 1)
        val fromDb = dbUtils.loadFromDatabase().blockingGet()[0]
        assert(item.title == fromDb.title)
        assert(item.filepath == fromDb.filepath)
    }

    @Test
    fun insertAndSelectMultipleEntity_success() {
        val item1 = GalleryItemEntity()
        val item2 = GalleryItemEntity()
        val item3 = GalleryItemEntity()
        val item4 = GalleryItemEntity()

        item1.title = "title"
        item1.filepath = "filepath"
        item2.title = "title"
        item2.filepath = "filepath"
        item3.title = "title"
        item3.filepath = "filepath"
        item4.title = "title"
        item4.filepath = "filepath"

        dbUtils.savePhotoToDatabase(item1).singleOrError().blockingGet()
        assert(dbUtils.loadFromDatabase().blockingGet().count() == 1)
        dbUtils.savePhotoToDatabase(item2).singleOrError().blockingGet()
        assert(dbUtils.loadFromDatabase().blockingGet().count() == 2)
        dbUtils.savePhotoToDatabase(item3).singleOrError().blockingGet()
        dbUtils.savePhotoToDatabase(item4).singleOrError().blockingGet()
        assert(dbUtils.loadFromDatabase().blockingGet().count() == 4)
    }
}