package com.bignerdranch.android.photogallery

import android.app.Application
import androidx.lifecycle.*

class PhotoGalleryViewModel(private val app: Application) : AndroidViewModel(app) {

    //хранение последнего запроса
    val galleryItemLiveData: LiveData<List<GalleryItem>>
    private val flickrFetchr = FlickrFetchr()
    private val mutableSearchTerm = MutableLiveData<String>()
    //Открытие поисковой фразы
    val searchTerm: String
        get() = mutableSearchTerm.value ?: ""

    init {
        mutableSearchTerm.value = QueryPreferences.getStoredQuery(app)
        galleryItemLiveData = Transformations.switchMap(mutableSearchTerm) { searchTerm ->
            if (searchTerm.isBlank()) {                                                             //Получение интересных фотографий, когда запрос пустой
                flickrFetchr.fetchPhotos()
            } else {
                flickrFetchr.searchPhotos(searchTerm)
            }                                                                                       // Запуск поискового запроса
        }
    }
    fun fetchPhotos(query: String = "") {
        QueryPreferences.setStoredQuery(app, query)
        mutableSearchTerm.value = query
    }
}