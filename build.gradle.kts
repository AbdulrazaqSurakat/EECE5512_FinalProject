// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}
dependencies {
    // Existing dependencies
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar"))))
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.github.amitshekhariitbhu.Fast-Android-Networking:android-networking:1.0.4")

    // ADD THIS LINE - ML Kit for Object Detection
    implementation("com.google.mlkit:object-detection:17.0.0")

    // Other existing dependencies...
}