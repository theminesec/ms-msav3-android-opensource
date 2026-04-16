plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
}

android {
    namespace = "com.minesec.msav3opensource"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.minesec.msav3opensource"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes += setOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/license.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/notice.txt",
                "META-INF/ASL2.0",
                "META-INF/*.kotlin_module"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    flavorDimensions += "client"

    productFlavors {
        create("clientA") {
            dimension = "client"
            applicationIdSuffix = ".clienta"
            versionNameSuffix = "-clientA"
        }
        create("clientB") {
            dimension = "client"
            applicationIdSuffix = ".clientb"
            versionNameSuffix = "-clientB"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.headless.mpoc)
    debugImplementation(libs.msa.core.stage) {
        exclude(group = "MultiplatformMSA", module = "msa-model")
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }
    releaseImplementation(libs.msa.core.prod) {
        exclude(group = "MultiplatformMSA", module = "msa-model")
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }
    implementation(libs.visa.sensory)
    implementation(libs.mc.sensory)

    implementation("io.insert-koin:koin-android:3.5.3")
    implementation("io.insert-koin:koin-androidx-compose:3.5.3")
    implementation("io.github.ismai117:kottie:2.1.0")
    implementation("network.chaintech:compose-multiplatform-screen-capture:1.0.1")
    val lottieVersion = "6.0.0"
    implementation("com.airbnb.android:lottie-compose:$lottieVersion")

    implementation("io.coil-kt.coil3:coil-compose:3.3.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.3.0")

    // For QR codes
    implementation("io.github.alexzhirkevich:qrose:1.0.1")
    implementation("io.github.ismai117:kottie:2.1.0")
    implementation(libs.easyqrscan)
    implementation("androidx.activity:activity-compose:1.11.0") // includes BackHandler
    implementation("io.github.suwasto:kmp-capturable-compose:0.1.1")
}