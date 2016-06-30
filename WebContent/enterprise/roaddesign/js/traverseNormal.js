window.onload = function() {
	setTraverseListItems("surveyStation,backsightSurveyStation");
};
function computeNormalTraverse() { //导线测量:一般测量
	//测站
	var surveyStation = document.getElementsByName('surveyStation')[0];
	var xa = toDouble(surveyStation.getAttribute("x"));
	var ya = toDouble(surveyStation.getAttribute("y"));
	var za = toDouble(surveyStation.getAttribute("z"));
	//后视测站
	var backsightSurveyStation = document.getElementsByName('backsightSurveyStation')[0];
	var xb = toDouble(backsightSurveyStation.getAttribute("x"));
	var yb = toDouble(backsightSurveyStation.getAttribute("y"));
	var zb = toDouble(backsightSurveyStation.getAttribute("z"));
	//水平角
	var horizontalAngle = toDouble(document.getElementsByName('horizontalAngle')[0].value);
	//水平距离
	var horizontalDistance = toDouble(document.getElementsByName('horizontalDistance')[0].value);
	//后高度值
	var backHeight = toDouble(document.getElementsByName('backHeight')[0].value);
	//后镜高
	var backMirrorHeight = toDouble(document.getElementsByName('backMirrorHeight')[0].value);
	//前高度值
	var frontHeight = toDouble(document.getElementsByName('frontHeight')[0].value);
	//前镜高
	var frontMirrorHeight = toDouble(document.getElementsByName('frontMirrorHeight')[0].value);
	
	//计算
	horizontalAngle = deg(horizontalAngle);
	var d10x = xa - xb
    var d10y = ya - yb
    var ct0 = d10x==0 ? Math.PI / 2 : Math.atan(Math.abs(d10y / d10x));
    var fw0;
    if(d10x >= 0 && d10y >= 0) {
    	fw0 = ct0;
    }
    if(d10x <= 0 && d10y >= 0) {
    	fw0 = Math.PI - ct0;
    }
    if(d10x <= 0 && d10y <= 0) {
    	fw0 = Math.PI + ct0;
    }
    if(d10x >= 0 && d10y <= 0) {
    	fw0 = 2 * Math.PI - ct0;
    }
    fw1 = fw0 + horizontalAngle / 180 * Math.PI - Math.PI;
    var x1 = round(xa + horizontalDistance * Math.cos(fw1), 4);
    var y1 = round(ya + horizontalDistance * Math.sin(fw1), 4);
    var z1 = round(zb + (frontHeight - frontMirrorHeight) - (backHeight - backMirrorHeight), 4);

	//输出
    var message = "X1=" + (isNaN(x1) ? "无效" : x1) + ", Y1=" + (isNaN(y1) ? "无效" : y1) + ", Z1=" + (isNaN(z1) ? "无效" : z1) + "<br/>是否添加项目数据？";
    var dialog = window.top.client.confirm("一般测量", message);
    dialog.onOK = function() {
    	addTraverse(document.getElementsByName('traverseName')[0].value, x1, y1, z1);
    	document.getElementsByName('traverseName')[0].value = "";
    	return true;
    };

 	//水平角
	document.getElementsByName('horizontalAngle')[0].value = "";
	//水平距离
	document.getElementsByName('horizontalDistance')[0].value = "";
	//后高度值
	document.getElementsByName('backHeight')[0].value = "";
	//后镜高
	document.getElementsByName('backMirrorHeight')[0].value = "1.3";
	//前高度值
	document.getElementsByName('frontHeight')[0].value = "";
	//前镜高
	document.getElementsByName('frontMirrorHeight')[0].value = "1.3";
}