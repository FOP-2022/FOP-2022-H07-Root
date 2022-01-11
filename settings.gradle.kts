rootProject.name = "FOP-2022-H07-Root"
include("FOP-2022-H07-Student")
include("grader")
include("solution")

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        id("org.sourcegrade.style") version "1.1.0-SNAPSHOT"
    }
}
