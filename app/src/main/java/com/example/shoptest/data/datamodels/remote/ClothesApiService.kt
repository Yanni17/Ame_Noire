import com.example.shoptest.data.datamodels.models.Clothes
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// Die Konstante enthält die URL der API
const val BASE_URL = "https://fakestoreapi.com/"


// Moshi konvertiert Serverantworten in Kotlin Objekte
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Retrofit übernimmt die Kommunikation und übersetzt die Antwort
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ClothesApiService {

    @GET("products")
    suspend fun getAllProducts(): List<Clothes>

}

object ClothesApi {
    val retrofitService: ClothesApiService by lazy { retrofit.create(ClothesApiService::class.java)}
}