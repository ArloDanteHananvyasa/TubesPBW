plugins {
	java
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(23)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.0.0")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mockito:mockito-junit-jupiter")
	testImplementation("com.h2database:h2")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.postgresql:postgresql")
	implementation("com.zaxxer:HikariCP:5.0.1")
	implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.hibernate.validator:hibernate-validator:6.2.0.Final")
	implementation("javax.validation:validation-api:2.0.1.Final")
	implementation("org.springframework.boot:spring-boot-starter-security")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
