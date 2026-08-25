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

@HiltViewModel
class Hilt @Inject constructor(
    private val api: ApiService,
    private val dao: PostDao
) : ViewModel(){

    val allPosts: Flow<List<PostEntity>> = dao.getAllPosts()

    private val _postState = MutableStateFlow<RetrofitPost?>(null)
    val postState: StateFlow<RetrofitPost?> = _postState.asStateFlow()

     fun LoadPost(){
        viewModelScope.launch {
            try {
                val post = api.getSinglePost()
                _postState.value = post
            } catch (e: Exception) {
                // Handle error
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
                // Handle error
                Log.d("Retrofit", "Error fetching or saving post: ${e.message}")
            }
        }
    }

}


