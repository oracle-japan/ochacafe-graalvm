# Node.js on GraalVM

### 事前準備

+ dbconfig.js の編集
+ ociconfig.js の編集
+ node パッケージのインストール

    ```
    npm install
    ```

## 実行方法

Queryサーバーの実行

```
$JAVA_HOME/bin/node --jvm dbserver.js
```

curl で確認

```
curl http://localhost:8080/country/1
USA

curl http://localhost:8080/country/81
Japan

# --jvm オプションをつけないと、この操作はエラーになる
curl http://localhost:8080/hello/
Hello world!
```

Insight をアタッチして実行

```
$JAVA_HOME/bin/node \
    --experimental-options \
    --insight=function-tracing.js \
    --js.print \
    dbserver.js 
```

VisualVM をリモートからアタッチするために JMX オプションを付加して実行

```
$JAVA_HOME/bin/node --jvm --polyglot \
    --vm.Dcom.sun.management.jmxremote.port=9010 \
    --vm.Dcom.sun.management.jmxremote.ssl=false \
    --vm.Dcom.sun.management.jmxremote.authenticate=false \
    --vm.Dcom.sun.management.jmxremote.rmi.port=9011 \
    --vm.Djava.rmi.server.hostname=localhost \
    --vm.Dcom.sun.management.jmxremote.local.only=false \
   dbserver.js
```
