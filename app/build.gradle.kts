plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.proyectogrupo1musicstore"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.proyectogrupo1musicstore"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    //Dependencia de Firebas
    //noinspection BomWithoutPlatform
    implementation("com.google.firebase:firebase-bom:32.4.1")
    implementation ("com.google.firebase:firebase-analytics:21.4.0")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    /*Abre una conexión entre el HTTP y el celular*/
    implementation ("com.android.volley:volley:1.2.1")
    /*conexion a glide*/
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")
    /*Para utilizar el algoritmo de encriptación bcrypt*/
    implementation ("org.mindrot:jbcrypt:0.4")
}