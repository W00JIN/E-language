var express = require('express')
var app = express()
app.listen(8080, function(){
  console.log("this line will be at the end")
})

app.get('/', function(req,res){
  console.log(req.body);
  res.send("<h1>안녕하세요</h1>")
  //res.sendFile( __dirname + "/public/main.html")
})




/*var http = require('http');
var querystring = require('querystring');
var Iconv1 = require('iconv').Iconv;

var server = http.createServer(function(request,response){
  // 1. post로 전달된 데이터를 담을 변수를 선언
  var postdata = '';
  var decodedUrl = '';
  var str = [];
  // 2. request객체에 on( ) 함수로 'data' 이벤트를 연결
  request.on('data', function (data) {
    // 3. data 이벤트가 발생할 때마다 callback을 통해 postdata 변수에 값을 저장
    console.log('\nconnected\n');
    var qs = require('querystring');
    postdata = postdata + data;
    decodedUrl = qs.unescape(postdata);
    str = decodedUrl.split("=");

    //iconv = new Iconv1('UTF-16BE','ISO-8859-1//TRANSLIT');
    //var str = iconv.convert(decodedUrl).toString();
    //sss = str
    console.log('\n'+decodedUrl+'\n');
  });

  // 4. request객체에 on( ) 함수로 'end' 이벤트를 연결
  request.on('end', function () {
    // 5. end 이벤트가 발생하면(end는 한버만 발생한다) 3번에서 저장해둔 postdata 를 querystring 으로 객체화
    var parsedQuery = querystring.parse(postdata);
    // 6. 객체화된 데이터를 로그로 출력
    //console.log(parsedQuery+'\n');
    // 7. 성공 HEADER 와 데이터를 담아서 클라이언트에 응답처리
    response.writeHead(200, {'Content-Type':'text/html'});
    response.end(str[1]);
  });

});
server.listen(8080, function(){
    console.log('Server is running...');
});
*/
