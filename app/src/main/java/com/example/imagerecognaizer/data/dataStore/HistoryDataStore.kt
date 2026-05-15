package com.example.imagerecognaizer.data.dataStore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.imagerecognaizer.data.history.HistoryItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



class HistoryDataStore(private val context: Context) {

    private val gson = Gson()
    private val HISTORY_KEY = stringPreferencesKey("history_items")

    val historyFlow: Flow<List<HistoryItem>> = context.dataStore.data
        .map { prefs ->
            val json = prefs[HISTORY_KEY] ?: "[]"
            val type = object : TypeToken<List<HistoryItem>>() {}.type
            gson.fromJson(json, type)
        }

    suspend fun addToHistory(item: HistoryItem) {
        context.dataStore.edit { prefs ->
            val currentList = getHistoryList(prefs)
            val newList = currentList + item
            prefs[HISTORY_KEY] = gson.toJson(newList)
        }
    }

    suspend fun removeFromHistory(item: HistoryItem) {
        context.dataStore.edit { prefs ->
            val currentList = getHistoryList(prefs)
            val newList = currentList.filterNot {
                it.imagePath == item.imagePath && it.label == item.label
            }
            prefs[HISTORY_KEY] = gson.toJson(newList)
        }
    }

    private fun getHistoryList(prefs: Preferences): List<HistoryItem> {
        val json = prefs[HISTORY_KEY] ?: "[]"
        val type = object : TypeToken<List<HistoryItem>>() {}.type
        return gson.fromJson(json, type)
    }
}
