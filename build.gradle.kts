import de.undercouch.gradle.tasks.download.Download
import eu.emundo.gradle.sevenz.UnSevenZ
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.3.4.RELEASE"
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	id("org.jetbrains.kotlin.plugin.jpa") version "1.4.10"
	id("eu.emundo.sevenz") version "1.0.5"
	id("de.undercouch.download") version "4.1.1"
	kotlin("jvm") version "1.4.10"
	kotlin("plugin.spring") version "1.4.10"
}

group = "dk.mercell.playday"
version = "0.0.2-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

val hi2txtFilename = "hi2txt@1.12@20200502-Java.7z"

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("com.h2database:h2")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
}

tasks {

	register<Download>("downloadLibs") {
		src("https://greatstoneex.github.io/hi2txt-doc/archives/${hi2txtFilename}")
		dest(file("lib/"))
	}

	register<UnSevenZ>("extractingLibs") {
		sourceFile = file("lib/${hi2txtFilename}")
		outputDir = file("lib/")
		mustRunAfter("downloadLibs")
	}

	register<Delete>("deleteUnusedFiles") {
		delete = setOf(
			file("lib/hi2txt.bat"),
			file("lib/hi2txt_doc"),
			file("lib/${hi2txtFilename}")
		)
		mustRunAfter("extractingLibs")
	}

	register("installLibs") {
		group = "build"
		description = "Download and install external libs."
		dependsOn("downloadLibs", "extractingLibs", "deleteUnusedFiles")
	}

	named("assemble") {
		dependsOn("installLibs")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}
