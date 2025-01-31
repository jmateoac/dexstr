plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.paparazzi)
    kotlin("kapt")
}

android {
    namespace = "social.plasma.ui"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()


        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles("proguard-rules.pro")
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
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

}

dependencies {
    implementation(projects.data.models)
    implementation(projects.data.opengraph)
    implementation(projects.common.utils.real)

    implementation(libs.accompanist.flowlayout)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.paging.compose)

    implementation(libs.circuit.overlay)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.coil.svg)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material3)
    implementation(libs.compose.material.materialicons)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.ui)
    implementation(libs.compose.ui.uitoolingpreview)
    implementation(libs.compose.ui.util)

    implementation(libs.exoplayer.core)
    implementation(libs.exoplayer.ui)
    implementation(libs.exoplayer.hls)

    implementation(libs.nostrino)

    implementation(libs.okhttp.core)
    implementation(libs.timber)
    implementation(libs.touchimageview)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    debugImplementation(libs.compose.ui.test.manifest)
    debugImplementation(libs.compose.ui.uitooling)

    testImplementation(projects.common.ui.testutils)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.testparameterinjector)
}
