# CloudFront/S3を使ってテナントUIを試す

ヘッダー部分をテナント毎にUIを異なるものにする例の検証コード。

## 構築
テナントUIのアセットをhttps配信するためにはRoute53->S3では無理なため、Route53->CloudFront->S3とする。

- アセット配信用のS3バケットの作成
- ACMで証明書作成(バージニア)
- CloudFrontの作成
  - Restrict Bucket Access: Yes
  - Grant Read Permissions on Bucket: Yes
- Route53にAレコード設定(CloudFrontのエイリアス)

## 実装
### HTMLテンプレート
src/main/resources/templates/hello.html のようにテナント毎にデザインをカスタムするため、link, scriptタグを以下のように構成する。
S3バケット内でテナント毎にフォルダを切り、CSS, JS, イメージファイルをサブドメインにより切り替える。

```html
<!doctype html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8" />
    <title>Hello</title>
    <link rel="stylesheet" th:href="@{/css/main.css}" />
    <link rel="stylesheet" th:href="@{https://assets.cm.shoito.dev/{tenant}/css/style.css(tenant=${tenant})}" />
    <script th:src="@{https://assets.cm.shoito.dev/{tenant}/js/tenant.js(tenant=${tenant})}"></script>
</head>
<body>
<header id="tenant-header"><!-- テナント毎ヘッダー --></header>
<h1 th:text="${message}"></h1>
<p>共通部分</p>
<footer id="tenant-footer"><!-- テナント毎フッター --></footer>
</body>
</html>
```

### S3バケットの構成
- assets.DOMAIN_NAME
    - TENANT_NAME_A
        - css/style.css
        - js/tenant.js
        - img/favicon.ico
        - ...
    - TENANT_NAME_B
        - css/style.css
        - js/tenant.js
        - img/favicon.ico
        - ...

## 確認方法

1. `/etc/hosts` にテナント分のサブドメインを設定する
    ```shell
    127.0.0.1 tenant-a.local
    127.0.0.1 tenant-b.local
    ...
    ```   

1. プログラムを実行する
    ```shell
    ./gradlew bootRun
    ```

1. ブラウザから各テナントのURLにアクセスする
    `http://tenant-a.local:8080/hello`
    `http://tenant-b.local:8080/hello`
