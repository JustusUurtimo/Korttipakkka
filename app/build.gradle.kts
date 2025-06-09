import com.android.build.api.dsl.TestCoverage
import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.gradle.kotlin.dsl.main
import org.gradle.kotlin.dsl.test

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

//    id("org.jetbrains.kotlinx.kover") version "0.9.1"

    id("pl.droidsonroids.pitest")
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
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebug = false
        }
        debug {
            isMinifyEnabled = false
            buildConfigField("boolean", "DEBUG", "true")
            buildConfigField("boolean", "IS_DEBUG", "true")
            resValue("bool", "IS_DEBUG", true.toString())
        }
        create("super_debug") {
            isDebuggable = true
            isJniDebuggable = true
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
            it.useJUnitPlatform {
//                fun TestCoverage.() {
//
//                }
            }
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
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            // Corrected path based on your finding:
            "${layout.buildDirectory.get()}/reports/kover/reportDebug.xml"
        )
    }
}

kover {
    currentProject{
        createVariant("customaaaa") {
            add("debug")
        }
    }

    reports {
        // filters for all report types of all build variants
        filters {
            excludes {
                androidGeneratedClasses()
            }
        }

        variant("release") {
//            html{
////                onCheck = false
//            }
//            xml{
////                onCheck = false
//            }
            // verification only for 'release' build variant
            verify {
                rule {
                    minBound(50)
                }
            }

            // filters for all report types only for 'release' build variant
            filters {
                excludes {
                    androidGeneratedClasses()
                    classes(
                        // excludes debug classes
                        "*.DebugUtil"
                    )
                }
            }
        }
    }
}

//tasks.named("pitestSuper_debug") {
//    dependsOn("compileSuper_debugKotlin")
//}
//tasks.named("pitest") {
//    dependsOn("compileSuper_debugKotlin")
//}

tasks.matching { it.name.startsWith("pitest") }.configureEach {
    dependsOn(
        tasks.matching { t -> t.name.startsWith("compile") && t.name.endsWith("Kotlin") }
    )
}



pitest{
    // Match the main class you want to mutate
    targetClasses.set(
        setOf("com.sq.thed_ck_licker.*")
    )

    // Ensure PIT finds your test class
    targetTests.set(
        setOf("com.sq.thed_ck_licker.*")
    )
//    targetClasses.set(setOf("${project.group}.*")) //by default "${project.group}.*"
    pitestVersion.set("1.19.5") //not needed when a default PIT version should be used
    threads.set(4)
    outputFormats.set(setOf("XML", "HTML"))
    timestampedReports.set(false)
    useClasspathFile.set(true)     //useful with bigger projects on Windows
    fileExtensionsToFilter.addAll("xml", "orbit")
    jvmArgs.set(listOf("-Xmx1024m"))
    useClasspathFile.set(true) //useful with bigger projects on Windows
    fileExtensionsToFilter.addAll("xml", "orbit")
    verbose = true
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
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)

    // (Optional) If you need "Parameterized Tests"
    testImplementation(libs.junit.jupiter.params)

    // (Optional) If you also have JUnit 4-based tests
//    testImplementation("junit:junit:4.13.2") == testImplementation(libs.junit)
    testImplementation(libs.junit)
    testImplementation(libs.junit.vintage.engine)
    testImplementation(kotlin("test"))

    // For Robolectric tests.
    testImplementation(libs.hilt.android.testing)
    // ...with Kotlin.
    kspTest(libs.hilt.android.compiler)


    // For instrumented tests.
    androidTestImplementation(libs.hilt.android.testing)
    // ...with Kotlin.
    kspAndroidTest(libs.hilt.android.compiler)

    testImplementation("org.pitest:pitest-maven:1.19.5")
    testImplementation("com.arcmutate:base:1.4.2")
    testImplementation("com.arcmutate:pitest-kotlin-plugin:1.4.3")
    testImplementation("org.pitest:pitest-junit5-plugin:1.2.3")
    // https://mvnrepository.com/artifact/com.arcmutate/arcmutate-android-parent
    testImplementation("com.arcmutate:arcmutate-android-parent:0.0.5")
    testImplementation("com.arcmutate:pitest-git-plugin:2.2.3")
}