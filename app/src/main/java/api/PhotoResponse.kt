package api

import com.bignerdranch.android.photogallery.GalleryItem
import com.google.gson.annotations.SerializedName

class PhotoResponse {
    @SerializedName("photo")//хранение списка галерейных объектов и примечаний к нему
    lateinit var galleryItems: List<GalleryItem>
}