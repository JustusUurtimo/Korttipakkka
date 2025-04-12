// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    jacoco
    id("org.jetbrains.kotlinx.kover") version "0.9.1"
}
buildscript{
    repositories {
        google()
        mavenCentral()
    }

    dependencies {

        classpath("de.mannodermaus.gradle.plugins:android-junit5:1.12.0.0")
    }

}