package com.lp.tp1.frontend.screens.switchboard

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.lp.tp1.backend.requests.SwitchboardResponse
import com.lp.tp1.backend.requests.getSwitchboardRetrofit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SwitchboardScreenVM(private val application: Application) : AndroidViewModel(application) {

    data class UiState(
        val requestedLoading: Boolean = false,
        val isLoading: Boolean = true,
        val responseText: String = "",
        val imageUrl: String = ""
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun load(link: String) {

        _uiState.update { uiState.value.copy(requestedLoading = true) }

        getSwitchboardRetrofit(link)
            .getSwitchboard()
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {

                    try {

                        val data = Gson().fromJson(response.body(), SwitchboardResponse::class.java)

                        _uiState.update {
                            uiState.value.copy(
                                isLoading = false,
                                responseText = response.body() ?: "",
                                imageUrl = "https://lp-tp1-api.vercel.app/blueprints/${data.blueprintFilename}/"
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("Json Error", e.message.toString())
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("API Error", t.message.toString())
                }
            })
    }

    fun isNetworkAvailable(): Boolean {

        val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities != null) {

            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) return true
            else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) return true
            else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) return true
        }
        return false
    }

}