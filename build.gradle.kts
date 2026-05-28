import xyz.jpenilla.runpaper.task.RunServer

plugins {
    id("java-library")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

repositories {
    mavenCentral()

    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    paperweight.paperDevBundle("26.1.2.build.+")

    compileOnly("com.github.darksoulq:AbyssalLib:v2.3.6-mc.26.1.2")
    compileOnly("com.github.darksoulq:Relique:v2.1.2-mc.26.1.2")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

runPaper.folia.registerTask()

tasks {
    runServer {
        minecraftVersion("26.1.2")

        jvmArgs(
            "-Xms2G",
            "-Xmx2G",
            "-Dcom.mojang.eula.agree=true"
        )

        downloadPlugins {
            modrinth("abyssallib", "2.3.6-mc.26.1.2")
            modrinth("relique", "2.1.2-mc.26.1.2")
        }
    }

    processResources {
        val props = mapOf("version" to version)

        inputs.properties(props)
        filteringCharset = "UTF-8"

        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}

tasks.named<RunServer>("runFolia") {
    minecraftVersion("26.1.2")

    jvmArgs(
        "-Xms2G",
        "-Xmx2G",
        "-Dcom.mojang.eula.agree=true"
    )

    downloadPlugins {
        modrinth("abyssallib", "2.3.6-mc.26.1.2")
        modrinth("relique", "2.1.2-mc.26.1.2")
    }
}