version = LibraryAndroidCoordinates.LIBRARY_VERSION

plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
    publish
}

android {
    namespace = "de.luckyworks.compose.charts.line"

    compileSdk = libs.versions.compile.sdk.version.get().toInt()

    defaultConfig {
        minSdk = libs.versions.min.sdk.version.get().toInt()
        targetSdk = libs.versions.target.sdk.version.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

/* lint {
        isWarningsAsErrors = true
        isAbortOnError = true
    } */

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(projects.lib.common)

    implementation(libs.compose.animation)
    implementation(libs.compose.core)
    implementation(libs.compose.layout)
    implementation(libs.compose.foundation)
    implementation(libs.compose.runtime)
    implementation(libs.compose.tooling)

}
