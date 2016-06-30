CookieUtils = function() {

};
CookieUtils.getCookie = function(cookieName) {
	var cookies = document.cookie.split("; ");
	for (var i=0; i < cookies.length; i++) {
		var values = cookies[i].split("=");
	    if (cookieName == values[0]) {
	    	return unescape(values[1]);
		}
  	}
  	return null;
};