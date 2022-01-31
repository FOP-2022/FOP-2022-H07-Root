repositories {
    mavenLocal()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    compileOnly("org.sourcegrade:jagr-launcher:0.4.0-SNAPSHOT")
    compileOnly("org.sourcegrade:jagr-grader-api:0.4-SNAPSHOT")
    compileOnly("org.ow2.asm:asm:9.2")
    implementation("org.mockito:mockito-inline:4.2.1-SNAPSHOT")
    implementation("org.powermock:powermock-api-mockito2:2.0.9")
    implementation("org.powermock:powermock-module-junit4:2.0.9")
    implementation("com.google.guava:guava:31.0.1-jre")
    implementation("fr.inria.gforge.spoon:spoon-core:10.0.0")
    implementation(project(":solution"))
    implementation("org.apache.logging.log4j:log4j-api:2.17.1")
    implementation("org.apache.logging.log4j:log4j-core:2.17.1")
    implementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

tasks {
    create<Jar>("buildLibs") {
        group = "build"
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        val runtimeDeps = sourceSets.main.get().runtimeClasspath.mapNotNull {
            if (it.path.toLowerCase().contains("h07")) {
                null
            } else if (it.isDirectory) {
                it
            } else {
                zipTree(it)
            }
        }
        from(runtimeDeps)
        archiveFileName.set("h07-libs.jar")
    }
}
