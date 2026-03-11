package com.minesec.msav3opensource

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.minesec.msav3opensource.ui.screens.providers
import com.theminesec.multiplatform.msa_core.acitivty.BaseMsaActivity
import com.theminesec.multiplatform.msa_core.app.navigation.App
import com.theminesec.multiplatform.msa_core.app.navigation.ScreenProviders
import com.theminesec.multiplatform.msa_core.util.nfc.provideNFCManager
import org.koin.core.module.Module
import org.koin.dsl.module

class MainActivity : BaseMsaActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        // Set up Compose UI
        setContent {
            App(providers = providers)
        }
    }

    override fun provideAdditionalKoinModules(): List<Module> {
        return listOf(
            provideNFCManager(),
            module {
                single<ScreenProviders> { providers }
            }
        )
    }


    @Preview
    @Composable
    fun AppAndroidPreview() {
        App(providers = providers)
    }
}