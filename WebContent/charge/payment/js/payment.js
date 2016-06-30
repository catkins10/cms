//以对话框的方式打开支付界面
var paymentDialog, paymentTimer;
function openPaymentDialog(applicatonName, payReason, providerId, providerName, money, providerMoney) {
	disableInput();
	var url = "https://passport.9191edu.com/kd9191edu/charge/payment/payment.shtml" +
			  "?act=dialog" +
			  "&applicationName=" + applicatonName +
			  "&payReason=" + StringUtils.utf8Encode(payReason) +
			  "&providerId=" + providerId +
			  "&providerName=" + StringUtils.utf8Encode(providerName) +
			  "&money=" + money +
			  "&providerMoney=" + providerMoney +
			  "&from=" + StringUtils.utf8Encode(window.location.href);
    paymentDialog = window.open(url, "payment", "width=600px,height=400px,scrollbars=yes,status=no,resizable=no,toolbar=no,menubar=no,location=no", false);
    paymentTimer = window.setInterval("retrievePrepayBillId('dialog')", 10);
}
//弹出支付页面
function popupPaymentPage(applicatonName, payReason, providerId, providerName, money, providerMoney) {
	disableInput();
	var paymentDiv = document.createElement("div");
	paymentDiv.id = "paymentDiv";
	var width = 500;
	var height = 300;
	paymentDiv.style.cssText = "width:" + width + ";" +
					 	   	   "height:" + height + ";" +
					 	   	   "position:absolute;" +
					 	   	   "top:" + (document.body.scrollTop + (document.body.clientHeight - height)/2)  + ";" +
					 	   	   "left:" + (document.body.scrollLeft + (document.body.clientWidth - width)/2) + ";" +
					 	   	   "border: 1 solid gray;" +
					 	   	   "background:#ffffff";
	var html = '<div style="height:22px;width:100%;background:#191970">' +
			   '	<div style="float:left;padding-left:5px;padding-top:5px;font-size:12px;font-family:宋体;font-weight:bold;color:#ffffff">支付平台</div>' +
			   '	<div style="float:right;padding-right:8px;padding-top:4px;cursor:pointer" onclick="hidePaymentPage()">' +
			   '		<img src="https://passport.9191edu.com/kd9191edu/charge/payment/images/exit.gif">' +
			   '	</div>' +
			   '</div>';
	paymentDiv.innerHTML = html;
	var iframe = document.createElement("iframe");
	iframe.id = "paymentPage";
	iframe.style.cssText = "width:100%;height:100%;";
	var url = "https://passport.9191edu.com/kd9191edu/charge/payment/payment.shtml" +
			  "?act=popup" +
			  "&applicationName=" + applicatonName +
			  "&payReason=" + StringUtils.utf8Encode(payReason) +
			  "&providerId=" + providerId +
			  "&providerName=" + StringUtils.utf8Encode(providerName) +
			  "&money=" + money +
			  "&providerMoney=" + providerMoney +
			  "&from=" + StringUtils.utf8Encode(window.location.href);
	iframe.src = url;
	paymentDiv.style.zIndex = MAX_Z_INDEX;
	document.body.appendChild(paymentDiv);
	paymentDiv.appendChild(iframe);
	paymentDialog = frames[frames.length - 1];
	iframe.focus();
	paymentTimer = window.setInterval("retrievePrepayBillId('popup')", 10);
}
function disableInput() { //禁止输入
	var shadowDiv = document.createElement("div");
	shadowDiv.id = "shadowDiv";
	shadowDiv.style.cssText = "width:100%; height:100%; position:absolute; top:" + document.body.scrollTop + "px; left:" + document.body.scrollLeft + "px; background-color:#FFF; filter:alpha(opacity=60);opacity:0.7; z-index:65530";
	document.body.appendChild(shadowDiv);
	document.body.onscroll = adjustPosition;
	document.body.onresize = adjustPosition;
}
function hidePaymentPage() { //关闭支付页面
	var paymentDiv = document.getElementById("paymentDiv");
	if(paymentDiv) {
		document.body.removeChild(paymentDiv);
	}
	document.body.removeChild(document.getElementById("shadowDiv"));
	window.clearInterval(paymentTimer);
}
function adjustPosition() { //调整位置
	var shadowDiv = document.getElementById("shadowDiv");
	if(!shadowDiv) {
		return;
	}
	shadowDiv.style.left = document.body.scrollLeft; 
	shadowDiv.style.top = document.body.scrollTop;
	var paymentDiv = document.getElementById("paymentDiv");
	if(paymentDiv) {
		paymentDiv.style.left = document.body.scrollLeft + (document.body.clientWidth - paymentDiv.offsetWidth)/2; 
		paymentDiv.style.top = document.body.scrollTop + (document.body.clientHeight - paymentDiv.offsetHeight)/2;
	}
}
function retrievePrepayBillId(openMode) { //获取预支付单ID
	try {
		if(paymentDialog.closed) {
			window.clearInterval(paymentTimer);
			hidePaymentPage();
			return;
		}
		var queryString = paymentDialog.location.search;
		var index = queryString.indexOf("repayBillId=");
		if(index!=-1) {
			window.clearInterval(paymentTimer);
			if(openMode=='dialog') {
				paymentDialog.close();
			}
			hidePaymentPage();
			var prepayBillId = queryString.substring(index + "repayBillId=".length);
			onPrepay(prepayBillId);
		}
	}
	catch(e) {
	
	}
}