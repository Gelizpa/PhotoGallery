package com.bignerdranch.android.photogallery

import android.content.Context
import android.preference.PreferenceManager

private const val PREF_SEARCH_QUERY = "searchQuery"                                                  // PREF_SEARCH_QUERY используется в качестве ключа для хранения запроса

                                                                                                      //класс для работы с хранимым запросом
object QueryPreferences {

    //возвращает значение запроса, хранящееся в общих настройках

    fun getStoredQuery(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PREF_SEARCH_QUERY, "")!!
    }

   //записывает запрос в хранилище общих настроек для заданного контекста

    fun setStoredQuery(context: Context, query: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(PREF_SEARCH_QUERY, query)
            .apply()                                                                                //apply() для объекта Editor, чтобы  изменения стали видимыми для всех пользователей файла
    }
}