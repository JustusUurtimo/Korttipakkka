plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("pl.droidsonroids.pitest") version "0.2.20"
//    id("info.solidsoft.pitest.aggregator")
//    id("info.solidsoft.pitest.aggregator")
//    id("org.jetbrains.kotlin.jvm")
////    id("com.arcmutate")
//    id("info.solidsoft.pitest") version "1.15.0"

//    id("info.solidsoft.pitest")


}
//apply(plugin = "info.solidsoft.pitest.aggregator")
var isDebug by extra(true)

android {

    android.testOptions.unitTests.isReturnDefaultValues = true
    namespace = "com.sq.thed_ck_licker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sq.thed_ck_licker"
        minSdk = 26
        targetSdk = 35
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
            isDebug = false
        }
        debug {

            isMinifyEnabled = false
//            isTestCoverageEnabled = true
            buildConfigField("boolean", "DEBUG", "true")
            buildConfigField("boolean", "IS_DEBUG", "true")
            resValue("bool", "IS_DEBUG", true.toString())
        }
//        create("super_debug") {
//            isDebuggable = true
//            isJniDebuggable = true
////            isRenderscriptDebuggable = true
//            signingConfig = signingConfigs.getByName("debug")
//            multiDexEnabled = false
//            matchingFallbacks += listOf("debug")
//        }

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
}
task("release"){dependsOn("pitest")}
task("debug"){dependsOn("pitest")}
task("super_debug"){dependsOn("pitest")}
tasks.build {
    dependsOn("pitest")
}
//tasks.named("compileKotlin").configure {
//    dependsOn("pitest")
////    mustRunAfter 'test'
//}

android.testOptions.unitTests.isReturnDefaultValues = true


pitest {
//    targetClasses = ['com.sq.thed_ck_licker.*']
    targetClasses.set(setOf("com.sq.thed_ck_licker.*"))
    excludeMockableAndroidJar = true
//    junit5PluginVersion = '1.2.0'
//    pitestVersion = '1.19.0'
    pitestVersion.set("1.19.0")
    threads = 4
//    outputFormats = ['XML', 'HTML']
    outputFormats.set(setOf("XML", "HTML"))
    mutationThreshold = 60
    coverageThreshold = 80
    timestampedReports.set(true)
//    reportAggregator {
//        testStrengthThreshold.set(50)
//        mutationThreshold.set(40)
//        maxSurviving.set(3)
//    }
//    failWhenNoMutations.set(false)
//    if (project.name in setOf("module-without-any-test")) {
//    }
    android.testOptions.unitTests.isReturnDefaultValues = true
//    android.testOptions.unitTests.returnDefaultValues = true


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
}