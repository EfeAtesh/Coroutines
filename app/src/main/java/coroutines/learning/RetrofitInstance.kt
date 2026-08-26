package coroutines.learning

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//creates standalone retrofit instance
//calling retrofit directly initiates tight coupling
//which makes it harder to observe and test
//replaced by hilt appmodule in modern architecture
/*
Etiket              Nereye Konur?            Ne İşe Yarar?
Retrofit.Builder    İstemci nesnesine        Base URL ve JSON dönüştürücüyü bağlayarak Retrofit'i kurar.
create(Api::class)  Retrofit motoruna        Interface'i çalışır bir HTTP istemcisine dönüştürür.
*/

//object ki api çağrışı kolay olsun

object RetrofitInstance {
    private const val URL = "https://jsonplaceholder.typicode.com/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}