package com.bignerdranch.android.photogallery


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import api.FlickrApi
import api.FlickrResponse
import api.PhotoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "FlickrFetchr"
class FlickrFetchr {
    private val flickrApi: FlickrApi
    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")//Задается базовый URL для  конечной точки
            .addConverterFactory(GsonConverterFactory.create())//возвращает бъект LiveData, обертывающий список элементов галереи
            .build()//возвращает экземпляр Retrofit, у которого появляются настройки, заданные с помощью объекта builder,Полуеный объект используется его для создания экземпляра  интерфейса API.
        flickrApi = retrofit.create(FlickrApi::class.java)
    }

    fun fetchPhotos(): LiveData<List<GalleryItem>> {
        val responseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()
        val flickrRequest: Call<FlickrResponse> = flickrApi.fetchPhotos()//ставит в очередь сетевой запрос и обертывает результат в LiveData
        flickrRequest.enqueue(object : Callback<FlickrResponse>//Функция Call.enqueue(...) выполняет веб-запрос, находящийся в объекте Call.
        {
            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }
    /*выделение списка элементов галереи из ответа и обновление LiveData*/
            override fun onResponse(
                call: Call<FlickrResponse>,
                response: Response<FlickrResponse>
            ) {
                Log.d(TAG, "Response received")

                val flickrResponse: FlickrResponse? = response.body()
                val photoResponse: PhotoResponse? = flickrResponse?.photos
                var galleryItems: List<GalleryItem> = photoResponse?.galleryItems
                    ?: mutableListOf()
                galleryItems = galleryItems.filterNot {
                    it.url.isBlank()
                }// фильтрация элементов галереи с пустыми значениями URL-адреса, используя filterNot{...}
                responseLiveData.value = galleryItems
            }
        })
        return responseLiveData
    }
}