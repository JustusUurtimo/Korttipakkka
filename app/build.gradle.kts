plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.parcelize")

    id("de.mannodermaus.android-junit5")
    id("org.jetbrains.kotlinx.kover")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("org.sonarqube")
}
var isDebug by extra(true)

android {
    namespace = "com.sq.thed_ck_licker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sq.thed_ck_licker"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        defaultPublishConfig("debug")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebug = false
        }
        debug {
            isMinifyEnabled = false
            isTestCoverageEnabled = true
            buildConfigField("boolean", "DEBUG", "true")
            buildConfigField("boolean", "IS_DEBUG", "true")
            resValue("bool", "IS_DEBUG", true.toString())
        }
        create("super_debug") {
            isDebuggable = true
            isJniDebuggable = true
            isRenderscriptDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            multiDexEnabled = false
            matchingFallbacks += listOf("debug")
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
        buildConfig = true
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
//        unitTests.returnDefaultValues = true
        unitTests.isReturnDefaultValues = true

    }
}

sonar {
    properties {
        property("sonar.projectKey", "JustusUurtimo_Korttipakkka")
        property("sonar.organization", "korttipakka")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}



dependencies {
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
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
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    //navigation
    implementation(libs.androidx.navigation.compose)

    // (Required) Writing and executing Unit Tests on the JUnit Platform
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")

    // (Optional) If you need "Parameterized Tests"
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.2")

    // (Optional) If you also have JUnit 4-based tests
//    testImplementation("junit:junit:4.13.2") == testImplementation(libs.junit)
    testImplementation(libs.junit)
    testImplementation("org.junit.vintage:junit-vintage-engine:5.7.2")
}