import org.sourcegrade.submitter.submit

plugins {
    id("org.sourcegrade.submitter") version "0.4.0"
}

dependencies {
    // JUnit only available in "test" source set (./src/test)
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

tasks {
    submit {
        assignmentId = "h07"
        studentId = "ab12cdef"
        firstName = "sol_first"
        lastName = "sol_last"
    }
    test {
        useJUnitPlatform()
    }
}
