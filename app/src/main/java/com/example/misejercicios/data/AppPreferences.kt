package com.example.misejercicios.data

import android.content.Context

/**
 * Preferencias simples de la app (tema e idioma), persistidas en SharedPreferences para
 * que sobrevivan a un reinicio de la Activity (necesario al cambiar de idioma).
 */
object AppPreferences {
    private const val PREFS_NAME = "bcp_prefs"
    private const val KEY_LANGUAGE = "language"
    private const val KEY_DARK_MODE = "dark_mode"

    const val LANGUAGE_ES = "es"
    const val LANGUAGE_EN = "en"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getLanguage(context: Context): String =
        prefs(context).getString(KEY_LANGUAGE, LANGUAGE_ES) ?: LANGUAGE_ES

    fun setLanguage(context: Context, languageCode: String) {
        prefs(context).edit().putString(KEY_LANGUAGE, languageCode).apply()
    }

    /** null = todavia no se eligio manualmente, usar el tema del sistema. */
    fun getDarkMode(context: Context): Boolean? =
        when (prefs(context).getInt(KEY_DARK_MODE, -1)) {
            0 -> false
            1 -> true
            else -> null
        }

    fun setDarkMode(context: Context, isDark: Boolean) {
        prefs(context).edit().putInt(KEY_DARK_MODE, if (isDark) 1 else 0).apply()
    }
}
