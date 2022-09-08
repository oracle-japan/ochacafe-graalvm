const restify = require('restify');
const oracledb = require('oracledb');
const common = require("oci-common");
const secrets = require("oci-secrets");

const dbConfig = require('./dbconfig.js');
const ociConfig = require('./ociconfig.js');


let dbPool;

// Oracle データベースへ接続して、クエリー結果を返す
async function findCountry(req, res, next) {
  console.log('finding country: %s', req.params.id)
  res.setHeader('content-type', 'text/plain');

  var conn = await oracledb.getConnection(dbPool.poolAlias);
  conn.execute(`SELECT COUNTRY_NAME FROM COUNTRY WHERE COUNTRY_ID = :id`, 
      [req.params.id], 
      { outFormat: oracledb.OBJECT }
  ).then((result) => {
    console.log(result)
    if(0 === result.rows.length) {
      res.send(404, 'not found.\n');
      next(false);
    }else{
      res.send(result.rows[0].COUNTRY_NAME + "\n");
      next();
    }
  }).finally(() => {
    conn.close();
  });
}

// Javaクラスにアクセス - 普通のnode.jsだとエラー "ReferenceError: Java is not defined"
function hello(req, res, next) {
  res.setHeader('content-type', 'text/plain');
  try{
    var javaString = Java.type("java.lang.String");
    var world = new javaString("world"); // Javaオブジェクトのインスタンス化
    var message = javaString.format("Hello %s!\n", req.params.name || world); // static メソッドの呼び出し
    res.send(message);
    next();
  }catch(err){
    console.log(err);
    res.send(500, 'not supported.\n');
    next(false);
  }
}

// Secretに保管されているDBパスワードを取り出す
async function getSecret(secretId){
  const provider = new common.ConfigFileAuthenticationDetailsProvider();
  const client = new secrets.SecretsClient({
    authenticationDetailsProvider: provider
  });
  const getSecretBundleRequest = {
    secretId: secretId
  };
  var response = await client.getSecretBundle(getSecretBundleRequest)
  return Buffer.from(response.secretBundle.secretBundleContent.content, 'base64').toString();
}

async function run() {
  // Connection Poolの作成
  const pwContent = await getSecret(ociConfig.pwSecretId);
  dbConfig.password = pwContent;
  dbPool = await oracledb.createPool(dbConfig);
  console.log("connection pool is ready");

  // RESTify サーバーの起動
  var server = restify.createServer();
  server.get('/country/:id', findCountry);
  server.get('/hello/:name', hello);
  server.listen(8080, function () {
    console.log('%s listening at %s', server.name, server.url);
  });
}

function terminate(){
  if(dbPool){
    console.log("\nclosing database connection pool...");
    dbPool.close((err) => {
      console.log("bye.");
      process.exit(0);
    });
  }
}

process
  .on("SIGINT", terminate)
  .on("SIGTERM", terminate);

run();