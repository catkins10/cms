function formOnSubmit() { 
	 var name = document.getElementsByName("name")[0].value;
	 var no = document.getElementsByName("idCard")[0].value;
	 if(name==null || name==''){
	 	alert("请填写姓名");
	 	return false;
	 }
	 if(no==null || no==''){
	 	alert("请填写身份证号");
	 	return false;
	 }
	return true;
}