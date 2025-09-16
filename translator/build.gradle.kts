plugins {
    id("java")
    id("application")
    id("com.gradleup.shadow") version "9.1.0"
}

group = "dev.lunna.translator"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("dev.lunna.nana.translator.bootstrap.Bootstrap")
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation("org.fusesource.jansi:jansi:2.4.2")

    implementation("info.picocli:picocli:4.7.7")
    implementation("com.google.inject:guice:7.0.0")

    implementation("io.github.ollama4j:ollama4j:1.1.0")
    implementation("com.google.genai:google-genai:1.16.0")

    implementation("org.hibernate:hibernate-core:7.1.1.Final")
    implementation("org.hibernate.orm:hibernate-community-dialects:7.1.1.Final")
    implementation("org.xerial:sqlite-jdbc:3.50.3.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.jetbrains:annotations:26.0.2-1")
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<Javadoc> {
        options.encoding = "UTF-8"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}