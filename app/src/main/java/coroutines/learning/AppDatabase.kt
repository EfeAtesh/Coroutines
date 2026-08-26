package coroutines.learning

import androidx.room.Database
import androidx.room.RoomDatabase

//main room database holder and sqlite engine
//exposes daos for database operations
/*
Etiket              Nereye Konur?            Ne İşe Yarar?
@Database           Abstract class başına    Tabloları ve sürümü belirterek ana veritabanını oluşturur.
RoomDatabase        Kalıtım olarak           Room'un SQLite yönetim motorunu sınıfa bağlar.
*/

@Database(entities = [PostEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}
