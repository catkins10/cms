window.onload = function() {
	setTraverseListItems("backsightPointA,backsightPointB");
};
function computeFrontCrossTraverse() { //导线测量:前方交会测量
	//后视点A
	var backsightPointA = document.getElementsByName('backsightPointA')[0];
	var xa = toDouble(backsightPointA.getAttribute("x"));
	var ya = toDouble(backsightPointA.getAttribute("y"));
	//后视点B
	var backsightPointB = document.getElementsByName('backsightPointB')[0];
	var xb = toDouble(backsightPointB.getAttribute("x"));
	var yb = toDouble(backsightPointB.getAttribute("y"));
	//后视点A距离
	var a = toDouble(document.getElementsByName('backsightPointADistance')[0].value);
	//后视点B距离
	var b = toDouble(document.getElementsByName('backsightPointBDistance')[0].value);
	//夹角(A→B)
	var jc = toDouble(document.getElementsByName('includedAngle')[0].value);

	//计算
	jc = deg(jc);
	var ja, jb;
    var dab = Math.sqrt(Math.pow(xb - xa, 2) + Math.pow(yb - ya, 2));
    if(a >= b) {
		var sina = b / dab * degreesSin(jc);
		var tana = sina / Math.sqrt(1 - Math.pow(sina, 2));
		ja = Math.atan(tana);
		jb = Math.PI - ja - jc / 180 * Math.PI;
    }
    else {
		var sinb = a / dab * degreesSin(jc);
		var tanb = sinb / Math.sqrt(1 - Math.pow(sinb, 2));
		jb = Math.atan(tanb);
		ja = Math.PI - jb - jc / 180 * Math.PI;
    }
	var xp = round((xa / Math.tan(jb) + xb / Math.tan(ja) + (yb - ya)) / (1 / Math.tan(ja) + 1 / Math.tan(jb)), 4);
	var yp = round((ya / Math.tan(jb) + yb / Math.tan(ja) + (xa - xb)) / (1 / Math.tan(ja) + 1 / Math.tan(jb)), 4);
	
	//输出
    var message = "X坐标：" + (isNaN(xp) ? "无效" : xp) + ", Y坐标：" + (isNaN(yp) ? "无效" : yp) + "<br/>是否添加项目数据？";
    var dialog = window.top.client.confirm("前方交会", message);
    dialog.onOK = function() {
    	addTraverse(document.getElementsByName('traverseName')[0].value, xp, yp, 0);
    	document.getElementsByName('traverseName')[0].value = "";
    	return true;
    };
	
	//后视点A距离
	document.getElementsByName('backsightPointADistance')[0].value = "";
	//后视点B距离
	document.getElementsByName('backsightPointBDistance')[0].value = "";
	//夹角(A→B)
	document.getElementsByName('includedAngle')[0].value = "";
}