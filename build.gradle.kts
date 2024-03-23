import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    kotlin("plugin.jpa") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
}

group = "kr.kro.lanthanide"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("org.commonmark:commonmark:0.22.0")


    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val reactDir = "$projectDir/src/main/webapp/reactapp"

sourceSets {
    main {
        resources {
            srcDirs("$projectDir/src/main/resources")
        }
    }
}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.WARN
//    dependsOn("copyReactBuildFiles")
}

task<Exec>("installReact") {
    workingDir(reactDir)
    inputs.dir(reactDir)
    group = BasePlugin.BUILD_GROUP

    if(System.getProperty("os.name").lowercase(Locale.ROOT).contains("windows")){
        commandLine("npm.cmd", "audit", "fix")
        commandLine("npm.cmd", "install")
    }else{
        commandLine("npm", "audit", "fix")
        commandLine("npm", "install")
    }
}

task<Exec>("buildReact") {
    dependsOn("installReact")
    workingDir(reactDir)
    inputs.dir(reactDir)
    group = BasePlugin.BUILD_GROUP

    if(System.getProperty("os.name").lowercase(Locale.ROOT).contains("windows")){
        commandLine("npm.cmd", "run-script", "build")
    }else{
        commandLine("npm", "run-script", "build")
    }
}

task<Copy>("copyReactBuildFiles") {
    dependsOn("buildReact")
    from("$reactDir/build")
    into("$projectDir/src/main/resources/static")
}