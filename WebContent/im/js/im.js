//会话错误
function onSessionFailed() {
	confirm("请重新登录。");
}
//定时调用keepAlive.shtml以延续会话
function keepAlive() {
	var keepAliveFrame = document.getElementById("keepAliveFrame");
	if(!keepAliveFrame) {
		keepAliveFrame = document.createElement("iframe");
		keepAliveFrame.id = "keepAliveFrame";
		keepAliveFrame.style.display = "none";
		document.body.appendChild(keepAliveFrame);
	}
	keepAliveFrame.src = "keepAlive.shtml?seq=" + Math.random();
}
window.setInterval("keepAlive()", 8*60000); //8分钟一次