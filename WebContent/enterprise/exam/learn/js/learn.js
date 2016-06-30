function updateLearnTime() { //更新学习时间
	var ajax = new Ajax();
	ajax.openRequest("GET", location.href.replace('learnMission.shtml', 'updateLearnTime.shtml') + "&seq=" + Math.round(Math.random() * 1000000000), "", false, null, null);
	if(ajax.getResponseText()=="NOSESSION") {
		LoginUtils.openLoginDialog();
	}
}
window.onload = function() {
	window.setInterval(updateLearnTime, 10000); //每10秒更新一次
	document.body.onunload = updateLearnTime; //窗口关闭时,更新一次
};