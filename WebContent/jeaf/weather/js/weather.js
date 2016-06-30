function setWeatherArea(area) {
	var expireDate = new Date();
	expireDate.setFullYear(expireDate.getFullYear() + 1);
	document.cookie = "weatherArea=" + StringUtils.utf8Encode(area) + "; expires=" + expireDate.toGMTString() + "; path=/";
}
function changeWeatherArea() {
	var area = StringUtils.utf8Decode(CookieUtils.getCookie('weatherArea'));
	DialogUtils.openInputDialog('天气', [{name:"area", title:"地区", defaultValue:area, length:100, inputMode:'text'}], 230, 50, '', function(value) {
		if(value.area!='') {
			setWeatherArea(value.area);
			window.location.reload();
		}
	});
}