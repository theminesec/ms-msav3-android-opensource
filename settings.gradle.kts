pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()

        val GITHUB_USERNAME: String? by settings
        val GITHUB_TOKEN: String? by settings

        // MineSec's maven registry
        requireNotNull(GITHUB_USERNAME) {
            """
                    Please set your MineSec Github credential in `gradle.properties`.
                    On local machine,
                    ** DO NOT **
                    ** DO NOT **
                    ** DO NOT **
                    Do not put it in the project's file. (and accidentally commit and push)
                    ** DO **
                    Do set it in your machine's global (~/.gradle/gradle.properties)
                """.trimIndent()
        }
        requireNotNull(GITHUB_TOKEN)
        println("github acc: $GITHUB_USERNAME")

        maven {
            name = "MineSecMavenClientRegistry"
            url = uri("https://maven.pkg.github.com/theminesec/ms-registry-client")
            credentials {
                username = GITHUB_USERNAME
                password = GITHUB_TOKEN
            }
        }
        maven {
            name = "gprInternal"
            url = uri("https://maven.pkg.github.com/theminesec/ms-registry-internal")
            credentials {
                username = GITHUB_USERNAME
                password = GITHUB_TOKEN
            }
        }
    }
    repositories {
        google {
            mavenLocal()
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "MSAV3Opensource"
include(":app")
 