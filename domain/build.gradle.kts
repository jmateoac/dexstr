plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "social.plasma.domain"
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
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

}

dependencies {
    implementation(projects.data.daos)
    implementation(projects.common.utils.real)
    implementation(projects.data.models)
    implementation(projects.data.nostr)
    implementation(projects.repositories.api)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.paging.common)
    implementation(libs.moshi.core)
    implementation(libs.moshi.kotlin)
    implementation(libs.nostrino)
    implementation(libs.timber)

    testImplementation(projects.repositories.fakes)
    testImplementation(projects.data.nostr.fakes)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.turbine)

    coreLibraryDesugaring(libs.android.desugaring)
}
