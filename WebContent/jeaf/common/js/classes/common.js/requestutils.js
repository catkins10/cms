RequestUtils = function() {

};
RequestUtils.contextPath = null;
RequestUtils.getContextPath = function() { //获取应用路径
	if(RequestUtils.contextPath!=null) {
		return RequestUtils.contextPath;	
	}
	var scripts = document.getElementsByTagName("script");
	for(var i=0; i<scripts.length; i++) {
		if(scripts[i].src.indexOf("common.js")==-1) {
			continue;
		}
		var url = RequestUtils._retrieveUrl(scripts[i].src);
		var endIndex = url.indexOf("/jeaf/common/js/common.js");
		if(endIndex==-1) {
			continue;
		}
		var beginIndex = url.indexOf("://");
		beginIndex = (beginIndex==-1 ? 0 : url.indexOf("/", beginIndex + 3));
		RequestUtils.contextPath = url.substring(beginIndex, endIndex);
	}
	return RequestUtils.contextPath;
};
RequestUtils._retrieveUrl = function(url) { //重置URL,删掉".."
	if(url.indexOf("://")==-1 && url.substring(0,1)!='/') {
		url = location.pathname.substring(0, location.pathname.lastIndexOf('/') + 1) + url;
	}
	var values = url.split("/");
	url = values[values.length-1];
	var skip = 0;
	for(var j=values.length-2; j>=0; j--) {
		if(values[j]=="..") {
			skip++;
		}
		else if(skip==0 || (skip--)==0) {
			url = values[j] + "/" + url;
		}
	}
	return url;
};
RequestUtils.getFullURL = function(url) { //获取完整的URL
	if(url.indexOf('://')!=-1) {
		return url;
	}
	var href = location.href;
	var index = href.indexOf('/', href.indexOf('://') + 3);
	var prefix = (index==-1 ? href : href.substring(0, index));
	if(url.substring(0, 1)=='/') {
		return prefix + url;
	}
	else {
		index = location.pathname.lastIndexOf('/');
		return prefix + (index==-1 ? '' : location.pathname.substring(0, index + 1)) + url;
	}
};