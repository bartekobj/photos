package com.madry.photos.db

import android.content.Context
import com.madry.photos.model.GalleryItemEntity
import com.madry.photos.model.Models
import com.madry.photos.utils.SingletonHolder
import io.reactivex.Observable
import io.reactivex.Single
import io.requery.Persistable
import io.requery.android.sqlite.DatabaseSource
import io.requery.reactivex.KotlinReactiveEntityStore
import io.requery.sql.KotlinEntityDataStore
import io.requery.sql.TableCreationMode


class DbUtils private constructor(context: Context) {
    private var database: KotlinReactiveEntityStore<Persistable>
    init {
        val source = DatabaseSource(context, Models.DEFAULT, 1)
        source.setTableCreationMode(TableCreationMode.DROP_CREATE)
        database = KotlinReactiveEntityStore(KotlinEntityDataStore(source.configuration))
    }

    companion object : SingletonHolder<DbUtils, Context>(::DbUtils)

    fun loadFromDatabase() : Single<MutableList<GalleryItemEntity>> {
        return database.select(GalleryItemEntity::class)
                .get()
                .observable()
                .toList()
    }

    fun savePhotoToDatabase(item: GalleryItemEntity): Observable<GalleryItemEntity> {
        return database.upsert(item)
                .toObservable()
    }
}