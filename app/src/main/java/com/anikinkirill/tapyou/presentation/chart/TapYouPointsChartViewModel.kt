package com.anikinkirill.tapyou.presentation.chart

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.viewModelScope
import com.anikinkirill.tapyou.R
import com.anikinkirill.tapyou.domain.Resource.Companion.onError
import com.anikinkirill.tapyou.domain.Resource.Companion.onSuccess
import com.anikinkirill.tapyou.domain.Text
import com.anikinkirill.tapyou.domain.model.TapYouPoints
import com.anikinkirill.tapyou.domain.usecase.GetTapYouPointsUseCase
import com.anikinkirill.tapyou.presentation.chart.TapYouPointsChartViewModel.Action
import com.anikinkirill.tapyou.presentation.chart.TapYouPointsChartViewModel.State
import com.anikinkirill.tapyou.presentation.utils.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class TapYouPointsChartViewModel(
    private val useCase: GetTapYouPointsUseCase,
) : BaseViewModel<State, Action>(State()) {

    fun loadPoints(amount: Int) {
        updateState {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            useCase.execute(amount)
                .onSuccess { result ->
                    updateState {
                        it.copy(
                            points = result,
                            isLoading = false,
                            errorState = null,
                        )
                    }
                }
                .onError { error ->
                    updateState {
                        it.copy(
                            points = emptyList(),
                            isLoading = false,
                            errorState = ErrorState(error)
                        )
                    }
                }
        }
    }

    fun saveImageToLocalStorage(bitmap: Bitmap, context: Context) {
        var outputStream: OutputStream?
        val filename = "tap_you_image_${System.currentTimeMillis()}.png"
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val contentValues = ContentValues().apply {
                            put(
                                MediaStore.MediaColumns.DISPLAY_NAME,
                                filename
                            )
                            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                        }
                        val uri = context.contentResolver.insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            contentValues
                        )
                        outputStream = uri?.let { context.contentResolver.openOutputStream(it) }
                    } else {
                        val imagesDir = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES
                        )
                        val image = File(imagesDir, filename)
                        outputStream = FileOutputStream(image)
                    }
                } catch (exception: Exception) {
                    outputStream = null
                }
                outputStream?.let { os ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
                    postSideEffect(Action.ShowToast(Text.res(R.string.image_is_successfully_saved)))
                } ?: kotlin.run {
                    postSideEffect(Action.ShowToast(Text.res(R.string.image_is_not_saved)))
                }
            }
        }
    }

    data class State(
        val points: List<TapYouPoints> = emptyList(),
        val isLoading: Boolean = false,
        val errorState: ErrorState? = null,
    )

    data class ErrorState(
        val errorText: Text,
    )

    sealed class Action {
        data class ShowToast(val message: Text) : Action()
    }
}