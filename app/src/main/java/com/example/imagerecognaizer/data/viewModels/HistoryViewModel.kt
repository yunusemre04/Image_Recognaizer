package com.example.imagerecognaizer.data.viewModels

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log

import com.example.imagerecognaizer.data.history.HistoryItem

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagerecognaizer.data.dataStore.HistoryDataStore

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class HistoryViewModel (application: Application) : AndroidViewModel(application) {
    private val dataStore = HistoryDataStore(application.applicationContext)

    private val _historyList = MutableStateFlow<List<HistoryItem>>(emptyList())
    val historyList: StateFlow<List<HistoryItem>> = _historyList.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.historyFlow.collect {
                _historyList.value = it
            }
        }
    }


    fun addToHistory(bitmap: Bitmap, label: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val filename = "IMG_${System.currentTimeMillis()}.png"
                val file = File(getApplication<Application>().filesDir, filename)
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }

                val item = HistoryItem(imagePath = file.absolutePath, label = label)
                dataStore.addToHistory(item)
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Error saving image: ${e.message}")
            }
        }
    }
    fun removeFromHistory(context: Context, item: HistoryItem) {
        // Update the list (remove from UI first)
        val updatedList = _historyList.value.toMutableList().apply {
            remove(item)
        }
        _historyList.value = updatedList

        // Delete the file (optional)
        try {
            File(item.imagePath).delete()
        } catch (e: Exception) {
            Log.e("HistoryViewModel", "File could not be deleted: ${e.message}")
        }

        // Remove from DataStore
        viewModelScope.launch(Dispatchers.IO) {
            val dataStore = HistoryDataStore(context)
            dataStore.removeFromHistory(item)
        }
    }




}
