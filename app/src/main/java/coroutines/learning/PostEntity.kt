package coroutines.learning

import androidx.room.Entity
import androidx.room.PrimaryKey

//defines room database table schema
//represents a single row in the sqlite table
/*
Etiket              Nereye Konur?            Ne İşe Yarar?
@Entity             Data class başına        SQLite içinde bir tablo (posts_table) oluşturur.
@PrimaryKey         Değişkene                Kaydın benzersiz kimlik anahtarı olduğunu belirtir.
*/

@Entity(tableName = "posts_table")
data class PostEntity(
    @PrimaryKey
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)
