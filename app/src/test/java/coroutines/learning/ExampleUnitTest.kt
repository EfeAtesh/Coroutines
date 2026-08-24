package coroutines.learning

import org.junit.Test

import org.junit.Assert.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.mockito.Mockito
import org.mockito.kotlin.whenever


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    val mockApiService = Mockito.mock(ApiService::class.java)
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun weirdsum(){
        val a = 458
        val b = 971563.3f

        assertEquals(971563.3f, a + b)

    }
    val expectedPost = RetrofitPost(
        userId = 1,
        id = 1,
        title = "Test Title",
        body = "Test Body"
    )

    @Test
    fun getSinglePost() = runTest {
        whenever(mockApiService.getSinglePost()).thenReturn(expectedPost)

    }



}