window.onload = function() {
	setTraverseListItems("backsightPointA,backsightPointB1,backsightPointB2");
};
function computeBackCrossTraverse() { //导线测量:后方交会测量
	//后视点A
	var backsightPointA = document.getElementsByName('backsightPointA')[0];
	var xa = toDouble(backsightPointA.getAttribute("x"));
	var ya = toDouble(backsightPointA.getAttribute("y"));
	//后视点B1
	var backsightPointB1 = document.getElementsByName('backsightPointB1')[0];
	var xb = toDouble(backsightPointB1.getAttribute("x"));
	var yb = toDouble(backsightPointB1.getAttribute("y"));
	//后视点B2
	var backsightPointB2 = document.getElementsByName('backsightPointB2')[0];
	var xc = toDouble(backsightPointB2.getAttribute("x"));
	var yc = toDouble(backsightPointB2.getAttribute("y"));
	//夹角α
	var ja = toDouble(document.getElementsByName('includedAngleα')[0].value);
	//夹角β
	var jb = toDouble(document.getElementsByName('includedAngleβ')[0].value);
	//夹角γ
	var jc = toDouble(document.getElementsByName('includedAngleγ')[0].value);
	
	//计算
	ja = deg(ja);
	jb = deg(jb);
	jc = deg(jc);
	var a = Math.sqrt(Math.pow(xb - xc, 2) + Math.pow(yb - yc, 2));
	var b = Math.sqrt(Math.pow(xa - xc, 2) + Math.pow(ya - yc, 2));
	var c = Math.sqrt(Math.pow(xb - xa, 2) + Math.pow(yb - ya, 2));
	var cosa = (c * c + b * b - a * a) / (2 * b * c);
	var cosb = (a * a + c * c - b * b) / (2 * a * c);
	var cosc = (a * a + b * b - c * c) / (2 * a * b);
	var tana = Math.sqrt(1 - cosa * cosa) / cosa;
	var tanb = Math.sqrt(1 - cosb * cosb) / cosb;
	var tanc = Math.sqrt(1 - cosc * cosc) / cosc;
	var pa = degreesTan(ja) * tana / (degreesTan(ja) - tana);
	var pb = degreesTan(jb) * tanb / (degreesTan(jb) - tanb);
	var pc = degreesTan(jc) * tanc / (degreesTan(jc) - tanc);
	var xp = round((pa * xa + pb * xb + pc * xc) / (pa + pb + pc), 4);
	var yp = round((pa * ya + pb * yb + pc * yc) / (pa + pb + pc), 4);
	
	//输出
    var message = "X坐标：" + (isNaN(xp) ? "无效" : xp) + ", Y坐标：" + (isNaN(yp) ? "无效" : yp) + "<br/>是否添加项目数据？";
    var dialog = window.top.client.confirm("后方交会", message);
    dialog.onOK = function() {
    	addTraverse(document.getElementsByName('traverseName')[0].value, xp, yp, 0);
    	document.getElementsByName('traverseName')[0].value = "";
    	return true;
    };
	
	//夹角α
	document.getElementsByName('includedAngleα')[0].value = "";
	//夹角β
	document.getElementsByName('includedAngleβ')[0].value = "";
	//夹角γ
	document.getElementsByName('includedAngleγ')[0].value = "";
}