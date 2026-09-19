plugins {
    id("java-library")
    id("maven-publish")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.23"
    id("xyz.jpenilla.run-paper") version "3.0.0"
}

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("26.2.build.+")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
    withSourcesJar()
    withJavadocJar()
}

tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("26.2")
        jvmArgs("-Xms2G", "-Xmx2G", "-Dcom.mojang.eula.agree=true")
    }

    processResources {
        val props = mapOf("version" to version, "description" to project.description)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name.set(project.name)
                description.set("What this library does")
            }
        }
    }

    repositories {
        maven {
            name = "nexus" // this name drives the credential property names below

            val releasesUrl = uri("https://nexus.bungo.ca/repository/maven-releases/")
            val snapshotsUrl = uri("https://nexus.bungo.ca/repository/maven-snapshots/")
            url = if (version.toString().endsWith("-SNAPSHOT")) snapshotsUrl else releasesUrl

            // Gradle looks up nexusUsername / nexusPassword automatically
            credentials(PasswordCredentials::class)
        }
    }
}
