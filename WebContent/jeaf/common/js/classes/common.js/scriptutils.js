ScriptUtils = function() {

};
//引入JS,如果已经引入过,则不会重新引入,onLoadCallback=function();
ScriptUtils.appendJsFile = function(document, jsFile, scriptId, onLoadCallback) {
	var head = document.getElementsByTagName("head")[0];
	var scripts = head.getElementsByTagName("script");
	for(var i=0; i < (scripts ? scripts.length : 0); i++) {
		if(!scripts[i].src) {
			continue;
		}
		if(scripts[i].src.indexOf(jsFile) == scripts[i].src.length - jsFile.length || (scripts[i].src && jsFile.substring(jsFile.length - 3, jsFile.length)==".js" && scripts[i].src.indexOf(jsFile)!=-1)) {
			if(onLoadCallback) {
				onLoadCallback.call(null);
			}
			return;
		}
	}
	if(document!=window.top.document && jsFile.substring(jsFile.length - 3, jsFile.length)==".js") { //从顶层窗口获取已经加载过的js
		var scripts = window.top.document.getElementsByTagName('script');
		for(var i=0; i < (scripts ? scripts.length : 0); i++) {
			if(scripts[i].src && scripts[i].src.indexOf(jsFile)!=-1) {
				jsFile = scripts[i].src;
				break;
			}
		}
	}
	if(scriptId) {
		var script = document.getElementById(scriptId);
	   	if(script) {
	   	 	script.parentNode.removeChild(script);
	   	}
   	}
	var script = document.createElement("script");
	if(scriptId) {
		script.id = scriptId;
	}
	script.src = jsFile;
	script.language = 'JavaScript';
	if(onLoadCallback) {
		script.onload = script.onreadystatechange = script.onerror = function() {  
			if(script.readyState && script.readyState != 'loaded' && script.readyState != 'complete') {
				return;
			}
			script.onreadystatechange = script.onload = null;
			onLoadCallback.call(null);
		}
	}
	head.appendChild(script);
};