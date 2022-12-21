package api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

    interface FlickrApi {

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
