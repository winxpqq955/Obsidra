import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.gradleup.shadow") version "9.1.0"
}

group = "me.bombardeen.obsidra"
version = "1.0.0-BUGFIX"

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

tasks.withType<ShadowJar> {
    archiveFileName.set("${rootProject.name}-velocity-${rootProject.version}.jar")
}