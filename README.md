
# Oracle Cloud Handout Cafe 6 #2 「GraalVM 最新事情」のデモ・ソース

デモ毎にディレクトリが分かれています。

```
.
├── helidon-jpa [ Helidon MP ネイティブ・イメージ ]
├── java-app [ GraalVMによるJavaアプリケーションの実行 ]
├── micronaut-fn-http [ Micronaut native-image FaaS API Server ]
├── node-app [ Node.js on GraalVM ]
├── oci-functions-jpa [ OCI Functions でネイティブ・イメージを動かす ]
└── reflection [ ネイティブ・イメージを作成する ]
```

デモの実行にはGraalVM が必要です。  
https://www.graalvm.org/downloads/
からダウンロードして下さい。  

$JAVA_HOME 環境変数が GraalVM のインストール・ディレクトリにセットされていることを前提にしています。
