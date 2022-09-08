const restify = require('restify');

function hello(req, res, next) {
  try{
    var message = "Hello " + (req.params.name || "world") + "!";
    res.setHeader('content-type', 'text/plain');
    res.send(message);
    next();
  }catch(err){
    console.log(err);
    res.send(500, err);
    next(false);
  }
}

var server = restify.createServer();
server.get('/hello/:name', hello);

server.listen(8080, function() {
  console.log('%s listening at %s', server.name, server.url);
});

function terminate(){
  print('\nbye.')
  process.exit(0);
}

process
  .on("SIGINT", terminate)
  .on("SIGTERM", terminate);