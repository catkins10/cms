//GPS定位
function gpsLocation(gpsTerminalNumber, recordId) {
	window.setTimeout("ScriptUtils.appendJsFile(document, '" + RequestUtils.getContextPath() + "/jeaf/gps/gpsLocation.js.shtml?gpsTerminalNumber=" + gpsTerminalNumber + "&recordId=" + recordId + "&seq=" + Math.random() + "')", 200);
}
//打开地图
function openMap(recordId, width, height, internal) {
	try {
		var latitude = document.getElementById("location.coordinate.latitude_" + recordId).innerHTML.trim();
		var longitude = document.getElementById("location.coordinate.longitude_" + recordId).innerHTML.trim();
		if(latitude=="" || longitude=="") {
			alert("未完成定位");
			return;
		}
		var url = RequestUtils.getContextPath() + '/jeaf/gps/map.shtml' +
				  '?latitude=' + latitude +
				  '&longitude=' + longitude +
				  (internal ? '&internal=true' : '');
		DialogUtils.openDialog(url, width, height);
	}
	catch(e) {
	
	}
}
//选择城市
function selectCity(width, height, fieldName, script, internal, ipLocationDisable) {
	var url = RequestUtils.getContextPath() + '/jeaf/gps/map.shtml' +
			  '?selectCity=true' +
			  '&fieldName=' + fieldName +
			  (script ? '&script=' + StringUtils.utf8Encode(script) : "") +
			  (internal ? '&internal=true' : '') +
			  (ipLocationDisable ? '&ipLocationDisable=true' : '');
	DialogUtils.openDialog(url, width, height);
}