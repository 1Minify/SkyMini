import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.libsDirectory
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    java
    id("gg.essential.loom") version "0.10.0.+"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.lombok") version "1.9.10"
    id("io.freefair.lombok") version "8.1.0"

    //id("net.minecraftforge.gradle") version "3.+"
}

/*task("runClient", type = JavaExec::class) {
    main = "net.minecraft.launchwrapper.Launch"
    classpath = sourceSets.getByName("main").runtimeClasspath
    args("--gameDir", "C:\\Users\\cleme\\AppData\\Roaming\\.minecraft")

    // Weitere Optionen hier, z.B. JVM-Argumente
    // args("--gameDir", "path/to/your/minecraft/folder")
}*/


group = "at.minify.skymini"
version = "1.0"
val modid = "skymini"

// Toolchains:
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

sourceSets.main {
    output.setResourcesDir(file("$buildDir/classes/java/main"))
}



repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.spongepowered.org/maven/")
    // If you don't want to log in with your real minecraft account, remove this line
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    maven("https://jitpack.io") {
        content {
            includeGroupByRegex("com\\.github\\..*")
        }
    }
    //maven("https://repo.nea.moe/releases")
    maven("https://maven.notenoughupdates.org/releases")
}

val shadowImpl by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

val shadowModImpl by configurations.creating {
    configurations.modImplementation.get().extendsFrom(this)

}

val devenvMod by configurations.creating {
    isTransitive = false
    isVisible = false
}

dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

    implementation("org.lwjgl:lwjgl:3.2.3")

     //Discord RPC client
    shadowImpl("com.github.ILikePlayingGames:DiscordIPC:f91ed4b") {
        exclude(module = "log4j")
        because("Different version conflicts with Minecraft's Log4J")
        exclude(module = "gson")
        because("Different version conflicts with Minecraft's Log4j")
    }


    shadowImpl("org.spongepowered:mixin:0.7.11-SNAPSHOT") {
        isTransitive = false
    }
    annotationProcessor("org.spongepowered:mixin:0.8.4-SNAPSHOT")

    implementation(kotlin("stdlib-jdk8"))
    shadowImpl("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") {
        exclude(group = "org.jetbrains.kotlin")
    }



    //"shadowModImpl"("org.notenoughupdates.moulconfig:modern:1.3.0")

    // If you don't want to log in with your real minecraft account, remove this line
    modRuntimeOnly("me.djtheredstoner:DevAuth-forge-legacy:1.1.0")

    //@Suppress("VulnerableLibrariesLocal")
    //implementation("com.github.hannibal002:notenoughupdates:4957f0b:all")
    //@Suppress("VulnerableLibrariesLocal")
    //devenvMod("com.github.hannibal002:notenoughupdates:4957f0b:all")

    shadowModImpl("com.github.NotEnoughUpdates:MoulConfig:1.3.0")
    //devenvMod("com.github.NotEnoughUpdates:MoulConfig:1.3.0:test")

    //shadowImpl("moe.nea:libautoupdate:1.0.3")
    shadowImpl("org.jetbrains.kotlin:kotlin-reflect:1.9.0")

    shadowModImpl(files("addons/libautoupdate-1.0.3.jar"))
}
kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
            enableLanguageFeature("BreakContinueInInlineLambdas")
        }
    }
}


// Minecraft configuration:
loom {
    launchConfigs {
        "client" {
            // If you don't want mixins, remove these lines
            property("mixin.debug", "true")
            property("asmhelper.verbose", "true")
            arg("--tweakClass", "org.spongepowered.asm.launch.MixinTweaker")
            arg("--mixin", "mixins.${modid}.json")
            val modFiles = devenvMod
                    .incoming.artifacts.resolvedArtifacts.get()
            arg("--mods", modFiles.joinToString(",") { it.file.relativeTo(file("run")).path })
        }
    }
    forge {
        pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
        // If you don't want mixins, remove this lines
        mixinConfig("mixins.${modid}.json")
    }
    // If you don't want mixins, remove these lines
    mixin {
        defaultRefmapName.set("mixins.${modid}.refmap.json")
    }
    runConfigs {
        "server" {
            isIdeConfigGenerated = false
        }
    }
}

// Tasks:
tasks.processResources {
    inputs.property("version", version)
    filesMatching("mcmod.info") {
        expand("version" to version)
    }
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}

tasks.withType(Jar::class) {
    archiveBaseName.set("skymini")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest.attributes.run {
        //this["Main-Class"] = "Main"
        this["FMLCorePluginContainsFMLMod"] = "true"
        this["ForceLoadAsMod"] = "true"
        this["ModSide"] = "CLIENT"
        // If you don't want mixins, remove these lines
        this["TweakClass"] = "org.spongepowered.asm.launch.MixinTweaker"
        this["MixinConfigs"] = "mixins.${modid}.json"
    }
}


val remapJar by tasks.named<net.fabricmc.loom.task.RemapJarTask>("remapJar") {
    archiveClassifier.set("")
    from(tasks.shadowJar)
    input.set(tasks.shadowJar.get().archiveFile)
}

tasks.shadowJar {
    destinationDirectory.set(layout.buildDirectory.dir("badjars"))
    archiveClassifier.set("all-dev")
    configurations = listOf(shadowImpl, shadowModImpl)
    relocate("io.github.notenoughupdates.moulconfig", "at.minify.skymini.deps.moulconfig")
   doLast {
        configurations.forEach {
            println("Config: ${it.files}")
        }
    }
    exclude("META-INF/versions/**")

    //relocate("io.github.moulberry.moulconfig", "at.hannibal2.${modid}.deps.moulconfig")
    //relocate("moe.nea.libautoupdate", "at.hannibal2.${modid}.deps.libautoupdate")
}


tasks.jar {
    archiveClassifier.set("nodeps")
    destinationDirectory.set(layout.buildDirectory.dir("badjars"))
}
tasks.assemble.get().dependsOn(tasks.remapJar)

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

tasks.register("copyJar", Copy::class) {
    from("${buildDir}/libs")
    into("${System.getProperty("user.home")}/AppData/Roaming/.minecraft/mods")
    rename { fileName ->
        fileName.replaceFirst("SkyMini-1.0", "SkyMini")
    }
    dependsOn("remapJar")
}

tasks.getByName("build").dependsOn("copyJar")