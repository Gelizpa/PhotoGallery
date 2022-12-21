package com.bignerdranch.android.photogallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PhotoGalleryActivityS : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_gallery_s)
        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, PhotoGalleryFragment.newInstance())//Проверка существования фрагмента
                .commit()
        }
    }
}