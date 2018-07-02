package com.madry.photos.model

import android.os.Parcelable
import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import io.requery.Persistable

@Entity
interface GalleryItem : Persistable, Parcelable {
    @get:Key
    @get:Generated
    var id: Int
    var title: String
    var filepath: String
}