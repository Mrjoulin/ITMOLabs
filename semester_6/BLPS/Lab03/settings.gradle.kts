plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "business1"
include("email-worker", "shared")
//findProject("email-worker")?.name = "email-worker"
//include("shared")
//findProject("shared")?.name = "shared"