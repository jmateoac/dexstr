plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.paparazzi)
    id("kotlin-parcelize")
    kotlin("kapt")
}

android {
    namespace = "social.plasma.features.onboarding.ui"
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
    implementation(projects.features.feeds.screens)
    implementation(projects.features.onboarding.screens)
    implementation(projects.features.discovery.screens)
    implementation(projects.common.ui)
    implementation(projects.data.models)

    implementation(libs.circuit.core)
    implementation(libs.coil.compose)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material3)
    implementation(libs.compose.material.materialicons)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.ui)
    implementation(libs.compose.ui.uitoolingpreview)
    implementation(libs.compose.ui.util)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.timber)

    debugImplementation(libs.compose.ui.test.manifest)
    debugImplementation(libs.compose.ui.uitooling)

    testImplementation(projects.common.ui.testutils)
    testImplementation(libs.junit)
    testImplementation(libs.testparameterinjector)
}
