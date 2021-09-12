# SpigotUpdateChecker

![](https://img.shields.io/nexus/r/net.savagedev/SpigotUpdateChecker?nexusVersion=3&server=https%3A%2F%2Frepo.savagedev.net%2F&style=flat-square)

A simple SpigotMC resource update checker.

How to include the API with Gradle:
```
plugins {
    id 'com.github.johnrengelman.shadow' version '6.0.0'
    id 'java'
}

repositories {
    maven { url 'https://repo.savagedev.net/repository/maven-releases/' }
}

dependencies {
    implementation 'net.savagedev:SpigotUpdateChecker:1.0.0'
}

shadowJar {
    archiveFileName = "${rootProject.name}-Spigot-${project.version}.jar"
}
```
