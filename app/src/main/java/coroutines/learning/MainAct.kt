package coroutines.learning

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val Context.dataStore by preferencesDataStore(name = "user_prefs")

@AndroidEntryPoint
class MainAct : ComponentActivity() {

    val USER_NAME_KEY = stringPreferencesKey("user_name")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }

    suspend fun fetchData(): String {
        return withContext(Dispatchers.IO) {
            delay(2000)
            "3000"
        }
    }

    fun trialo() {
        Log.i("e", "Trialo")
    }

    suspend fun fetchUser(): String {
        return withContext(Dispatchers.IO) {
            delay(1000)
            "Dwayne"
        }
    }

    suspend fun fetchAge(): Int {
        return withContext(Dispatchers.IO) {
            delay(1000)
            8000
        }
    }

    fun countDownFlow(): Flow<Int> = flow {
        for (i in 10 downTo 1) {
            emit(i)
            delay(1000)
        }
    }.flowOn(Dispatchers.Default)

    suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    val userNameFlow: Flow<String>
        get() = dataStore.data.map { preferences ->
            preferences[USER_NAME_KEY] ?: "Unknown"
        }

    // 1. Stateful Ekran (Hilt ve ViewModel ile konuşur)
    @Composable
    fun MainScreen(viewModel: Hilt = hiltViewModel()) {
        val postList by viewModel.allPosts.collectAsState(initial = emptyList())
        val post by viewModel.postState.collectAsState()

        MainScreenContent(
            postList = postList,
            post = post,
            onHiltClick = { viewModel.LoadPost() },
            onRoomClick = { viewModel.fetchAndSavePost() },
            onAsyncClick = {
                fetchUser()
                fetchData()
                fetchAge()
            }
        )
    }

    // 2. Stateless Ekran (Sadece arayüzü çizer, ViewModel bağımsızdır)
    @Composable
    fun MainScreenContent(
        postList: List<PostEntity>,
        post: RetrofitPost?,
        onHiltClick: () -> Unit,
        onRoomClick: () -> Unit,
        onAsyncClick: suspend () -> Unit
    ) {
        val scope = rememberCoroutineScope()
        var placeholder by remember { mutableStateOf("Placeholder") }

        Column(modifier = Modifier.padding(16.dp)) {

            Button(onClick = {
                scope.launch {
                    val user = async { fetchUser() }
                    val data = async { fetchData() }
                    val age = async { fetchAge() }
                    placeholder = "User: ${user.await()}, Data: ${data.await()}, Age: ${age.await()}"
                    saveName(user.await())
                }
            }) {
                Text(text = "Async / DataStore: $placeholder")
            }

            Button(
                modifier = Modifier.padding(top = 8.dp),
                onClick = {
                    scope.launch {
                        try {
                            val networkPost = RetrofitInstance.api.getSinglePost()
                            placeholder = "Başlık: ${networkPost.title}\n\nİçerik: ${networkPost.body}"
                        } catch (e: Exception) {
                            placeholder = "Hata: ${e.message}"
                        }
                    }
                }
            ) {
                Text("Retrofit Tek Başına Çek")
            }

            // Hilt Butonu
            Button(
                modifier = Modifier.padding(top = 8.dp),
                onClick = onHiltClick
            ) {
                Text(text = "Hilt ile Post Çek")
            }

            post?.let {
                Text(
                    text = "Hilt Gelen: ${it.title}",
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Room / Offline-First Butonu
            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = onRoomClick
            ) {
                Text(text = "DAO Çek ve Room'a Kaydet (Offline-First)")
            }

            // Room Kayıtları
            postList.forEach { p ->
                Text(
                    text = "Room'dan (#${p.id}): ${p.title}\n${p.body}",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun flowscreen() {
        val timerValue by countDownFlow().collectAsState(initial = 10)
        Column {
            Text(text = "Countdown: $timerValue")
        }
    }

    // 3. Preview (Sahte veriyle çökmeden çizilir)
    @Preview(showBackground = true)
    @Composable
    fun MainScreenPreview() {
        val samplePosts = listOf(
            PostEntity(id = 1, userId = 1, title = "Örnek Room Başlığı", body = "Örnek Room İçeriği")
        )
        val samplePost = RetrofitPost(id = 1, userId = 1, title = "Örnek Hilt Başlığı", body = "Örnek İçerik")

        Column {
            MainScreenContent(
                postList = samplePosts,
                post = samplePost,
                onHiltClick = {},
                onRoomClick = {},
                onAsyncClick = {}
            )
            flowscreen()
        }
    }
}
