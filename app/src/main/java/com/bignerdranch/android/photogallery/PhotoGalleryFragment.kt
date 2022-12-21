package com.bignerdranch.android.photogallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.FlickrApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "PhotoGalleryFragment"

class PhotoGalleryFragment : Fragment() {
    private lateinit var photoRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://www.flickr.com/")//Задается базовый URL для  конечной точки
            .addConverterFactory(ScalarsConverterFactory.create())//преобразовывает ответ в строку
            .build()//возвращает экземпляр Retrofit, у которого появляются настройки, заданные с помощью объекта builder,Полуеный объект используется его для создания экземпляра  интерфейса API.
        val flickrApi: FlickrApi = retrofit.create(FlickrApi::class.java)
        val flickrHomePageRequest: Call<String> = flickrApi.fetchContents()//возвращает объект Call<String>, представляющий собой веб-запрос

        flickrHomePageRequest.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.d(TAG, "Response received: ${response.body()}")
            }
        })//Функция Call.enqueue(...) выполняет веб-запрос, находящийся в объекте Call.
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)
        photoRecyclerView = view.findViewById(R.id.photo_recycler_view)
        photoRecyclerView.layoutManager = GridLayoutManager(context, 3)//создание 3-х столбцов
        return view
    }
    companion object {
        fun newInstance() = PhotoGalleryFragment()
    }
}