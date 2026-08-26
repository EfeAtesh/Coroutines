package coroutines.learning

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//root application class for the android app
//entry point for hilt dependency injection container
/*
Etiket              Nereye Konur?            Ne İşe Yarar?
@HiltAndroidApp     Application sınıfına     Hilt'in tüm uygulamadaki ana motorunu ve kod üretimini çalıştırır.
*/

@HiltAndroidApp
class MyApplication : Application()