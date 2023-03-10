package com.bignerdranch.android.photogallery


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import api.FlickrApi
import api.FlickrResponse
import api.PhotoInterceptor
import api.PhotoResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
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

//перехватчик в конфигурации Retrofit
        val client = OkHttpClient.Builder()
            .addInterceptor(PhotoInterceptor())
            .build()



        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")                              //Задается базовый URL для  конечной точки
            .addConverterFactory(GsonConverterFactory.create())                     //возвращает бъект LiveData, обертывающий список элементов галереи
            .client(client)
            .build()                                                                //возвращает экземпляр Retrofit, у которого появляются настройки, заданные с помощью объекта builder,Полуеный объект используется его для создания экземпляра  интерфейса API.
        flickrApi = retrofit.create(FlickrApi::class.java)
    }

    //функция поиска
    fun fetchPhotos(): LiveData<List<GalleryItem>> {
        return fetchPhotoMetadata(flickrApi.fetchPhotos())
    }
    fun searchPhotos(query: String): LiveData<List<GalleryItem>> {
        return fetchPhotoMetadata(flickrApi.searchPhotos(query))
    }
    private fun fetchPhotoMetadata(flickrRequest: Call<FlickrResponse>)
            : LiveData<List<GalleryItem>> {
        val responseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()



        flickrRequest.enqueue(object : Callback<FlickrResponse>                     //Функция Call.enqueue(...) выполняет веб-запрос, находящийся в объекте Call.
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


  //Добавление загрузки изображения
    @WorkerThread                                                                      //@WorkerThread указывает, что эта функция должна вызываться только в фоновом потоке
    fun fetchPhoto(url: String): Bitmap? {
        val response: Response<ResponseBody> = flickrApi.fetchUrlBytes(url).execute()  //Call.execute(),  синхронно выполняет веб-запрос
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
        Log.i(TAG, "Decoded bitmap=$bitmap from Response=$response")
        return bitmap
    }
}