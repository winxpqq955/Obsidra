import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.withType

plugins {
    id("de.eldoria.plugin-yml.paper") version "0.8.0"
}

group = "me.bombardeen.obsidra"
version = "1.0.0-BUGFIX"

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    implementation(project(":Obsidra-common"))
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
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