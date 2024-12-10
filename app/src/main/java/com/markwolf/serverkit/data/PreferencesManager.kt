package com.markwolf.serverkit.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
val ROOT_PATH_KEY = stringPreferencesKey("rootPath")


object PreferencesManager {
    suspend fun setRootPath(context: Context, path: String) {
        context.dataStore.edit { preferences ->
            preferences[ROOT_PATH_KEY] = path
        }
    }

    fun getRootPath(context: Context): Flow<String> {
        return context.dataStore.data.map { it[ROOT_PATH_KEY] ?: "" }
    }
}