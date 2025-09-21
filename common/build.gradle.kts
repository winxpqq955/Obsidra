import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.gradleup.shadow") version "9.1.0"
}

group = "me.bombardeen.obsidra"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    implementation("com.google.code.gson:gson:2.13.2")
    implementation("com.google.guava:guava:33.5.0-jre")
    implementation("io.nats:jnats:2.22.0")
}

tasks.withType<ShadowJar> {
    archiveFileName.set("${rootProject.name}-common-${rootProject.version}.jar")

    relocate("org.jspecify", "me.bombardeen.obsidra.shaded.jspecify")
    relocate("org.bouncycastle", "me.bombardeen.obsidra.shaded.bouncycastle")
    relocate("io.nats", "me.bombardeen.obsidra.shaded.nats")
    relocate("com.google", "me.bombardeen.obsidra.shaded.google")
}