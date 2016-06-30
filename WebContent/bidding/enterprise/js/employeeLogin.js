var lastKeyError;
var loginName = '';
function biddingLogin() {
	readKey();
	if(loginName=='') {
		alert(lastKeyError);
		return;
	}
	var form = document.forms['bidding/enterprise/employeeLogin'];
	var keyReader = document.getElementById("KeyReader");
	form.action += '?companyId=' + keyReader.getCompanyId() + '&userId=' + keyReader.getUserId() + '&keyId=' + keyReader.getKeyId();
	doLogin();
}
function readKey() {
	if(loginName!='') {
		return loginName;
	}
	var html = document.getElementById("KeyReader").outerHTML;
	document.getElementById("KeyReader").outerHTML = '<a id="tempObject"></a>';
	document.getElementById("tempObject").outerHTML = html;
	loginName = document.getElementById("KeyReader").getUserLoginName();
	if(loginName!='') {
		document.getElementsByName("userName")[0].value = loginName;
	}
	else {
		window.setTimeout('readKey()', 2000);
	}
}
function onReadError(error) {
	lastKeyError = error;
}
if(location.href.indexOf('login.shtml')==-1) {
	window.setTimeout('readKey()', 5000);
}
else {
	window.setTimeout('readKey()', 100);
}