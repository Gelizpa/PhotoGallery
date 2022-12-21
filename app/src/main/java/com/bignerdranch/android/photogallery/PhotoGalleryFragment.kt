package com.bignerdranch.android.photogallery

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.FlickrApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "PhotoGalleryFragment"
private lateinit var photoGalleryViewModel: PhotoGalleryViewModel

class PhotoGalleryFragment : Fragment() {
    private lateinit var photoRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoGalleryViewModel =
            ViewModelProviders.of(this).get(PhotoGalleryViewModel::class.java)

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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoGalleryViewModel.galleryItemLiveData.observe(
            viewLifecycleOwner,
            Observer { galleryItems ->
                photoRecyclerView.adapter = PhotoAdapter(galleryItems)//адаптер для наблюдения за доступностью и изменением данных
            })
    }
    private class PhotoHolder(private val itemImageView: ImageView)
        : RecyclerView.ViewHolder(itemImageView) {
        val bindDrawable: (Drawable) -> Unit = itemImageView::setImageDrawable
    }
    private inner class PhotoAdapter(private val galleryItems: List<GalleryItem>)//inner, чтобы у PhotoAdapter появился прямой доступ к свойству layoutInflater родительской activity
        : RecyclerView.Adapter<PhotoHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): PhotoHolder {
            val view = layoutInflater.inflate(
                R.layout.list_item_gallery,
                parent,
                false
            ) as ImageView
            return PhotoHolder(view)
        }
        override fun getItemCount(): Int = galleryItems.size
        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = galleryItems[position]
   //временное изображение назначается объектом Drawable виджета ImageView
            val placeholder: Drawable = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.bill_up_close) ?: ColorDrawable()
            holder.bindDrawable(placeholder)
        }
    }
    companion object {
        fun newInstance() = PhotoGalleryFragment()
    }
}