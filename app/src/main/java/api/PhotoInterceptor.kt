package api


import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


//Добавление перехватчика для вставки URL-констант

private const val API_KEY = "e56347be8eeb6062f06669edaa6603b6"
class PhotoInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()                 //chain.request() для доступа к исходному запросу
        val newUrl: HttpUrl = originalRequest.url().newBuilder()       /*Функция originalRequest.url() извлекает исходный URL из запроса, а затем
                                                                        используется HttpUrl.Builder для добавления параметров запроса*/
            .addQueryParameter("api_key", API_KEY)                //HttpUrl.Builder создает новый запрос на основе оригинального запроса изаменяет исходный URL на новый.
            .addQueryParameter("format", "json")
            .addQueryParameter("nojsoncallback", "1")
            .addQueryParameter("extras", "url_s")
            .addQueryParameter("safesearch", "1")
            .build()
        val newRequest: Request = originalRequest.newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(newRequest)                                //функция chain.continue(newRequest) для создания ответа
    }
}