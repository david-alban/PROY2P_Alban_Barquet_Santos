import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.JavadocMemberLevel

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.espol.proyecto2"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.espol.proyecto2"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}

val androidExtension =
    extensions.getByType<com.android.build.api.dsl.ApplicationExtension>()

val androidComponents =
    extensions.getByType<com.android.build.api.variant.ApplicationAndroidComponentsExtension>()

androidComponents.onVariants(
    androidComponents.selector().withBuildType("debug")
) { variant ->

    tasks.register<Javadoc>("generarJavadoc") {
        group = "documentation"
        description = "Genera el Javadoc del proyecto de forma limpia"

        source(
            files(
                androidExtension.sourceSets
                    .getByName("main")
                    .java
                    .directories
            )
        )

        classpath =
            variant.compileClasspath +
                    files(androidComponents.sdkComponents.bootClasspath)

        exclude("**/R.java", "**/BuildConfig.java")

        destinationDir = file("$rootDir/JAVADOC")

        options {
            encoding = "UTF-8"
            memberLevel = JavadocMemberLevel.PROTECTED

            (this as StandardJavadocDocletOptions).apply {
                header = "Proyecto 2P - Javadoc"
                addStringOption("encoding", "UTF-8")
                addStringOption("docencoding", "UTF-8")
                addStringOption("charset", "UTF-8")
            }
        }

        isFailOnError = false
    }
}
