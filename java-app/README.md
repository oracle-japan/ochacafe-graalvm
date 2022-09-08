# GraalVMによるJavaアプリケーションの実行

```bash
# jar ファイルの作成
mvn package -DskipTests=true

# GraalVMで実行 – 通常の実行方法
java –cp target/demo-1.0.jar com.example.App

# Graal JIT コンパイラを敢えて無効にして実行する
java -XX:-UseJVMCICompiler -cp target/demo-1.0.jar com.example.App

# Java on Truffle (Truffle上のJVMバイトコード・インタプリタ) で実行する
java -truffle -cp target/demo-1.0.jar com.example.App

# Java on Truffle は単一のファイルであればソースファイルも実行できる！
java -truffle src/main/java/com/example/App.java 

# Java on Truffle で異なるJavaのインストール先にある実行モジュール(=jarとライブラリ)を使用して実行する – バージョンが違ってもOK！
java -truffle --java.JavaHome=$ANOTHER_JAVA_HOME -cp target/demo-1.0.jar com.example.App

# ネイティブ・イメージの作成 (Maven native-maven-plugin を利用) と実行
mvn -Pnative package -DskipTests=true
./target/demo

# Javascript コードから Javaクラスにアクセス
$JAVA_HOME/bin/js --jvm -e 'Java.type("java.lang.System").out.println("Hello World!")'
```

その他

```bash
# ネイティブ・イメージでのテストも実行してからネイティブ・イメージを作成する
mvn -Pnative package

# 作成されたテスト用のネイティブ・イメージを実行する
target/native-tests
JUnit Platform on Native Image - report
----------------------------------------

com.example.AppTest > shouldAnswerWithTrue() SUCCESSFUL


Test run finished after 2 ms
[         2 containers found      ]
[         0 containers skipped    ]
[         2 containers started    ]
[         0 containers aborted    ]
[         2 containers successful ]
[         0 containers failed     ]
[         1 tests found           ]
[         0 tests skipped         ]
[         1 tests started         ]
[         0 tests aborted         ]
[         1 tests successful      ]
[         0 tests failed          ]

# Maven native-maven-plugin を使わずにネイティブ・イメージを作成する
$JAVA_HOME/bin/native-image -cp target/demo-1.0-SNAPSHOT.jar com.example.App

# com.example.app という名前のネイティブ・イメージがカレント・ディレクトリに作られる
./com.example.app 
```

