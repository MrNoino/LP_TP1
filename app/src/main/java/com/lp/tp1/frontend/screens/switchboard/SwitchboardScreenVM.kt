package com.lp.tp1.frontend.screens.switchboard

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.lp.tp1.backend.requests.SwitchboardResponse
import com.lp.tp1.backend.requests.getRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SwitchboardScreenVM(private val application: Application) : AndroidViewModel(application) {

    data class UiState(
        val requestedLoading: Boolean = false,
        val isLoading: Boolean = true,
        val responseText: String = "",
        val blueprint: Bitmap? = null,
        val firstSwitchChecked: Boolean = true,
        val secondSwitchChecked: Boolean = true,
        val thirdSwitchChecked: Boolean = true,
        val forthSwitchChecked: Boolean = true,
        val fifthSwitchChecked: Boolean = true,
        val focusImage: Boolean = false,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private lateinit var switchboardResponse: SwitchboardResponse
    private lateinit var blueprintBitmap: Bitmap

    fun load(link: String) {

        _uiState.update { uiState.value.copy(requestedLoading = true) }

        getRetrofit(link)
            .getSwitchboard()
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {

                    switchboardResponse =
                        Gson().fromJson(response.body(), SwitchboardResponse::class.java)

                    _uiState.update {
                        uiState.value.copy(
                            isLoading = false,
                            responseText = response.body() ?: ""
                        )
                    }

                    getRetrofit("https://lp-tp1-api.vercel.app/blueprints/${switchboardResponse.blueprintFilename}/")
                        .getBlueprint()
                        .enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {

                                val blueprintBytes = response.body()!!.bytes()
                                blueprintBitmap = BitmapFactory.decodeByteArray(
                                    blueprintBytes,
                                    0,
                                    blueprintBytes.size
                                )

                                _uiState.update { uiState.value.copy(blueprint = blueprintBitmap) }

                                drawRoomBorders()
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Log.e("API Error", t.message.toString())
                            }
                        })

                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("API Error", t.message.toString())
                }
            })
    }

    fun isNetworkAvailable(): Boolean {

        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities != null) {

            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) return true
            else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) return true
            else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) return true
        }
        return false
    }

    fun updateFocusImage(v: Boolean) {
        _uiState.update { uiState.value.copy(focusImage = v) }
    }

    fun toggleFirstSwitch(v: Boolean) {
        _uiState.update { uiState.value.copy(firstSwitchChecked = v) }
        drawRoomBorders()
    }

    fun toggleSecondSwitch(v: Boolean) {
        _uiState.update { uiState.value.copy(secondSwitchChecked = v) }
        drawRoomBorders()
    }

    fun toggleThirdSwitch(v: Boolean) {
        _uiState.update { uiState.value.copy(thirdSwitchChecked = v) }
        drawRoomBorders()
    }

    fun toggleForthSwitch(v: Boolean) {
        _uiState.update { uiState.value.copy(forthSwitchChecked = v) }
        drawRoomBorders()
    }

    fun toggleFifthSwitch(v: Boolean) {
        _uiState.update { uiState.value.copy(fifthSwitchChecked = v) }
        drawRoomBorders()
    }


    fun drawRoomBorders() {

        viewModelScope.launch(Dispatchers.Main) {
            val mutableBlueprint = blueprintBitmap.copy(Bitmap.Config.ARGB_8888, true)

            switchboardResponse.switches.forEach { switch ->

                val active = when (switch.position) {
                    1 -> uiState.value.firstSwitchChecked
                    2 -> uiState.value.secondSwitchChecked
                    3 -> uiState.value.thirdSwitchChecked
                    4 -> uiState.value.forthSwitchChecked
                    5 -> uiState.value.fifthSwitchChecked
                    else -> false
                }

                if (active) {
                    val color = Color.argb(0xFF, 255, 0, 0)

                    for (leftBorderY in switch.blueprintRoomPosition.topLeft.y..switch.blueprintRoomPosition.bottomLeft.y) {
                        mutableBlueprint.setPixel(
                            switch.blueprintRoomPosition.topLeft.x,
                            leftBorderY,
                            color
                        )
                    }

                    for (rightBorderY in switch.blueprintRoomPosition.topRight.y..switch.blueprintRoomPosition.bottomRight.y) {
                        mutableBlueprint.setPixel(
                            switch.blueprintRoomPosition.topRight.x,
                            rightBorderY,
                            color
                        )
                    }

                    for (topBorderX in switch.blueprintRoomPosition.topLeft.x..switch.blueprintRoomPosition.topRight.x) {
                        mutableBlueprint.setPixel(
                            topBorderX,
                            switch.blueprintRoomPosition.topLeft.y,
                            color
                        )
                    }

                    for (bottomBorderX in switch.blueprintRoomPosition.bottomLeft.x..switch.blueprintRoomPosition.bottomRight.x) {
                        mutableBlueprint.setPixel(
                            bottomBorderX,
                            switch.blueprintRoomPosition.bottomLeft.y,
                            color
                        )
                    }
                }
            }

            _uiState.update { uiState.value.copy(blueprint = mutableBlueprint) }
        }
    }
}