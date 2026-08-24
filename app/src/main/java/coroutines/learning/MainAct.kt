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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map


import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel

import kotlinx.coroutines.flow.flowOn

val Context.dataStore by preferencesDataStore(name = "user_prefs")

// MainActivity but names as MainAct


@AndroidEntryPoint
class MainAct : ComponentActivity() {
    // This is the main activity of the application.

    val USER_NAME_KEY = stringPreferencesKey("user_name")


    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            MainScreen()
            }
    }


    //coroutine
    suspend fun fetchData() : String {
        //Here it uses IO Chip

        return withContext(kotlinx.coroutines.Dispatchers.IO) {
            delay(2000) // Simulate a network call or long-running operation
            "3000"
        }
    }

    fun trialo(){
        Log.i("e","Trialo")
    }

    @Composable
    fun MainScreen(viewModel: Hilt = hiltViewModel()) {
        val post by viewModel.postState.collectAsState()
        var data by remember { mutableStateOf<String?>(null) }
        val scope = rememberCoroutineScope()
        var placeholder by remember { mutableStateOf<String?>("Placeholder") }

        Column(modifier = Modifier.padding(16.dp)) {
            Button(onClick = {
                scope.launch {
                    val user = async { fetchUser()}
                    val data = async{ fetchData()}
                    val age = async { fetchAge() }
                    placeholder = "User: ${user.await()}, Data: ${data.await()}, Age: ${age.await()}"
                    saveName("" + user)
                }
            }) {
            }


            Button(
                onClick = { scope.launch {

                    try {
                        val post = RetrofitInstance.api.getSinglePost()
                        placeholder = "Başlık: ${post.title}\n\nİçerik: ${post.body}"
                    }
                    catch (e: Exception){
                        placeholder = "Hata: ${e.message}"
                    }

                }}
            ) {

                Text("" + placeholder)


            }


            // Hilt Butonu:
            Button(
                onClick = {
                    {viewModel.LoadPost()}
                }
            ) {
                Text(text = "Hilt ile Post Çek")
            }

            // Gelen sonucu butonun altında gösteriyoruz:
            post?.let {
                Text(
                    text = "Başlık: ${it.title}\n\nİçerik: ${it.body}",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

        }
    }

    suspend fun fetchUser() : String{

        return withContext(kotlinx.coroutines.Dispatchers.IO) {
            delay(1000) // Simulate a network call or long-running operation
            "Dwayne"
        }
    }

    suspend fun fetchAge() : Int{
        return withContext(kotlinx.coroutines.Dispatchers.IO) {
            delay(1000) // Simulate a network call or long-running operation
            8000
        }
    }

    //flow fonksyionlarında hep emit kullanır
    fun countDownFlow() : Flow<Int> = kotlinx.coroutines.flow.flow {
        for (i in 10 downTo 1) {
            emit(i)
            delay(1000)
        }
    }.flowOn(kotlinx.coroutines.Dispatchers.Default)

    suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    val userNameFlow: Flow<String>
        get() = dataStore.data.map { preferences ->
            preferences[USER_NAME_KEY] ?: "Unknown"

        }

    @Preview(showBackground = true)
    @Composable
    fun flowscreen(){
        val timerValue by countDownFlow().collectAsState(initial = 10)
        Column() {        Text(text = "Countdown: $timerValue")
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MainScreenPreview() {
        MainScreen()
        flowscreen()
    }


}


