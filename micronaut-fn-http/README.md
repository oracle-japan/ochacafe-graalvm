# Micronaut native-image FaaS API Server

## ビルド手順

```bash
# テスト (instance principal を使用するので、OCIの設定が必要)
$ mvn clean test -Doci.config.instance-principal.enabled=true

# デプロイ (OCIRにイメージをpushするところまで)
$ mvn deploy -Dpackaging=docker-native
```

---

## 参考 ドキュメンテーション

## Feature oracle-cloud-sdk documentation

- [Micronaut Oracle Cloud SDK documentation](https://micronaut-projects.github.io/micronaut-oracle-cloud/latest/guide/)

- [https://docs.cloud.oracle.com/en-us/iaas/Content/API/SDKDocs/javasdk.htm](https://docs.cloud.oracle.com/en-us/iaas/Content/API/SDKDocs/javasdk.htm)


## Feature oracle-function-http documentation

- [Micronaut Oracle Function documentation](https://micronaut-projects.github.io/micronaut-oracle-cloud/latest/guide/#httpFunctions)


## Feature oracle-function documentation

- [Micronaut Oracle Function documentation](https://micronaut-projects.github.io/micronaut-oracle-cloud/latest/guide/#functions)

- [https://docs.cloud.oracle.com/iaas/Content/Functions/Concepts/functionsoverview.htm](https://docs.cloud.oracle.com/iaas/Content/Functions/Concepts/functionsoverview.htm)


