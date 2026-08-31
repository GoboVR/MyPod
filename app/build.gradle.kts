plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.gobovr.mypod"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.gobovr.mypod"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
    }

    buildFeatures {
        compose = true
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {
    implementation(project(":innertube"))

    implementation(libs.core.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material.icons.core)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.session)
    implementation(libs.okhttp)
    implementation(libs.coroutines.android)
    implementation(libs.timber)
    debugImplementation(libs.compose.ui.tooling)

    coreLibraryDesugaring(libs.desugaring)
}
