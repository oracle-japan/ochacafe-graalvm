# OCI Functions でネイティブ・イメージを動かす

+ maven-surefire-plugin の `<argLine>` に `-agentlib:...` を仕込んでテスト実施時にGraalVMネイティブイメージ構成ファイルを出力する
+ access-filter.json で構成ファイルから不要な情報（JUnit系）をフィルタする
+ Dockerfile でマルチフェーズのビルドを行う

データベースのパスワードは、リソース・プリンシパルを使って OCI Secret から取得しています。  
したがって実際にこのアプリケーションを実行させるには Dynamic Group・Policy・Secret 等の設定が必要です。

JPAに必要なデータベース関連設定情報は、func.yaml に定義されています。

+ 2022-12-20
  + Wallet ファイルのダウンロードに対応
  + Eclipselink をバージョンアップ
  + OCI Java SDK をバージョンアップ

## 事前準備

mvnw が実行できるようにします。

```bash
mvn wrapper:wrapper
```

Fn CLI ツールをダウンロードします。
```bash
curl -LSs https://raw.githubusercontent.com/fnproject/cli/master/install | sh
```

下記のドキュメントを参照して、fn コマンドが正常に動作するようにします。
https://docs.oracle.com/ja-jp/iaas/Content/Functions/Tasks/functionsconfiguringclient.htm


## ビルド＆デプロイ

```bash
# テスト - ネイティブ・イメージ構成情報は以下のディレクトリに作成される
# target/native/agent-output/test/${project.groupId}/${project.artifactId}
mvn test

# 構成情報は src/main/resources/META-INF/native-image 配下に配置する

# ビルド
fn build

# デプロイ
fn deploy --app <application name>
```