plugins {
  java
}
allprojects {
  apply(plugin = "java")
  repositories {
    mavenCentral()
  }
  java {
    withSourcesJar()
  }
  tasks {
    withType<JavaCompile> {
      options.encoding = "UTF-8"
      sourceCompatibility = "11"
      targetCompatibility = "11"
    }
    jar {
      archiveFileName.set("${rootProject.name}-${project.name}.jar")
    }
    named<Jar>("sourcesJar") {
      archiveFileName.set("${rootProject.name}-${project.name}-sources.jar")
    }
  }
}
