import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.withType

plugins {
    id("java")
    id("de.eldoria.plugin-yml.paper") version "0.8.0"
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
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")

    implementation(fileTree("libs") {
        include("**/*.jar")
    })
}

paper {
    name = "${rootProject.name}-paper"
    version = "${rootProject.version}"
    description = "${rootProject.description}"
    author = "jsexp"

    apiVersion = "1.20"
    main = "me.bombardeen.obsidra.paper.ObsidraPaperImpl"
    generateLibrariesJson = true
}

tasks.withType<ShadowJar> {
    archiveFileName.set("${rootProject.name}-paper-${rootProject.version}.jar")
}