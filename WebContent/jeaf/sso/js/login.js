//function onLogining() //在登录,由继承者实现
function doLogin(formId) {
	var loginForm = formId ? document.getElementById(formId) : document.forms[0];
	if(loginForm.password.value=="") {
		alert("密码不能为空");
		loginForm.password.focus();
		return false;
	}
	var sessionId = document.cookie;
	var index = !sessionId ? -1 : sessionId.indexOf("JSESSIONID=");
	if(index==-1 && location.search) {
		sessionId = location.search.replace("&", ";");
		index = sessionId.indexOf("jsessionid=");
	}
	if(index==-1) {
		alert("您的浏览器不支持COOKIE,登录不能完成");
		return false;
	}
	sessionId = sessionId.substring(index + "JSESSIONID=".length);
	index = sessionId.indexOf(";");
	if(index!=-1) {
		sessionId = sessionId.substring(0, index);
	}
	loginForm.password.value = hex_md5(loginForm.password.value + sessionId);
	try {
		onLogining();
	}
	catch(e) {
	
	}
	FormUtils.submitForm(true, formId);
}
function initLoginForm(formId) {
	var form = document.getElementById(formId);
	var userName = DomUtils.getElement(form, 'input', 'userName');
	if(userName.value=='') {
		userName.focus();
	}
	else {
		DomUtils.getElement(form, 'input', 'password').focus();
	}
	form.onkeypress = function() {
		if(event.keyCode!=13) {
			return true;
		}
		var fieldNames = ['userName', 'password', 'validateCode'];
		var i = 0;
		for(; i < fieldNames.length; i++) {
			var field = DomUtils.getElement(this, 'input', fieldNames[i]);
			if(field && field.value=='') {
				field.focus();
				break;
			}
		}
		if(i==fieldNames.length) {
			doLogin(formId);
		}
		return false;
	}
}
function openChangePasswordDialog(changePasswordReason, formId) { //打开修改密码对话框
	window.onload = function() {
		var url = RequestUtils.getContextPath() + "/jeaf/sso/changePassword.shtml?prompt=" + StringUtils.utf8Encode(changePasswordReason);
		DialogUtils.openDialog(url, 430, 260, null, function() {
			window.location = DomUtils.getElement(document.getElementById(formId), 'input', 'redirect').value;
		});
	}
}