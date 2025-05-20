plugins {
    id("java")
}

group = "org.polina.practice"
version = "1.0-SNAPSHOT"


tasks.test {
    useJUnitPlatform()
}