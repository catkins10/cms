var webimServerPath; //webim服务器路径
var currentPageServerPath; //当前页面服务器路径
var fixedPositionSupport = true; //是否支持固定位置
var blankPageURL = "/blank.html"; //当前页面服务器上的空白页URL，如果不是这个,可以在<script id="scriptWebim"...后加上blankPageURL="..."调整
var webimDialogs = new Array(); //webim使用的div列表
var webimAlign = "right_bottom"; //对齐方式:left_top,left_bottom,right_top,right_borrom,center
var horizontalMargin = 10; //水平边距
var verticalMargin = 0; //垂直边距
var zIndex = 1000000;

//加载Webim
function loadWebim() {
	if(top.location.href.indexOf('emplate.shtml')!=-1) { //模板配置
		return;
	}
	//检查当前页面和Webim是否在同一台服务
	var isCustomerService = false;
	var scriptWebim = document.getElementById("scriptWebim");
	webimServerPath = scriptWebim.src.substring(0, scriptWebim.src.indexOf("/im/webim")); //webim服务器路径
	currentPageServerPath = location.href.substring(0, location.href.indexOf("/", location.href.indexOf("://")+3)); //当前页面服务器路径
	isCustomerService = "true"==StringUtils.getPropertyValue(scriptWebim.src, "customerService"); //是否客服
	webimAlign = StringUtils.getPropertyValue(scriptWebim.src, "webimAlign", "right_bottom"); //对齐方式
	horizontalMargin = Number(StringUtils.getPropertyValue(scriptWebim.src, "horizontalMargin", "10")); //水平边距
	verticalMargin = Number(StringUtils.getPropertyValue(scriptWebim.src, "verticalMargin", "0")); //垂直边距
	//创建DIV,显示Webim
	var divWebim = document.createElement("div");
	divWebim.style.position = 'fixed';
	divWebim.id = 'divWebim';
	divWebim.style.zIndex = zIndex;
	divWebim.style.left = "100px";
	divWebim.style.top = "100px";
	divWebim.style.width = "1px";
	divWebim.style.height = "1px";
	divWebim.style.visibility = 'hidden';
	divWebim.style.backgroundColor = "transparent";
	document.body.insertBefore(divWebim, document.body.childNodes[0]);
	if(divWebim.offsetTop!=100 || divWebim.offsetLeft!=100) { //不支持固定位置
		divWebim.style.position = 'absolute';
		fixedPositionSupport = false
	}
	//检查宿主页面主机和webim主机是否相同,如果不同,创建IFRAME,用来给宿主窗口发送通知
	if(scriptWebim.src.substring(0, 1)!="/" && scriptWebim.src.split("/")[2].split(":")[0]!=location.hostname) {
		var frameNotify = document.createElement(document.all ? '<iframe onload="onWebimNotify()"></iframe>' : 'iframe');
		frameNotify.id = frameNotify.name = "frameNotify";
		if(!document.all) {
			frameNotify.setAttribute("onload", "onWebimNotify()");
		}
		frameNotify.style.display = 'none';
		frameNotify.src = webimServerPath + "/blank.html";
		divWebim.appendChild(frameNotify);
	}
	//创建IFRAME,加载Webim页面
	var frameWebim = document.createElement('iframe');
	frameWebim.id = frameWebim.name = "frameWebim";
	frameWebim.frameBorder = 0;
	frameWebim.scrolling = "no";
	frameWebim.style.width = "100%";
	frameWebim.style.height = "100%";
	frameWebim.allowTransparency = true;
	var url = webimServerPath + "/im/webim/webim.shtml?";
	if(isCustomerService) {
		url += scriptWebim.src.substring(scriptWebim.src.indexOf('?')+1) + "&";
	}
	url += "notifyURL=" + currentPageServerPath + blankPageURL;
	frameWebim.src = url;
	divWebim.appendChild(frameWebim);
}
//IM发送消息给宿主窗口
function onWebimNotify() {
	try {
		var href = window.frames["frameNotify"].location.href;
		if(href) {
			window.frames["frameNotify"].location.replace(webimServerPath + "/blank.html"); //重新打开Webim服务上的空白页面,以便处理下一次的通知
			processWebimNotify(href.substring(href.indexOf("#")+1)); //处理接收到的消息
		}
	}
	catch(e) {
	
	}
}
//处理接收到的消息
function processWebimNotify(notifyContent) {
	var parameters = notifyContent.split("$$");
	if("showWebim"==parameters[0]) { //调整主帧尺寸
		showWebim(Number(parameters[1]), Number(parameters[2]));
	}
	if("closeWebim"==parameters[0]) { //关闭IM
		closeWebim();
	}
	else if("openWebimDialog"==parameters[0]) { //显示一个对话框,openWebimDialog(url, left, top, width, height, align)
		openWebimDialog(parameters[1], Number(parameters[2]), Number(parameters[3]), Number(parameters[4]), Number(parameters[5]), parameters[6]);
	}
	else if("closeWebimDialog"==parameters[0]) { //关闭一个对话框
		closeWebimDialog(parameters[1], parameters[2]=="true");
	}
}
//显示和调整主窗口尺寸
function showWebim(width, height) {
	var divWebim = document.getElementById("divWebim");
	//调整显示位置,避免出现滚动条
	divWebim.style.left = (divWebim.offsetLeft - (width - divWebim.offsetWidth)) + "px";
	divWebim.style.top = (divWebim.offsetTop - (height - divWebim.offsetHeight)) + "px";
	//调整尺寸
	document.getElementById("frameWebim").style.width = divWebim.style.width = width + "px";
	document.getElementById("frameWebim").style.height = divWebim.style.height = height + "px";
	adjustWebimPosition(true, false);
	divWebim.style.visibility = 'visible';
}
//关闭IM
function closeWebim() {
	var divWebim = document.getElementById("divWebim");
	divWebim.parentNode.removeChild(divWebim);
}
//显示一个对话框
function openWebimDialog(url, left, top, width, height, align) {
	//检查对话框是否已经存在
	var divDialog = document.getElementById("webim_" + url);
	var divWebim = document.getElementById("divWebim");
	if(!divDialog) {
		//创建DIV,显示Webim
		divDialog = document.createElement("div");
		divDialog.style.position = (fixedPositionSupport ? 'fixed' : 'absolute');
		divDialog.id = "webim_" + url;
		divDialog.style.backgroundColor = "transparent";
		document.body.insertBefore(divDialog, document.body.childNodes[0]);
		//创建IFRAME,加载Webim页面
		var frameDialog = document.createElement('iframe');
		frameDialog.frameBorder = 0;
		frameDialog.scrolling = "no";
		frameDialog.allowTransparency = true;
		frameDialog.id = frameDialog.name = "frame_" + url;
		frameDialog.src = webimServerPath + url;
		divDialog.appendChild(frameDialog);
		//添加到对话框列表
		webimDialogs[webimDialogs.length] = url;
	}
	if(align=="center") {
		divDialog.align = "center";
	}
	else {
		divDialog.removeAttribute("align");
	}
	//显示对话框
	divDialog.style.zIndex = zIndex + (align=="center" ? 1 : -1);
	divDialog.style.display = "block";
	divDialog.childNodes[0].style.display = "block";
	//设置窗口位置和尺寸
	width = Math.min(width, DomUtils.getClientWidth(document) - 6);
	height = Math.min(height, (align=="center" ? DomUtils.getClientHeight(document)-3 : DomUtils.getClientHeight(document) - divWebim.offsetHeight - verticalMargin - 3));
	if(align=="center") {
		divDialog.style.left = ((fixedPositionSupport ? 0 : document.body.scrollLeft) + (DomUtils.getClientWidth(document) - width) / 2) + "px";
		divDialog.style.top = ((fixedPositionSupport ? 0 : document.body.scrollTop) + (DomUtils.getClientHeight(document) - height) / 2 + 1) + "px";
	}
	else {
		divDialog.style.left = Math.max(3, Math.min(divWebim.offsetLeft + divWebim.offsetWidth - width, divWebim.offsetLeft + left)) + "px";
		divDialog.style.top = (divWebim.offsetTop - height + top + 1) + "px";
	}
	var frameDialog = divDialog.childNodes[0];
	frameDialog.style.width = divDialog.style.width =  width + "px";
	frameDialog.style.height = divDialog.style.height = height + "px";
}
//关闭一个对话框
function closeWebimDialog(url, hideOnly) {
	var divDialog = document.getElementById("webim_" + url);
	if(!divDialog) {
		return;
	}
	if(hideOnly) { //只隐藏
		divDialog.style.display = "none";
		divDialog.childNodes[0].style.display = "none";
		divDialog.style.zIndex = -1;
	}
	else { //删除对话框
		divDialog.parentNode.removeChild(divDialog);
		for(var i=0; i<webimDialogs.length; i++) {
			if(webimDialogs[i]==url) {
				webimDialogs.splice(i, 1);
				break;
			}
		}
	}
}
//当前窗口尺寸改变
function onCurrentWindowResize() {
	adjustWebimPosition(true, true);
}
//当前窗口滚动
function onCurrentWindowScroll() {
	adjustWebimPosition(false, true);
}
//调整窗口位置
function adjustWebimPosition(force, adjustDialog) {
	if(fixedPositionSupport && !force) { //支持固定位置,且不是调整尺寸
		return;
	}
	//调整主窗口位置
	var divWebim = document.getElementById("divWebim");
	if(!divWebim) {
		return;
	}
	var oldLeft = divWebim.offsetLeft;
	var oldTop = divWebim.offsetTop;
	var left = (fixedPositionSupport ? 0 : document.body.scrollLeft);
	var top = (fixedPositionSupport ? 0 : document.body.scrollTop);
	//根据对齐方式方式(left_top,left_bottom,right_top,right_borrom,center)决定显示位置
	if(webimAlign=="right_bottom" || webimAlign=="right_top") {
		left = left - Number(document.body.style.marginRight.replace("px", "")) - Number(document.body.style.paddingRight.replace("px", "")) + DomUtils.getClientWidth(document) - divWebim.offsetWidth - horizontalMargin;
	}
	else if(webimAlign=="left_bottom" || webimAlign=="left_top") {
		left += horizontalMargin;
	}
	else if(webimAlign=="center") {
		left += (DomUtils.getClientWidth(document) - divWebim.offsetWidth) / 2;
	}
	
	if(webimAlign=="right_bottom" || webimAlign=="left_bottom") {
		top += DomUtils.getClientHeight(document) - divWebim.offsetHeight - verticalMargin;
	}
	else if(webimAlign=="right_top" || webimAlign=="left_top") {
		top += verticalMargin;
	}
	else if(webimAlign=="center") {
		top += (DomUtils.getClientHeight(document) - divWebim.offsetHeight) / 2;
	}
	divWebim.style.left = left + "px";
	divWebim.style.top = top + "px";
	if(adjustDialog) {
		//调整对话框位置
		for(var i=0; i<webimDialogs.length; i++) {
			var divDialog = document.getElementById("webim_" + webimDialogs[i]);
			if(divDialog.align=="center") { //居中模式
				divDialog.style.left = ((fixedPositionSupport ? 0 : document.body.scrollLeft) + (DomUtils.getClientWidth(document) - divDialog.offsetWidth) / 2) + "px";
				divDialog.style.top = ((fixedPositionSupport ? 0 : document.body.scrollTop) + (DomUtils.getClientHeight(document) - divDialog.offsetHeight) / 2)+ "px";
			}
			else {
				divDialog.style.left = (Number(divDialog.style.left.replace("px", "")) - oldLeft + divWebim.offsetLeft) + "px";
				divDialog.style.top = (Number(divDialog.style.top.replace("px", "")) - oldTop + divWebim.offsetTop) + "px";
			}
		}
	}
}
//绑定事件
if(window.attachEvent) { //IE
	window.attachEvent('onload', loadWebim);
	window.attachEvent('onresize', onCurrentWindowResize);
	window.attachEvent('onscroll', onCurrentWindowScroll);
}
else { //w3c
	window.addEventListener("load", loadWebim, false);
	window.addEventListener("resize", onCurrentWindowResize, false);
	window.addEventListener("scroll", onCurrentWindowScroll, false);
}