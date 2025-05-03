plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    `maven-publish`
}

group = "com.basic.logcat"
version = "0.0.1"


//// 创建一个task来发布源码
//tasks.register<Jar>("sourcesJar") {
//    archiveClassifier.set("sources")
//    val sources = android.sourceSets.map { set -> set.java.getSourceFiles() }
//    from(sources)
//}


afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            create<MavenPublication>("release") {
                // Applies the component for the release build variant.\
                // from(components["release"])
                // You can then customize attributes of the publication as shown below.
                groupId = (group.toString())
                artifactId = "logcat"
                version = version
            }
        }
    }
}


android {
    namespace = "com.basic.logcat"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.basic.logcat"
        minSdk = 24
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(project(":logcat"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}