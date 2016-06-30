var src = window.frameElement.src;
var index = src.indexOf("cssFile=");
if(index!=-1) {
	document.write('<link href="' + src.substring(index + "cssFile=".length).replace(/%2f/gi, '/') + '" rel="stylesheet" type="text/css"/>');
}