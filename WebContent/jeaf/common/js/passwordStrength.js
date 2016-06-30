var STRENGTH_COLORS = ["#ebebeb", "#ff4545", "#ffd35e", "#3abb1c"];
//输出密码强度,passwordStrengthElement:显示密码强度的页面元素,一般情况下使用div或span
function writePasswordStrength(passwordField, passwordStrengthElement) {
	var checkPasswordStrength = function() {
		var strength = getPasswordStrength(passwordField.value);
		var table = passwordStrengthElement.getElementsByTagName("table")[0];
		if(!table) {
			passwordStrengthElement.innerHTML = '<table border="0" cellpadding="4" cellspacing="0" bgcolor="' + STRENGTH_COLORS[0] + '">' +
												'	<tr>' +
												'		<td align="right" width="50px">&nbsp;</td>' +
												'		<td align="right" width="30px">&nbsp;</td>' +
												'		<td align="right" width="30px">&nbsp;</td>' +
												'	</tr>' +
												'</table>';
			table = passwordStrengthElement.getElementsByTagName("table")[0];
		}
		for(var i=0; i<3; i++) {
			table.rows[0].cells[i].innerHTML = Math.max(0, strength-1)==i ? ["未能评级", "弱", "中", "强"][strength] : "&nbsp;";
			table.rows[0].cells[i].style.backgroundColor = Math.max(0, strength-1)>=i ? STRENGTH_COLORS[strength] : "transparent";
		}
	};
	passwordField.onkeyup = checkPasswordStrength;
	checkPasswordStrength.call(null);
}

//获取密码强度,0/未能评级,1/弱,2/中,3/强
function getPasswordStrength(password) {
	if(password.length<6) {
		return 0; //未能评级
	}
	var number = 0; //是否有数字
	var lowerCaseLetter = 0; //小写字母
	var upperCaseLetter = 0; //大写字母
	var otherLetter = 0; //其它字符
	for(var i=0; i<password.length; i++) {
		var ch = password.charAt(i);
		if(ch>='0' && ch<='9') {
			number = 1;
		}
		else if(ch>='a' && ch<='z') {
			lowerCaseLetter = 1;
		}
		else if(ch>='A' && ch<='Z') {
			upperCaseLetter = 1;
		}
		else {
			otherLetter = 1;
		}
	}
	var strength = number + lowerCaseLetter + upperCaseLetter + otherLetter;
	return strength>3 ? 3 : strength;
}