java_library(
    name = "components",
    srcs = glob(["components/*.java"]),
    deps = [
        ":configuration",
        ":events",
        ":hash_util",
        ":ports",
        ":types",
        "@com_google_guava_guava//jar",
        "@org_apache_logging_log4j_log4j//jar",
        "@org_slf4j_slf4j_api//jar",
        "@org_slf4j_slf4j_log4j12//jar",
        "@se_sics_kompics_basic_kompics_component_java_timer//jar",
        "@se_sics_kompics_basic_kompics_port_timer//jar",
        "@se_sics_kompics_kompics_core//jar",
    ],
)

java_library(
    name = "events",
    srcs = glob(["events/*.java"]),
    deps = [
        ":types",
        "@com_google_guava_guava//jar",
        "@se_sics_kompics_kompics_core//jar",
    ],
)

java_library(
    name = "ports",
    srcs = glob(["ports/*.java"]),
    deps = [
        ":events",
        "@se_sics_kompics_kompics_core//jar",
    ],
)

java_library(
    name = "types",
    srcs = glob(["types/*.java"]),
)

java_library(
    name = "configuration",
    srcs = glob(["Configuration.java"]),
)

java_library(
    name = "hash_util",
    srcs = glob(["HashUtil.java"]),
    deps = [
        ":types",
        "@com_google_guava_guava//jar",
    ],
)

java_binary(
    name = "main",
    srcs = glob(["Main.java"]),
    main_class = "io.hops.blockreportsimulation.Main",
    deps = [
        ":components",
        "@com_typesafe_config//jar",
        "@log4j_log4j//jar",
        "@org_apache_logging_log4j_log4j//jar",
        "@se_sics_kompics_kompics_core//jar",
    ],
)
