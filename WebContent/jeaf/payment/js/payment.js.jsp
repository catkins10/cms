<%@ page contentType="text/javascript; charset=UTF-8" %>

//function onPaymentComplete(paymentId, paymentSuccess) //支付完成方法,由调用者实现

var PAYMENT_WIDTH = 800; //支付界面宽度
var PAYMENT_HEIGHT = 500; //支付界面高度
var PAYMENT_POPUP_SUPPORT = true; //是否允许弹出页面方式

var paymentDialog, paymentTimer, cover;
/**
  * 打开支付界面,在根目录放置空文件blank.html
  * payerId 支付人ID
  * payerName 支付人姓名
  * applicationName 应用名称
  * money 支付金额
  * providerId 服务提供者ID,0表示系统
  * providerName 服务提供者姓名
  * providerMoney 服务提供者应得的金额
  */
function redirectToPayment(applicationName, orderId, payerId, payerName, paymentReason, money, providerId, providerName, providerMoney, redirectOfSuccess, redirectOfFailed) {
   	var url = "<%=com.yuanluesoft.jeaf.util.Environment.getWebApplicationSafeUrl()%>/jeaf/payment/payment.shtml" +
			  "?pageMode=redirect" +
			  "&applicationName=" + StringUtils.utf8Encode(applicationName) +
			  "&orderId=" + orderId +
			  "&payerId=" + payerId +
			  "&payerName=" + StringUtils.utf8Encode(payerName) +
			  "&paymentReason=" + StringUtils.utf8Encode(paymentReason) +
			  "&money=" + money +
			  "&providerId=" + providerId +
			  "&providerName=" + StringUtils.utf8Encode(providerName) +
			  "&providerMoney=" + providerMoney +
			  "&redirectOfSuccess=" + StringUtils.utf8Encode(redirectOfSuccess) +
			  "&redirectOfFailed=" + StringUtils.utf8Encode(redirectOfFailed);
	window.location.href = url;
}
/**
  * 打开支付界面,在根目录放置空文件blank.html
  * payerId 支付人ID
  * payerName 支付人姓名
  * applicationName 应用名称
  * money 支付金额
  * providerId 服务提供者ID,0表示系统
  * providerName 服务提供者姓名
  * providerMoney 服务提供者应得的金额
  */
function openPayment(applicationName, orderId, payerId, payerName, paymentReason, money, providerId, providerName, providerMoney) {
	cover = createCover(window, 30);
	var topWindow = getTopWindow();
	//检查宽度和高度,是否满足要求,满足则采用内嵌方式,否则采用弹出新窗口方式
	var mode = PAYMENT_POPUP_SUPPORT && topWindow.document.body.clientWidth >= PAYMENT_WIDTH && topWindow.document.body.clientHeight >= PAYMENT_HEIGHT ? 'popup' : 'dialog';
   	var url = "<%=com.yuanluesoft.jeaf.util.Environment.getWebApplicationSafeUrl()%>/jeaf/payment/payment.shtml" +
			  "?pageMode=" + mode +
			  "&applicationName=" + StringUtils.utf8Encode(applicationName) +
			  "&orderId=" + orderId +
			  "&payerId=" + payerId +
			  "&payerName=" + StringUtils.utf8Encode(payerName) +
			  "&paymentReason=" + StringUtils.utf8Encode(paymentReason) +
			  "&money=" + money +
			  "&providerId=" + providerId +
			  "&providerName=" + StringUtils.utf8Encode(providerName) +
			  "&providerMoney=" + providerMoney +
			  "&from=" + StringUtils.utf8Encode(window.location.href);
	if(mode=='dialog') {
		var left =(screen.width - PAYMENT_WIDTH)/2;
	   	var top =(screen.height - PAYMENT_HEIGHT - 16)/2;
	   	paymentDialog = window.open(url, "payment", "left=" + left + "px,top=" + top + "px,width=" + PAYMENT_WIDTH + "px,height=" + PAYMENT_HEIGHT + "px,status=0,help=0,scrollbars=1,resizable=1", true);
	   	paymentDialog.focus();
	}
	else {
		var top  = Math.max((cover.offsetHeight - PAYMENT_HEIGHT - 20) / 2, 0);
		var left = Math.max((cover.offsetWidth - PAYMENT_WIDTH - 20)  / 2, 0);
		//创建iframe,加载dialog.html
		paymentDialog = topWindow.document.createElement( 'iframe' ) ;
		paymentDialog.frameBorder = 0 ;
		paymentDialog.allowTransparency = false ;
		//设置样式
		paymentDialog.style.position = (BROWSER_INFO.IsIE ? 'absolute' : 'fixed');
		paymentDialog.src = "<%=com.yuanluesoft.jeaf.util.Environment.getWebApplicationSafeUrl()%>/jeaf/dialog/dialog.html?dialogUrl=" + url;
		paymentDialog.style.left = left + 'px';
		paymentDialog.style.top = top + 'px';
		paymentDialog.style.width = PAYMENT_WIDTH + 'px';
		paymentDialog.style.height = PAYMENT_HEIGHT + 'px';
		paymentDialog.id = 'dialog';
		cover.appendChild(paymentDialog);
		paymentDialog = document.frames ? document.frames["dialog"] : document.getElementById("dialog").contentWindow;
	}
	//创建定时器检查支付状态
	paymentTimer = window.setInterval("retrievePaymentId('" + mode + "')", 5);
}
//获取预支付单ID
function retrievePaymentId(openMode) {
	try {
		if(paymentDialog.closed) {
			window.clearInterval(paymentTimer);
			closePayment();
			return;
		}
		var queryString = paymentDialog.location.search;
		var index = queryString.indexOf("paymentSuccess=");
		if(index!=-1) {
			window.clearInterval(paymentTimer);
			closePayment();
			var paymentSuccess = queryString.substring(index + "paymentSuccess=".length);
			var paymentId = queryString.substring("?paymentId=".length, index - 1);
			onPaymentComplete(paymentId, paymentSuccess); //调用支付完成方法,由调用者实现
		}
	}
	catch(e) {
	
	}
}

//关闭支付页面
function closePayment() {
	if(!paymentDialog.closed) { //使用window.open方式打开
		paymentDialog.close();
	}
	destoryCover(getTopWindow(), cover);
}

//浏览器信息
var BROWSER_INFO = {
	IsIE		: /*@cc_on!@*/false,
	IsIE7		: /*@cc_on!@*/false && ( parseInt( navigator.userAgent.toLowerCase().match( /msie (\d+)/ )[1], 10 ) >= 7 ),
	IsIE6		: /*@cc_on!@*/false && ( parseInt( navigator.userAgent.toLowerCase().match( /msie (\d+)/ )[1], 10 ) >= 6 ),
	IsSafari	: navigator.userAgent.toLowerCase().indexOf(' applewebkit/')!=-1,		// Read "IsWebKit"
	IsOpera		: !!window.opera,
	IsAIR		: navigator.userAgent.toLowerCase().indexOf(' adobeair/')!=-1,
	IsMac		: navigator.userAgent.toLowerCase().indexOf('macintosh')!=-1
}
//创建div覆盖当前窗口,屏蔽对本窗口的操作
function createCover(parentWindow, opacity) {
	var cover = parentWindow.document.createElement("div");
	cover.style.position = (BROWSER_INFO.IsIE ? 'absolute' : 'fixed');
	cover.style.left = parentWindow.document.body.scrollLeft + 'px';
	cover.style.top = parentWindow.document.body.scrollTop + 'px';
	cover.style.width = parentWindow.document.body.clientWidth + 'px';
	cover.style.height = parentWindow.document.body.clientHeight + 'px';
	cover.id = 'cover';
	cover.style.backgroundColor = "transparent";
	//创建iframe,避免因div优先级低不能覆盖select,iframe等元素
	var coverFrame = parentWindow.document.createElement( 'iframe' ) ;
	coverFrame.frameBorder = 0 ;
	coverFrame.allowTransparency = true ;
	coverFrame.style.position = (BROWSER_INFO.IsIE ? 'absolute' : 'fixed');
	coverFrame.style.left = '0px';
	coverFrame.style.top = '0px';
	coverFrame.style.width = cover.style.width;
	coverFrame.style.height = cover.style.height;
	coverFrame.style.filter = 'alpha(opacity=' + opacity + ');';
	coverFrame.style.opacity = opacity;
	cover.appendChild(coverFrame);
	var childNodes = parentWindow.document.body.childNodes;
	if(childNodes.length==0) {
		parentWindow.document.body.appendChild(cover);
	}
	else {
		var maxZIndex = 0;
		for(var i=0; i < childNodes.length; i++) {
			if(childNodes[i].style && childNodes[i].style.zIndex) {
				var zIndex = childNodes[i].style.zIndex;
				if(zIndex!="") {
					zIndex = parseInt(zIndex);
					if(zIndex>maxZIndex) {
						maxZIndex = zIndex;
					}
				}
			}
		}
		cover.style.zIndex = maxZIndex + 1;
		parentWindow.document.body.insertBefore(cover, childNodes(0));
	}
	var adjustCoverSize = function()  {
		cover.style.left = parentWindow.document.body.scrollLeft + 'px';
		cover.style.top = parentWindow.document.body.scrollTop + 'px';
		cover.style.width = parentWindow.document.body.clientWidth + 'px';
		cover.style.height = parentWindow.document.body.clientHeight + 'px';
		coverFrame.style.width = cover.style.width;
		coverFrame.style.height = cover.style.height;
	};
	cover.adjustCoverSize = adjustCoverSize;
	parentWindow.attachEvent('onresize', adjustCoverSize);
	parentWindow.attachEvent('onscroll', adjustCoverSize);
	return cover;
}
function destoryCover(parentWindow, cover) { //销毁覆盖在窗口上的div
	parentWindow.detachEvent('onresize', cover.adjustCoverSize);
	parentWindow.detachEvent('onscroll', cover.adjustCoverSize);
	cover.parentNode.removeChild(cover);
}
function getTopWindow() { //获取当前窗口隶属的顶层窗口或帧frame
	var topWindow = window;
	while(topWindow.frameElement) {
		if(topWindow.frameElement.tagName.toLowerCase() == "frame") {
			break;
		}
		topWindow = topWindow.frameElement.ownerDocument.parentWindow;
	}
	return topWindow;
}
function getDialogFrame() { //获取对话框iframe
	var dialog = window.frameElement;
	while(dialog.id!='dialog') {
		dialog = dialog.ownerDocument.parentWindow.frameElement;
	}
	return dialog;
}