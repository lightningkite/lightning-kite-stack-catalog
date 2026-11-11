// Publishes `com.lightningkite:stack-catalog` and `com.lightningkite:stack-bom`.  `lk` writes both inputs into
// build/ and passes the version; see RELEASE-SPEC.md in lightning-kite-release-tool, "Catalogs and BOM".
plugins {
    `version-catalog`
    `java-platform`
    `maven-publish`
}

group = "com.lightningkite"
version = providers.gradleProperty("stackVersion").get()

catalog {
    versionCatalog {
        from(files("build/stack.versions.toml"))
    }
}

dependencies {
    constraints {
        file("build/stack-bom.txt").readLines().forEach { api(it) }
    }
}

publishing {
    repositories {
        maven { name = "staging"; url = uri(layout.buildDirectory.dir("staging-repo")) }
    }
    publications {
        create<MavenPublication>("catalog") {
            artifactId = "stack-catalog"
            from(components["versionCatalog"])
        }
        create<MavenPublication>("bom") {
            artifactId = "stack-bom"
            from(components["javaPlatform"])
        }
    }
}
