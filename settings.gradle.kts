pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.aliyun.com/nexus/content/repositories/google")
        }
        maven {
            url = uri("https://maven.aliyun.com/nexus/content/groups/public")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/jcenter")
        }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url = uri("https://maven.aliyun.com/repository/apache-snapshots") }
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
//        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        maven {
            url = uri("https://maven.aliyun.com/nexus/content/repositories/google")
        }
        maven {
            url = uri("https://maven.aliyun.com/nexus/content/groups/public")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/jcenter")
        }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url = uri("https://maven.aliyun.com/repository/apache-snapshots") }
        google()
        mavenCentral()
    }
}

rootProject.name = "LearnAndroidGLES"
include(":app")
 