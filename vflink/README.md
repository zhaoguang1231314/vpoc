# 创建项目

```bash
mvn org.apache.maven.plugins:maven-archetype-plugin:2.4:generate \
	-DarchetypeGroupId=org.apache.flink \
	-DarchetypeArtifactId=flink-walkthrough-datastream-java \
	-DarchetypeCatalog=https://repository.apache.org/content/repositories/snapshots/ \
	-DarchetypeVersion=1.11.0 \
	-DgroupId=io.vizit.vpoc \
	-DartifactId=vflink \
	-Dversion=0.0.1-SNAPSHOT \
	-Dpackage=io.vizit.vpoc \
	-DinteractiveMode=false
```

# Trouble Shooting
## Cannot find 'resource' in class

如果你的项目是一个子项目，你可能会遇到下面的问题。

> [ERROR] Failed to execute goal org.apache.maven.plugins:maven-shade-plugin:3.0.0:shade (default) on project vflink: Unable to parse configuration of mojo org.apache.maven.plugins:maven-shade-plugin:3.0.0:shade for parameter resource: Cannot find 'resource' in class org.apache.maven.plugins.shade.resource.ManifestResourceTransformer -> [Help 1]

那么你在pom.xml可以添加下面的id:
```xml
<id>shade-vflink-jar</id>
```

## archetype-catalog.xml' is not supported anymore.
使用下面官方提供的创建项目的方式我会遇到这个问题。所有我就用了最开始的方式创建项目.
```bash
mvn archetype:generate \
    -DarchetypeGroupId=org.apache.flink \
    -DarchetypeArtifactId=flink-walkthrough-datastream-java \
    -DarchetypeVersion=1.11.0 \
    -DgroupId=frauddetection \
    -DartifactId=frauddetection \
    -Dversion=0.1 \
    -Dpackage=spendreport \
    -DinteractiveMode=false
```

## java.lang.NoClassDefFoundError: org/apache/flink/streaming/api/functions/source/SourceFunction
在IDE里面运行，需要注释掉provided
```xml
<dependency>
			<groupId>org.apache.flink</groupId>
			<artifactId>flink-streaming-java_${scala.binary.version}</artifactId>
			<version>${flink.version}</version>
<!--			<scope>provided</scope>-->
		</dependency>
		<dependency>
			<groupId>org.apache.flink</groupId>
			<artifactId>flink-clients_${scala.binary.version}</artifactId>
			<version>${flink.version}</version>
<!--			<scope>provided</scope>-->
		</dependency>
```

