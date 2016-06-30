LoginUtils = function() {

};
//打开登录对话框,适用于文件上传和下载等场合
LoginUtils.openLoginDialog = function(callback) { //callback = function();
	DialogUtils.openDialog(RequestUtils.getContextPath() + "/jeaf/sessionmanage/resetSession.shtml", 350, 180, "登录", callback);
};
//注销
LoginUtils.logout = function(redirect, external) {
	if(!redirect) {
		redirect = top.location.href;
	}
	else if(redirect.indexOf("http://")==-1 && redirect.indexOf("https://")==-1) {
		redirect = top.location.protocol + "//" + location.host + redirect;
	}
	window.top.location = RequestUtils.getContextPath() + '/jeaf/sessionmanage/logout.shtml?' + (external ? 'external=true&' : '') + 'redirect=' + StringUtils.utf8Encode(redirect);
};