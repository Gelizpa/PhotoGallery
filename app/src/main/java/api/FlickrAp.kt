package api

import retrofit2.Call
import retrofit2.http.GET

/*Аннотация @GET("/") в приведенном ниже коде настраивает Call на выполнение GET-запроса.
метод flickr.interestingness.getList.«возвращает список интересных фотографий за последний день или указанную пользователем дату»
 */

    interface FlickrApi {
        @GET("services/rest/?method=flickr.interestingness.getList&api_key=e56347be8eeb6062f06669edaa6603b6&format=json&nojsoncallbac k=1&extras=url_s"

            /*"services/rest/?method=flickr.interestingness.getList" +
                    "&api_key=e56347be8eeb6062f06669edaa6603b6" +
                    "&format=json" +
                    "&nojsoncallback=1" +// инструктирует Flickr убрать из ответа круглые скобки
                    "&extras=url_s"//добавить URL-адрес мини-версии изображения, если таковая есть*/
        )
        fun fetchPhotos(): Call<FlickrResponse>//FlickrResponse для десериализации JSON-данных в ответе
    }// Объект Call представляет собой один веб-запрос, который вы можете выполнить.При выполнении вызова генерируется один соответствующий веб-отклик.



