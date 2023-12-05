@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
    id("signing")
}

android {
    namespace = "com.microsoft.identity.msal"
    compileSdk = Setup.Version.compileSdkVersion

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    defaultConfig {
        multiDexEnabled = true
        minSdk = Setup.Version.minSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        // testCoverageEnabled flag is set to true to get coverage reports for Android Tests
        getByName("debug") {
            isJniDebuggable = true
            enableUnitTestCoverage = project.hasProperty("coverage")
            buildConfigField("String", "VERSION_NAME", "\"${project.version}\"")
        }
        getByName("release") {
            isMinifyEnabled = false
            isJniDebuggable = false

            proguardFile(getDefaultProguardFile("proguard-android.txt"))
            proguardFile("consumer-rules.pro")
            buildConfigField("String", "VERSION_NAME", "\"${project.version}\"")
        }
    }

    lint {
        abortOnError = true
        disable += "MissingPermission"
        disable += "LongLogTag"
        disable += "DefaultLocale"
        disable += "UnusedResources"
        disable += "GradleDependency"
        disable += "GradleOverrides"
        disable += "OldTargetApi"
        disable += "ExportedService"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    flavorDimensions += "main"

    productFlavors {
        // The 'local' productFlavor sources common from mavenLocal and is intended to be used
        // during development.
        create("local") {
            dimension = "main"
        }

        create("snapshot") {
            dimension = "main"
        }

        // The 'dist' productFlavor sources common from a central repository and is intended
        // to be used for releases.
        create("dist") {
            dimension = "main"
        }
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/main/AndroidManifest.xml")
            java.srcDirs("src/main/java", "src/main/kotlin")
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Setup.Version.kotlinVersion}")
    implementation("androidx.appcompat:appcompat:${Setup.Version.appCompatVersion}")
    implementation("androidx.browser:browser:${Setup.Version.browserVersion}")
    implementation("androidx.constraintlayout:constraintlayout:${Setup.Version.constraintLayoutVersion}")
    implementation("com.google.code.gson:gson:${Setup.Version.gsonVersion}")
    implementation("cz.msebera.android:httpclient:${Setup.Version.mseberaApacheHttpClientVersion}")
    implementation(enforcedPlatform("io.opentelemetry:opentelemetry-bom:${Setup.Version.opentelemetryVersion}"))
    implementation("io.opentelemetry:opentelemetry-api:${Setup.Version.opentelemetryVersion}")
    implementation("com.nimbusds:nimbus-jose-jwt:${Setup.Version.nimbusVersion}") {
        exclude(module = "asm")
    }
    implementation("com.microsoft.identity:common:${Setup.Version.msalCommonVersion}") {
        exclude(group = "com.microsoft.device.display")
    }

    compileOnly("com.github.spotbugs:spotbugs-annotations:4.3.0")
    compileOnly("org.projectlombok:lombok:${Setup.Version.lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${Setup.Version.lombokVersion}")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:${Setup.Version.coreLibraryDesugaringVersion}")

    // Test dependencies
    testImplementation("junit:junit:${Setup.Version.junitVersion}")
    testImplementation("org.mockito:mockito-inline:${Setup.Version.mockitoCoreVersion}")
    testImplementation("org.robolectric:robolectric:${Setup.Version.robolectricVersion}")
    testImplementation("androidx.test:core:${Setup.Version.androidxTestCoreVersion}")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib:${Setup.Version.kotlinVersion}")

    // InstrumentationTest dependencies
    androidTestImplementation("androidx.test.ext:junit:${Setup.Version.androidxJunitVersion}")
    androidTestImplementation("androidx.test:rules:${Setup.Version.rulesVersion}")
    androidTestImplementation("org.mockito:mockito-android:${Setup.Version.mockitoAndroidVersion}")
    androidTestImplementation("org.jetbrains.kotlin:kotlin-stdlib:${Setup.Version.kotlinVersion}")
}

afterEvaluate {
    // Gradle 6.1.1 and Android Gradle Plugin 4.0.1 doesn't rename the file (see 'outputFileName' above)
    // Adding this work around to have the file properly renamed.
    tasks.named("assembleDistRelease").configure {
        val buildFile = file("$buildDir/outputs/aar/msal-dist-release.aar")
        doLast {
            if (buildFile.exists()) {
                print("Build file $buildFile")
                val newFileName = "$buildDir/outputs/aar/msal-${Setup.Version.versionName}.aar"
                println("Renaming build file $buildFile to '$newFileName'")
                if (!buildFile.renameTo(file(newFileName))) {
                    println("Rename failed!")
                }
            }
        }
    }
}

apply(from = "$projectDir/publish.gradle.kts")

tasks {
    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(android.sourceSets.getByName("main").java.srcDirs)
    }

    artifacts {
        archives(sourcesJar)
    }
}
