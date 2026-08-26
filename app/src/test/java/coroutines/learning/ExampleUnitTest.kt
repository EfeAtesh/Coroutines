package coroutines.learning

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.whenever

//unit tests using junit and mockito
//uses mock objects to test without real network requests
//follows arrange act assert testing pattern
/*
Etiket              Nereye Konur?            Ne İşe Yarar?
@Test               Fonksiyona               JUnit test metodu olduğunu belirtir.
runTest             Test bloğuna             Coroutine ve suspend fonksiyonları zaman atlamalı hızlı test eder.
whenever()          Mock nesneye             Sahte nesnenin çağrıldığında döneceği davranışı belirler.
*/

class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testGetSinglePost() = runTest {
        // 1. Arrange
        val mockApiService = Mockito.mock(ApiService::class.java)
        val expectedPost = RetrofitPost(
            userId = 1,
            id = 1,
            title = "Test Title",
            body = "Test Body"
        )
        whenever(mockApiService.getSinglePost()).thenReturn(expectedPost)

        // 2. Act
        val actualPost = mockApiService.getSinglePost()

        // 3. Assert
        assertEquals(expectedPost, actualPost)
    }
}