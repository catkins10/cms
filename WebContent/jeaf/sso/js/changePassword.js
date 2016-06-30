function formOnSubmit() {
	if(document.getElementsByName("oldPassword")[0].value=='') {
		alert("请输入原密码！");
		document.getElementsByName("oldPassword")[0].focus();
		return false;
	}
	if(document.getElementsByName("newPassword")[0].value=='') {
		alert("请输入新密码！");
		document.getElementsByName("newPassword")[0].focus();
		return false;
	}
	if(document.getElementsByName("newPassword")[0].value!=document.getElementsByName("newPassword")[1].value) {
		alert("密码不一致！");
		document.getElementsByName("newPassword")[1].focus();
		return false;
	}
	var passwordStrength = Number(document.getElementsByName("passwordStrength")[0].value);
	if(passwordStrength>1 && getPasswordStrength(document.getElementsByName("newPassword")[0].value)<passwordStrength) {
		alert("密码安全等级不够！");
		document.getElementsByName("newPassword")[0].focus();
		return false;
	}
	return true;
}