// File: app/src/main/java/com/example/plantidentifier/viewmodels/HomeViewModel.kt

package com.example.plantidentifier.viewmodels

import com.example.plantidentifier.data.models.GeminiRequest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantidentifier.data.models.*
import com.example.plantidentifier.data.services.GeminiApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val geminiApiService: GeminiApiService,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var selectedImageUri: Uri? = null

    fun setSelectedImage(uri: Uri) {
        selectedImageUri = uri
    }

    fun identifyPlant() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading

                val imageBase64 = selectedImageUri?.let { uriToBase64(it) }
                    ?: throw IllegalStateException("No image selected")

                val request = GeminiRequest(
                    contents = listOf(
                        Content(
                            parts = listOf(
                                Part(text = "Identify this plant:"),
                                Part(
                                    inlineData = InlineData(
                                        mimeType = "image/jpeg",
                                        data = imageBase64
                                    )
                                )
                            )
                        )
                    )
                )

                //version 5
                val response = geminiApiService.identifyPlant(request)
                print("DEBUG-The GeminiServiceAPIresponse:${response}")
                _uiState.value = UiState.Success(response?.candidates?.first()?.content?.parts?.first()?.text)
                //Fix Version 5
                /*try {
                    val response = geminiApiService.identifyPlant(request)
                    print("DEBUG-The GeminiServiceAPIresponse:${response}")
                    _uiState.value = UiState.Success(response)
                    onResult(response.name)
                } catch (e: Exception) {
                    // Handle error
                    onResult("Unable to identify plant")
                }*/

            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    private fun uriToBase64(uri: Uri): String {
        val bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    sealed class UiState {
        object Initial : UiState()
        object Loading : UiState()
        //data class Success(val plantInfo: PlantInfo) : UiState()
        data class Success(val plantInfo: String?) : UiState()
        data class Error(val message: String) : UiState()
    }
}
