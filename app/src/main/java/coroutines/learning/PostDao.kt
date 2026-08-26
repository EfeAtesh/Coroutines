package coroutines.learning

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

//data access object for room database operations
//returns flow for real-time reactive database updates
//uses suspend for async insert and delete operations
/*
Etiket              Nereye Konur?            Ne İşe Yarar?
@Dao                Interface başına         Room SQL sorgularını içeren arayüz olduğunu belirtir.
@Query("SQL")       Fonksiyona               Özel SQL sorgusu çalıştırır (Flow dönerse canlı dinler).
@Insert             Fonksiyona               Veritabanına yeni kayıt ekler (çakışmada REPLACE yapar).
*/

@Dao
interface PostDao {

    @Query("SELECT * FROM posts_table")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Query("DELETE FROM posts_table")
    suspend fun clearAll()
}
