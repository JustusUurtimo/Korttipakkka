plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("jacoco")
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
        versionNameSuffix = rootProject.extra["defaultVersionNameSuffix"] as String

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebug = false
            enableUnitTestCoverage = false
            enableAndroidTestCoverage = false
        }
        debug {
            isMinifyEnabled = false
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
            buildConfigField("boolean", "DEBUG", "true")
            buildConfigField("boolean", "IS_DEBUG", "true")
            resValue("bool", "IS_DEBUG", true.toString())
        }
        create("super_debug") {
            isDebuggable = true
            isJniDebuggable = true
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
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

    tasks.withType<JacocoReport> {
        dependsOn("testDebugUnitTest", "createDebugCoverageReport")
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
        val fileFilter =
//            emptyList<String>()
        listOf(
            "**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*Test*.*",
            "android/**/*.*"
        )
        val debugTree = fileTree(File("$buildDir/intermediates/classes/debug")) {
            exclude(fileFilter)
        }
        val mainSrc = File("${project.projectDir}/src/main/java")
        sourceDirectories.setFrom(mainSrc)
        classDirectories.setFrom(debugTree)
//        executionData.setFrom(
//            fileTree(File(buildDir)) {
//                include(
//        "jacoco/testDebugUnitTest.exec",
//        "outputs/code-coverage/connected/*coverage.ec"
//                )
//            }
//        )
    }

//testOptions {
//    unitTests.all {
//            it.jacoco {
//                isIncludeNoLocationClasses = true
//        }
//    }
//        unitTests.isReturnDefaultValues = true
//}

//    buildTypes.configureEach {
//
//    }
//    debug {
//        testCoverageEnabled true
//    }
//    release {
//        minifyEnabled false
//        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
//    }

}


//    debug {
//        testCoverageEnabled true
//    }
//    release {
//        minifyEnabled false
//        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
//    }
//}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
//    implementation(libs.jacoco)
//    implementation(libs.org.jacoco.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}