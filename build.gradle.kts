plugins {
    id("java")
    id("com.gradleup.shadow") version "9.1.0"
}

group = "me.bombardeen.obsidra"
version = "1.0.0-BUGFIX"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "com.gradleup.shadow")

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.42")
        annotationProcessor("org.projectlombok:lombok:1.18.42")
    }

    tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
        relocate("org.jspecify", "me.bombardeen.obsidra.shaded.jspecify")
        relocate("org.bouncycastle", "me.bombardeen.obsidra.shaded.bouncycastle")
        relocate("io.nats", "me.bombardeen.obsidra.shaded.nats")
        relocate("com.google", "me.bombardeen.obsidra.shaded.google")
    }
}