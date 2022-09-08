# ネイティブ・イメージを作成する

```bash
# まず通常通りコンパイル
mvn clean compile

# 実行
$JAVA_HOME/bin/java -cp target/classes com.example.App com.example.Hello say

# ネイティブ・イメージを作成
$JAVA_HOME/bin/native-image --no-fallback -cp target/classes com.example.App

# ネイティブ・イメージを実行するがエラー
./com.example.app com.example.Hello say
Exception in thread "main" java.lang.ClassNotFoundException: com.example.Hello
        at java.lang.Class.forName(DynamicHub.java:1121)
        at java.lang.Class.forName(DynamicHub.java:1097)
        at com.example.App.main(App.java:9)

# トレース・エージェントを動かして設定ファイルを出力する
$JAVA_HOME/bin/java \
  -agentlib:native-image-agent=config-output-dir=target/native-image \
  -cp target/classes com.example.App com.example.Hello say

# ネイティブ・イメージを再作成 - オプション1: 設定ファイルを指定
$JAVA_HOME/bin/native-image \
  -H:ReflectionConfigurationFiles=target/native-image/reflect-config.json \
  --no-fallback -cp target/classes com.example.App

# 動作確認
./com.example.app com.example.Hello say

# ネイティブ・イメージを再作成 - オプション2: Feature 実装クラスを指定
$JAVA_HOME/bin/native-image \
  --features=com.example.RuntimeReflectionRegistrationFeature \
  --no-fallback -cp target/classes com.example.App

# 動作確認
./com.example.app com.example.Hello say
Hello World!
```

トレース・エージェント実行で自動生成された reflect-config.json を確認

```json
[
{
  "name":"com.example.Hello",
  "methods":[
    {"name":"<init>","parameterTypes":[] }, 
    {"name":"say","parameterTypes":[] }
  ]
}
]
```
