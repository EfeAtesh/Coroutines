package coroutines.learning

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//provides retrofit and room instances to hilt
//solves tight coupling across the entire app
//allows swapping real dependencies with mocks during testing
/*
Etiket              Nereye Konur?            Ne İşe Yarar?
@Module             Object / Class'a         Hilt için bağımlılık sağlayıcı bir modül olduğunu belirtir.
@InstallIn          Modül sınıfına           Modülün yaşam döngüsünü (SingletonComponent = App boyu) belirler.
@Provides           Fonksiyona               Retrofit ve Room gibi nesnelerin nasıl üretileceğini Hilt'e öğretir.
@Singleton          @Provides üstüne         Uygulama boyunca tek bir nesne üretilip paylaşılmasını sağlar.
*/

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "posts_database"
        ).build()
    }

    @Provides
    fun providePostDao(database: AppDatabase): PostDao {
        return database.postDao()
    }
}