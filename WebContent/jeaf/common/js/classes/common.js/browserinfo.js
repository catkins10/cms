//浏览器信息
BrowserInfo = function() {

};
BrowserInfo.isIE = function() {
	return /(msie\s|trident.*rv:)([\w.]+)/.test(navigator.userAgent.toLowerCase()) && !/opera/i.test(navigator.userAgent); // /*@cc_on!@*/false,
};
BrowserInfo.getIEVersion = function() {
	return /(msie\s|trident.*rv:)([\w.]+)/.test(navigator.userAgent.toLowerCase()) && !/opera/i.test(navigator.userAgent) ? parseFloat( navigator.userAgent.toLowerCase().match(/(msie\s|trident.*rv:)([\w.]+)/)[2]) : 0;
};
BrowserInfo.isSafari = function() {
	return navigator.userAgent.toLowerCase().indexOf(' applewebkit/')!=-1;		// Read "IsWebKit"
};
BrowserInfo.isOpera = function() {
	return !!window.opera;
};
BrowserInfo.isAIR = function() {
	return navigator.userAgent.toLowerCase().indexOf(' adobeair/')!=-1;
}
BrowserInfo.isMac = function() {
	return navigator.userAgent.toLowerCase().indexOf('macintosh')!=-1;
};
BrowserInfo.isAndroid = function() {
	return navigator.userAgent.toLowerCase().indexOf('android')!=-1;
};
BrowserInfo.isIPhone = function() {
	return navigator.userAgent.toLowerCase().indexOf('iphone')!=-1;
};
BrowserInfo.isIPad = function() {
	return navigator.userAgent.toLowerCase().indexOf('ipad')!=-1;
};
BrowserInfo.getFlashVersion = function() { //获取flash版本,如果没有安装,返回null
	var plugin;
	try {
		plugin = document.all ? new ActiveXObject('ShockwaveFlash.ShockwaveFlash') : null;
	}
	catch(e) {
	
	}
	if(plugin) {
		var version = plugin.GetVariable("$version");
 		return parseInt(version.split(" ")[1].split(",")[0]); 
 	}
 	else if(navigator.plugins && navigator.plugins.length > 0 && (plugin = navigator.plugins["Shockwave Flash"])) {
		var values = plugin.description.split(" ");
		for(var i = 0; i < values.length; ++i) {
			if(!isNaN(values[i]=parseInt(values[i]))) {
				return values[i];
			}
	 	}
 	}
 	return null;
}