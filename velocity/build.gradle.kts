plugins {
    id("java")
    id("com.gradleup.shadow") version "9.1.0"
}

group = "me.bombardeen.obsidra"
version = "1.0.0"

repositories {
    mavenCentral()

    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")

    implementation(fileTree("libs") {
        include("**/*.jar")
    })
}