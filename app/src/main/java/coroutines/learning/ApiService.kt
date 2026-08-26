package coroutines.learning

import retrofit2.http.GET

//defines rest api endpoints using retrofit
//uses suspend functions for non-blocking network calls
//automatically parsed to kotlin objects by gson converter
/*
Etiket              Nereye Konur?            Ne İşe Yarar?
@GET("endpoint")    Fonksiyon üstüne         Belirtilen sunucu adresine HTTP GET isteği atar.
suspend             Fonksiyon başına         Ağ çağrısını arkaplanda UI'ı dondurmadan çalıştırır.
*/

interface ApiService {
    @GET("posts/1")
    suspend fun getSinglePost(): RetrofitPost
}