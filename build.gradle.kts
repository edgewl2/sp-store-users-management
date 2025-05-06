plugins {
    java
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.flywaydb.flyway") version "11.8.0"
    id("org.openapi.generator") version "7.12.0"
}

group = "dev.edgeahz.ec.spstore"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2024.0.1"

dependencies {
    //Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    //Spring Cloud
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // OAuth2 Resource Server para validar tokens
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-oauth2-jose")

    // Database
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")
    runtimeOnly("com.mysql:mysql-connector-j")

    // Lombok
    compileOnly("org.projectlombok:lombok")

    // MapStruct
    implementation("org.mapstruct:mapstruct:1.6.3")

    // OpenAPI
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.30")

    // Develop
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Annotation Processors - Orden importante
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("com.h2database:h2")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(listOf(
        "-Amapstruct.defaultComponentModel=spring",
        "-Amapstruct.unmappedTargetPolicy=IGNORE"
    ))
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$projectDir/src/main/resources/openapi/api.yaml")
    outputDir.set("${layout.buildDirectory.get()}/generated")
    apiPackage.set("dev.edgeahz.ec.spstore.user_management.api.rest")
    modelPackage.set("dev.edgeahz.ec.spstore.user_management.api.rest.dto")

    additionalProperties.set(mapOf(
        "additionalModelTypeAnnotations" to "@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)"
    ))

    configOptions.set(mapOf(
        "interfaceOnly" to "true",
        "delegatePattern" to "true",
        "useSpringBoot3" to "true",
        "returnSuccessCode" to "true",
        "useTags" to "true",
        "useOptional" to "false",
        "dateLibrary" to "java8",
        "serializationLibrary" to "jackson"
    ))
}

sourceSets {
    main {
        java {
            srcDir("${layout.buildDirectory.get()}/generated/src/main/java")
        }
    }
}

tasks.compileJava {
    dependsOn(tasks.openApiGenerate)
}

