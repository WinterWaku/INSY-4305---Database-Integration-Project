plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "com.example.project3"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.xerial:sqlite-jdbc:3.45.3.0")
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass = "com.example.project3.Launcher"
    // Ensure JavaFX modules are passed to the JVM at runtime
    applicationDefaultJvmArgs = listOf(
        "--add-opens", "javafx.graphics/com.sun.javafx.application=ALL-UNNAMED"
    )
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

// Pass JavaFX module path args when running via Gradle
tasks.withType<JavaExec> {
    jvmArgs = listOf(
        "--add-opens", "javafx.graphics/com.sun.javafx.application=ALL-UNNAMED"
    )
}
