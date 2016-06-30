function doRegist() { //注册用户
	var passwords = document.getElementsByName("password");
	for(var i=1; i<passwords.length; i++) {
		if(passwords[0].value!=passwords[i].value) {
			alert('密码不一致，注册不能完成');
			return false;
		}
	}
	FormUtils.submitForm(false, 'lss/insurance/registUser');
}