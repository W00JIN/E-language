var txt ="";
var last = "";
window.onload = function(){
  var a = document.getElementsByClassName("btn");
  for(var i=0;i<a.length;i++){
    a[i].onclick=btnClicked;
  }
  //document.getElementById("startbtn").onclick=getConsole;
  document.onkeydown = keydown;
}
function btnClicked(){
  if(this.id == "startbtn"){
    download('a.txt',txt);
    getConsole();
  }else if(this.id=="<-"){
    pushBack();
  }else if(this.id=="space"){
    txt+=" ";
    document.getElementById("text").innerHTML+="&nbsp;";
    last = "space";
  }else if(this.id =="tab"){
    txt+="    ";
    document.getElementById("text").innerHTML+="&nbsp;&nbsp;&nbsp;&nbsp;";
    last = "tab";
  }else if(this.id=="enter"){
    txt+="\n";
    document.getElementById("text").innerHTML+="<br>";
    last = "enter";
  }else{
    txt+=this.id;
    document.getElementById("text").innerHTML+=this.id;
    last = "";
  }
}
function keydown(e) {
    e = e || window.event;
    var num = parseInt(e.keyCode)
    if(num == 8){
      pushBack();
    }else if(num == 9 || num == 13 || num == 32){
      var input = document.getElementsByName(e.keyCode)[0];
      if(input.id =='tab'){
        txt+="    ";
        document.getElementById("text").innerHTML+="&nbsp;&nbsp;&nbsp;&nbsp;";
        last = "tab";
      }else if(input.id =='space'){
        txt+=" ";
        document.getElementById("text").innerHTML+="&nbsp;";
        last = "space";
      }else if(input.id =='enter'){
        txt+="\n";
        document.getElementById("text").innerHTML+="<br>";
        last = "enter";
      }
    }
    else{
      var input = document.getElementsByName(e.keyCode)[0];
      document.getElementById("text").innerHTML+=input.id;
      txt+=input.id;
      last = "";
    }
}

function pushBack(){
  var temp = document.getElementById("text").innerHTML;
  if(last == 'tab'){
    txt = txt.substring(0,txt.length-4);
    temp = temp.substring(0,temp.length-24);
  }else if(last == 'enter'){
    txt = txt.substring(0,txt.length-1);
    temp = temp.substring(0,temp.length-2);
  }else if(last == 'space'){
    txt = txt.substring(0,txt.length-1);
    temp = temp.substring(0,temp.length-6);
  }else{
    txt = txt.substring(0,txt.length-2);
    temp = temp.substring(0,temp.length-2);
  }
  document.getElementById("text").innerHTML = temp;
  last = "";
}
function download(filename, text) {
    var pom = document.createElement('a');
    pom.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
    pom.setAttribute('download', filename);

    if (document.createEvent) {
        var event = document.createEvent('MouseEvents');
        event.initEvent('click', true, true);
        pom.dispatchEvent(event);
    }
    else {
        pom.click();
        alert("download error");
    }
}
function getConsole() {
  new Ajax.Request("b.txt", {
    method: "get",
    onSuccess: printConsole,
    onFailure: ajaxFailure
  });
}
function ajaxFailure(ajax, exception) {
  alert("Error making Ajax request:" +"\n\nServer status:\n" + ajax.status + " " + ajax.statusText +"\n\nServer response text:\n" + ajax.responseText);
  if (exception) {
    throw exception;
  }
}
function printConsole(ajax){
  var temp = ajax.responseText.strip().split('\n');
  var console = "";
  temp.forEach(function(element) {
    console += element+"<br>";
  });
  document.getElementById("console").innerHTML = console;
}
