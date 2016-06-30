//类:ajax
Ajax = function() {
	this.xmlHttp = null;
	this.errorCount = 0;
	try {
		this.xmlHttp = new XMLHttpRequest();
	}
	catch(e) {
		try { 
			this.xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
	    }
	    catch(ex) {
			this.xmlHttp = new ActiveXObject("Microsoft.XMLHTTP"); 
		}
	}
}
//发送请求,onDataArriveCallback=function(), onErrorCallback=function()
Ajax.prototype.openRequest = function(method, url, data, async, onDataArriveCallback, onErrorCallback, contentType) {
	try { 
		this.abort();
    }
    catch(e) {
		
	}
	try {
		this.xmlHttp.timeout = async ? 30000 : 0;
	}
    catch(e) {
		
	}
	this.xmlHttp.open(method, url, async);
	var ajax = this;
	this.xmlHttp.onreadystatechange = function() { //应答处理
		if(ajax.xmlHttp.readyState!=4) {
			return;
		}
		if(ajax.xmlHttp.status<300 && ajax.xmlHttp.status>=200) { //正常返回
			ajax.errorCount = 0;
			if(onDataArriveCallback) {
				onDataArriveCallback.call();
			}
		}
		else if(ajax.xmlHttp.status!=0) { //页面错误
			ajax.errorCount++;
			if(onErrorCallback) {
				onErrorCallback.call();
				ajax.errorCount = 0;
			}
		}
	};
	try {
		this.xmlHttp.onerror = this.xmlHttp.onabort  = function() { //错误处理
			onErrorCallback.call();
		};
	}
	catch(e) {
	
	}
	if(contentType) {
		this.xmlHttp.setRequestHeader("Content-Type", contentType);
	}
	this.xmlHttp.send(data);
};
Ajax.prototype.abort = function() { //放弃请求
	this.xmlHttp.abort();
};
Ajax.prototype.getStatus = function() { //获取状态
	return this.xmlHttp.status;
};
Ajax.prototype.getErrorCount = function() { //获取出错次数
	return this.errorCount;
};
Ajax.prototype.getResponseText = function() { //获取responseText
	return this.xmlHttp.responseText;
};
Ajax.prototype.getResponseXML = function() { //获取responseXML
	return this.xmlHttp.responseXML;
};
Ajax.getText = function(xmlElement) { //获取通知的属性
	return document.all ? xmlElement.text : xmlElement.textContent;
};