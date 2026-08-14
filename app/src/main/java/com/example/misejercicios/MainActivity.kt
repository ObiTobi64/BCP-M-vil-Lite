package com.example.misejercicios

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.misejercicios.data.AppPreferences
import com.example.misejercicios.ui.navigation.BcpNavGraph
import com.example.misejercicios.ui.theme.BcpBoliviaTheme
import java.util.Locale

class MainActivity : ComponentActivity() {

    // Aplica el idioma guardado por el usuario (por defecto espanol), sin importar el
    // idioma del sistema. Ver AppPreferences para el toggle expuesto en el HomeScreen.
    override fun attachBaseContext(newBase: Context) {
        val locale = Locale(AppPreferences.getLanguage(newBase))
        Locale.setDefault(locale)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemDarkTheme = isSystemInDarkTheme()
            var isDarkTheme by remember {
                mutableStateOf(AppPreferences.getDarkMode(this) ?: systemDarkTheme)
            }

            BcpBoliviaTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    BcpNavGraph(
                        navController = navController,
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = {
                            isDarkTheme = !isDarkTheme
                            AppPreferences.setDarkMode(this, isDarkTheme)
                        },
                        onToggleLanguage = {
                            val nextLanguage = if (AppPreferences.getLanguage(this) == AppPreferences.LANGUAGE_ES) {
                                AppPreferences.LANGUAGE_EN
                            } else {
                                AppPreferences.LANGUAGE_ES
                            }
                            AppPreferences.setLanguage(this, nextLanguage)
                            recreate()
                        }
                    )
                }
            }
        }
    }
}
