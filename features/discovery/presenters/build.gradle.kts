plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.molecule)
    kotlin("kapt")
}

android {
    namespace = "social.plasma.features.discovery.presenters"
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


    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(projects.features.discovery.screens)
    implementation(projects.features.feeds.screens)
    implementation(projects.features.profile.screens)
    implementation(projects.repositories.api)
    implementation(projects.common.utils.api)
    implementation(projects.domain)
    implementation(projects.data.models)
    implementation(projects.data.nostr)

    implementation(libs.circuit.core)
    implementation(libs.circuit.retained)
    implementation(libs.nostrino)
    implementation(libs.timber)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    testImplementation(projects.repositories.fakes)
    testImplementation(projects.data.daos.fakes)
    testImplementation(projects.common.utils.fakes)
    testImplementation(libs.circuit.test)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.testparameterinjector)
    testImplementation(libs.truth)
    testImplementation(libs.turbine)
}
