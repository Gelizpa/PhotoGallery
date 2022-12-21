package com.bignerdranch.android.photogallery

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

private const val TAG = "PhotoGalleryFragment"
private lateinit var photoGalleryViewModel: PhotoGalleryViewModel


class PhotoGalleryFragment : Fragment() {
    private lateinit var photoRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true                                                            //сохранение фрагмента
        setHasOptionsMenu(true)                                                           //Регистрация фрагментов для обратного вызова в меню
        photoGalleryViewModel =
            ViewModelProviders.of(this).get(PhotoGalleryViewModel::class.java)
    }

   //Настройка фрагмента

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)
        photoRecyclerView = view.findViewById(R.id.photo_recycler_view)
        photoRecyclerView.layoutManager = GridLayoutManager(context, 3)          //создание 3-х столбцов
        return view
    }

    //Наблюдение за «живыми» данными ViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoGalleryViewModel.galleryItemLiveData.observe(                                //удаление наблюдателя при уничтожении представления фрагмента
            viewLifecycleOwner,
            Observer { galleryItems ->
                photoRecyclerView.adapter = PhotoAdapter(galleryItems)                    //адаптер для наблюдения за доступностью и изменением данных
            })
    }

 //регистрация фрагмента для получения обратных вызовов меню.

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photo_gallery, menu)

//Регистрация событий обратного вызова при отправке запроса

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView
        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(queryText: String): Boolean {
                    Log.d(TAG, "QueryTextSubmit: $queryText")
                    photoGalleryViewModel.fetchPhotos(queryText)
                    return true
                }
                override fun onQueryTextChange(queryText: String): Boolean {                 //Обратный вызов onQueryTextChange(String) выполняется при изменении текста в текстовом поле SearchView
                    Log.d(TAG, "QueryTextChange: $queryText")
                    return false
                }
            })
            //Предварительное заполнение поисковой строки
            setOnSearchClickListener {
                searchView.setQuery(photoGalleryViewModel.searchTerm, false)
            }
        }
    }

    //Очистка сохраненного запроса
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_clear -> {
                photoGalleryViewModel.fetchPhotos("")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


   //Добавление реализации хранения ссылок на представление элемента
    private class PhotoHolder(itemImageView: ImageView)
        : RecyclerView.ViewHolder(itemImageView) {
        val bindImageView: (ImageView) = itemImageView}



   private inner class PhotoAdapter(private val galleryItems: List<GalleryItem>)
       : RecyclerView.Adapter<PhotoHolder>() {
        //выдает PhotoHolder из галереи(заполняет файл list_item_gallery.xml и передаёт его конструктору PhotoHolder
       override fun onCreateViewHolder(                                                                 //Adapter.onCreateViewHolder(...) отвечает за создание представления на дисплее, оборачивает его в холдер и возвращает результат
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


        //возвращает количество элементов
       override fun getItemCount(): Int = galleryItems.size


       // отвечает за заполнение из данной позиции position
        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            lateinit var itemImageView: ImageView
            val galleryItem = galleryItems[position]

            //
            Picasso.get()                                      //задает динамические интерфейсы
                .load(galleryItem.url)                        //адрес загружаемого изображения
                .placeholder(R.drawable.bill_up_close)        //изображение, которое выводится до полной загрузки необходимого изображения
                .into(holder.bindImageView)                   //для загрузки результатов
        }

    }
    companion object {
        //передача аргументов при создании нового экземпляра фрагмента
        fun newInstance() = PhotoGalleryFragment()
    }
}