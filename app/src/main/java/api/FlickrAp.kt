package api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/*Аннотация @GET("/") в приведенном ниже коде настраивает Call на выполнение GET-запроса.
метод flickr.interestingness.getList.«возвращает список интересных фотографий за последний день или указанную пользователем дату»
 */

    interface FlickrApi {
        /* @GET(
             "serves/rest/?method=flickr.interestingness.getList" +
                     "&api_key=e56347be8eeb6062f06669edaa6603b6" +
                     "&format=json" +
                     "&nojsoncallback=1" +                               // инструктирует Flickr убрать из ответа круглые скобки
                     "&extras=url_s"                                     //добавить URL-адрес мини-версии изображения, если таковая есть
         )*/
         @GET("services/rest?method=flickr.interestingness.getList")
         fun fetchPhotos(): Call<FlickrResponse>                       //FlickrResponse для десериализации JSON-данных в ответе
         @GET
         fun fetchUrlBytes(@Url url: String): Call<ResponseBody>       //URL-адрес,который используется для определения того, откуда загружать данные
                                                                   // Объект Call представляет собой один веб-запрос, который вы можете выполнить.
                                                                      // При выполнении вызова генерируется один соответствующий веб-отклик.

      //функция поиска
        @GET("services/rest?method=flickr.photos.search")
        fun searchPhotos(@Query("text") query: String): Call<FlickrResponse>   //@Query позволяет динамически добавлять к URL параметры запроса.

    }
