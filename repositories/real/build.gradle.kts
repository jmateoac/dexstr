plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "social.plasma.shared.repositories.real"
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
    api(projects.repositories.api)
    implementation(projects.common.utils.real)
    implementation(projects.data.daos)
    implementation(projects.data.nostr)
    implementation(projects.data.models)
    implementation(libs.nostrino)
    implementation(libs.okhttp.core)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.moshi.core)

    testImplementation(projects.data.nostr.fakes)
    testImplementation(projects.data.daos.fakes)
    testImplementation(projects.repositories.fakes)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.moshi.kotlin)
    testImplementation(libs.okhttp.mockwebserver)
    testImplementation(libs.truth)
    testImplementation(libs.turbine)

    coreLibraryDesugaring(libs.android.desugaring)
}
