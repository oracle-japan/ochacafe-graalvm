<font size="4">**This demo doesn't work with GraalVM 22.2.0**</font>  
<font size="4">**Please build it with GraalVM 22.1.0**</font>  

# Helidon MP Database

Helidon MP application that uses JPA with ~~an in-memory H2~~ Oracle database

## Preparation

Edit src/main/resources/META-INF/microprofile-config.properties to adjust database settings.

``` properties
javax.sql.DataSource.test.dataSource.url=jdbc:oracle:thin:@//localhost:1521/PDB1
javax.sql.DataSource.test.dataSource.user=demo
javax.sql.DataSource.test.dataSource.password=Oracle12345
```

## Build and run

With JDK11+
```bash
mvn package
java -jar target/database-mp.jar
```

## Exercise the application

```
curl -X GET http://localhost:8080/pokemon
[{"id":1,"type":12,"name":"Bulbasaur"}, ...]

curl -X GET http://localhost:8080/type
[{"id":1,"name":"Normal"}, ...]

curl -H "Content-Type: application/json" --request POST --data '{"id":100, "type":1, "name":"Test"}' http://localhost:8080/pokemon
```

## Try health and metrics

```
curl -s -X GET http://localhost:8080/health
{"outcome":"UP",...
. . .

# Prometheus Format
curl -s -X GET http://localhost:8080/metrics
# TYPE base:gc_g1_young_generation_count gauge
. . .

# JSON Format
curl -H 'Accept: application/json' -X GET http://localhost:8080/metrics
{"base":...
. . .
```

## GraalVM Native Support

The generation of native binaries requires an installation of GraalVM 20.1.0+. For more
information about the steps necessary to use GraalVM with Helidon
see https://helidon.io/docs/v2/#/mp/guides/36_graalnative.


```
mvn -Pnative-image package -DskipTests
```

The generation of the executable binary may take several minutes to complete
depending on your hardware and operating system --with Linux typically outperforming other
platforms. When completed, the executable file will be available
under the `target` directory and be named after the artifact ID you have chosen during the
project generation phase.
