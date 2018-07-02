package com.madry.photos.utils

import android.content.Context
import com.madry.photos.BuildConfig
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.text.SimpleDateFormat

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class FileUtilsTest {
    private val fileUtils = FileUtils()
    private lateinit var context: Context

    @Before
    fun setup() {
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun createFile_success() {
        val f = fileUtils.createFile(context)
        assert(f!!.exists())
        assert("" + f.extension == FILE_SUFIX)
        val sdf = SimpleDateFormat(DATE_FORMAT)
        val date = sdf.parse(f.nameWithoutExtension)!!
    }

    @Test(expected = NullPointerException::class)
    fun createFile_nullContext_failure() {
        val c: Context? = null
        val f = fileUtils.createFile(c!!)
        assert(f!!.exists())
    }
}