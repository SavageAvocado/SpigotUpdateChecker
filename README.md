# SpigotUpdateChecker

[![](https://jitpack.io/v/SavageAvocado/SpigotUpdateChecker.svg)](https://jitpack.io/#SavageAvocado/SpigotUpdateChecker)

A simple SpigotMC resource update checker.

How to include the API with Gradle:
```
plugins {
    id 'com.github.johnrengelman.shadow' version '6.0.0'
    id 'java'
}

repositories {
	maven { url 'https://jitpack.io' }
}

dependencies {
    compile 'com.github.SavageAvocado:SpigotUpdateChecker:1.0.0'
}

shadowJar {
    getArchiveFileName().set('${project.name}-${project.version}.jar')
    configurations = [project.configurations.compile]
}

artifacts {
    shadowJar
}
```
