// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    jacoco
    id("org.jetbrains.kotlinx.kover") version "0.9.1"
    id("com.google.dagger.hilt.android") version "2.56.1" apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.24" apply false
    id("org.sonarqube") version "6.0.1.5171"
}
buildscript{
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.junit5)
        classpath("org.bouncycastle:bcutil-jdk18on:1.78.1")

        classpath("pl.droidsonroids.gradle:gradle-pitest-plugin:0.2.21")
    }

}
subprojects {
    apply(plugin = "pl.droidsonroids.pitest")

    buildscript {
        dependencies.add("pitest", "com.arcmutate:pitest-kotlin-plugin:1.4.3")
        dependencies.add("pitest", "com.arcmutate:android:0.0.5")
        dependencies.add("pitest", "com.arcmutate:base:1.4.2")
        dependencies.add("pitest", "com.arcmutate:pitest-git-plugin:2.2.3")
    }
}

sonar {
    properties {
        property("sonar.projectKey", "JustusUurtimo_Korttipakkka")
        property("sonar.organization", "korttipakka")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}