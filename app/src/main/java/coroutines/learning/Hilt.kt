package coroutines.learning

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//uses flow from coroutine for instant updates
//uses hilt which is dependency injection
//calling retrofit initiates tight coupling
//which makes it harder to observe and test
//however with hilt dependencies are injected automatically
/*
Etiket              Nereye Konur?            Ne İşe Yarar?
@HiltViewModel      ViewModel sınıfına       ViewModel'e otomatik nesne enjekte edilmesini sağlar.
@Inject             Constructor'a            Hilt'in ApiService ve PostDao nesnelerini otomatik vermesini sağlar.
viewModelScope      ViewModel içine          ViewModel kapanınca arkaplan coroutine'lerini otomatik iptal eder.
StateFlow           Değişkene                Arayüze (Compose) güncel durumu canlı ve reaktif olarak iletir.
*/

@HiltViewModel
class Hilt @Inject constructor(
    private val api: ApiService,
    private val dao: PostDao
) : ViewModel() {

    val allPosts: Flow<List<PostEntity>> = dao.getAllPosts()

    private val _postState = MutableStateFlow<RetrofitPost?>(null)
    val postState: StateFlow<RetrofitPost?> = _postState.asStateFlow()

    fun LoadPost() {
        viewModelScope.launch {
            try {
                val post = api.getSinglePost()
                _postState.value = post
            } catch (e: Exception) {
                _postState.value = null
                Log.d("Retrofit", "Error fetching post: ${e.message}")
            }
        }
    }

    fun fetchAndSavePost() {
        viewModelScope.launch {
            try {
                val post = api.getSinglePost()
                val postEntity = PostEntity(
                    id = post.id,
                    userId = post.userId,
                    title = post.title,
                    body = post.body
                )
                dao.insertPosts(listOf(postEntity))
            } catch (e: Exception) {
                Log.d("Retrofit", "Error fetching or saving post: ${e.message}")
            }
        }
    }
}
