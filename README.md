# SpigotUpdateChecker
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
    compile 'com.github.SavageAvocado:SpigotUpdateChecker:master-SNAPSHOT'
}

shadowJar {
    getArchiveFileName().set('${project.name}-${project.version}.jar')
    configurations = [project.configurations.compile]
}

artifacts {
    shadowJar
}
```
