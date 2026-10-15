package com.gymguide.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class HomeUiState(
    val loading: Boolean = true,
    val quote: String? = null,
    val author: String? = null,
    val error: String? = null
)

private data class QuoteDto(
    @SerializedName("q") val quote: String?,
    @SerializedName("a") val author: String?
)

private interface QuoteApi {
    @GET("api/random/")
    suspend fun getRandomQuote(): List<QuoteDto>
}

private object QuoteService {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .callTimeout(12, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    val api: QuoteApi = Retrofit.Builder()
        .baseUrl("https://zenquotes.io/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(QuoteApi::class.java)
}

class HomeViewModel : ViewModel() {
    private companion object {
        const val TAG = "GymGuideHttp"
    }

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init { cargarFrase() }

    fun cargarFrase() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(loading = true)
            try {
                Log.i(TAG, "Solicitando frase a ZenQuotes")
                val quote = QuoteService.api.getRandomQuote().firstOrNull()
                    ?: throw IllegalStateException("El servicio no devolvió una frase.")
                if (quote.quote.isNullOrBlank()) throw IllegalStateException("La frase recibida está vacía.")
                Log.i(TAG, "Frase recibida correctamente; autor=${quote.author}")
                _uiState.value = HomeUiState(
                    loading = false,
                    quote = quote.quote,
                    author = quote.author
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Log.e(TAG, "Falló la petición de frase", exception)
                _uiState.value = HomeUiState(
                    loading = false,
                    error = "No se pudo conectar con el servicio. Revisa tu conexión e inténtalo de nuevo."
                )
            }
        }
    }
}
