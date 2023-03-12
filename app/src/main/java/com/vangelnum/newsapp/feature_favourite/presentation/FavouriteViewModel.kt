package com.vangelnum.newsapp.feature_favourite.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vangelnum.newsapp.core.common.Resource
import com.vangelnum.newsapp.feature_favourite.data.model.FavouriteData
import com.vangelnum.newsapp.feature_favourite.domain.repository.FavouriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val repository: FavouriteRepository
) : ViewModel() {
//    var readAllData: LiveData<List<FavouriteData>> = repository.getAll()

    private val _readAllData = MutableStateFlow<Resource<List<FavouriteData>>>(Resource.Loading())
    val readAllData = _readAllData.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAll().collect { res->
                _readAllData.value = res
            }
        }
    }

    fun addNewsDataBase(news: FavouriteData) {
        viewModelScope.launch {
            repository.addNews(news)
        }
    }

    fun deleteNewsDataBase(news: FavouriteData) {
        viewModelScope.launch {
            repository.deleteNews(news)
        }
    }


    fun share(context: Context, urlPhoto: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, urlPhoto)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    @Suppress("DEPRECATION")
    fun vibrate(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(70)
        }
    }
    fun goToBrowser(url: String, context: Context) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

}