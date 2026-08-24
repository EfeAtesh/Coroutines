package coroutines.learning
import retrofit2.http.GET


interface ApiService {
    @GET("posts/1")
    suspend fun getSinglePost(): RetrofitPost

}