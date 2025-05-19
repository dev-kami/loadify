plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
    id("signing")
}

android {
    namespace = "com.quad.loadify"
    compileSdk = 35

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.quadlogixs.loadify"
                artifactId = "loadify"
                version = "0.0.3"

                pom {
                    name.set("Loadify")
                    description.set("Powerful image loader for Jetpack Compose")
                    url.set("https://github.com/kami-kamran/Loadify")
                    licenses {
                        license {
                            name.set("MIT")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                    developers {
                        developer {
                            id.set("kami-kamran")
                            name.set("Kamran")
                            email.set("mkami.kamran786@gmail.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:https://github.com/kami-kamran/Loadify.git")
                        developerConnection.set("scm:git:ssh://github.com/kami-kamran/Loadify.git")
                        url.set("https://github.com/kami-kamran/Loadify")
                    }
                }
            }
        }

        repositories {
            maven {
                name = "central"
                url = uri("https://s01.oss.sonatype.org/content/repositories/releases/")
                credentials {
                    username = findProperty("nexusUsername") as String?
                    password = findProperty("nexusPassword") as String?
                }
            }
        }
    }

    signing {
        useGpgCmd()
        sign(publishing.publications["release"])
    }
}




dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
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
    implementation(libs.androidsvg)
    implementation(libs.okhttp)
    api(libs.lottie.compose)
}
