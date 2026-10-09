plugins {
    kotlin("jvm") version "1.9.21"
    id("info.solidsoft.pitest") version "1.19.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    pitest("org.pitest:pitest-junit5-plugin:1.2.1")
}

configure<info.solidsoft.gradle.pitest.PitestPluginExtension> {
    targetClasses.set(setOf("tivpomodule.*"))
    targetTests.set(setOf("*Test", "*Tests"))

    pitestVersion.set("1.19.0")
    threads.set(4)

    outputFormats.set(setOf("HTML", "XML"))

    excludedClasses.set(
        setOf(
            "model.TextHolder",
            "kotlin.jvm.internal.*"
        )
    )

    excludedMethods.set(
        setOf(
            "equals",
            "hashCode",
            "toString",
            "copy",
            "component1",
            "component2",
            "component3",
            "component4",
            "component5",
            "component6",
            "component7",
            "copy\$default"
        )
    )
    avoidCallsTo.set(
        setOf("kotlin.jvm.internal.Intrinsics")
    )
}

tasks.test {
    useJUnitPlatform()
}