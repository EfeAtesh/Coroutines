package coroutines.learning

//network data model representing api response
//automatically parsed from json to kotlin data class by gson
/*
Etiket              Nereye Konur?            Ne İşe Yarar?
data class          Sınıf başına             Veri tutucu model olduğunu belirtir (equals, copy vb. üretir).
*/

data class RetrofitPost(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)
