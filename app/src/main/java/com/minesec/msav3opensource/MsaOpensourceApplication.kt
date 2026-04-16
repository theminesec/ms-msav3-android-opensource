package com.minesec.msav3opensource

import android.app.Application
import com.minesec.msav3opensource.ui.screens.providers
import com.theminesec.multiplatform.msa_core.app.config.EnvironmentConfig
import com.theminesec.multiplatform.msa_core.app.config.MsaEnvironment
import com.theminesec.multiplatform.msa_core.app.navigation.ScreenProviders
import com.theminesec.multiplatform.msa_core.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.dsl.module

class MsaOpensourceApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        EnvironmentConfig.initialize(
            EnvironmentConfig(
                environment = MsaEnvironment.Staging,
                licenseFileName = "lic_01KMZVPHT922W4YHZZEZH7A8HN-20260330_171211.license",
            )
        )
        initKoin {
            androidLogger()
            androidContext(this@MsaOpensourceApplication)
            modules(
                module {
                    single<ScreenProviders> { providers }
                }
            )
        }
    }
}
