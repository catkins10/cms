function beforSubmit(){
  var idCode=document.getElementsByName("securityNumber")[0].value;
  var name=document.getElementsByName("name")[0].value;
  if(idCode==""){
   alert("身份证号不能为空");
   return false;
  }
  var form=document.getElementsByName("lss/cardquery/cardQuery")[0];
  form.submit();
}
