plugins {
    java
    id("org.sourcegrade.style") version "1.1.0"
}
allprojects {
    apply(plugin = "java")
    apply(plugin = "org.sourcegrade.style")
    repositories {
        mavenCentral()
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
    java {
        withSourcesJar()
    }
    dependencies {
        implementation("org.sourcegrade:jagr-grader-api:0.3")
        implementation("org.junit.jupiter:junit-jupiter:5.8.2")
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
