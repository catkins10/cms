function compute() {
    var curveLength = !document.getElementsByName("curveLengthEnabled")[0].checked ?  -1 : toDouble(document.getElementsByName("curveLength")[0].value);
   QieJiXian(toDouble(document.getElementsByName("driftAngleA")[0].value),
			  toDouble(document.getElementsByName("driftAngleB")[0].value),
			  toDouble(document.getElementsByName("baseLineLength")[0].value),
			  curveLength);

}
//切基线双交点平曲线设计
function QieJiXian(Ja, Jb, AB, L0) {
    oJA = Ja;
    oJB = Jb;
    Ja = deg(Ja);
    Jb = deg(Jb);
    LS = Math.abs(L0);
    SA = degreesSin(Ja);
    SB = degreesSin(Jb);
    CA = degreesCos(Ja);
    CB = degreesCos(Jb);
    while(true) {
        a = (1 - CA) / SA + (1 - CB) / SB;
        b = -AB;
        c = LS * LS / 24 * (1 / SA + 1 / SB);
        if(L0 != -1) {
        	break;
        }
        JP = (Ja + Jb) * Math.PI / 180;
        R = AB / (a + JP * JP / 96 * (1 / SA + 1 / SB));
        LS = Math.floor(R * JP / 2);
        L0 = 1;
    }
    d = b * b - 4 * a * c;
    R = 0;
    if(d < 0) {
        alert("LS=" + LS + "太大");
        return;
    }
    R = (-b + Math.pow(d, 0.5)) / 2 / a;
    Ta = LS / 2 + (R - (R + LS * LS / 24 / R) * CA) / SA - Math.pow(LS, 3) / 240 / R / R;
    Tb = LS / 2 + (R - (R + LS * LS / 24 / R) * CB) / SB - Math.pow(LS, 3) / 240 / R / R;
    LY = ((Ja + Jb) * Math.PI / 180 - LS / R) * R;
    L = 2 * LS + LY;

	var values = "PJa： " + round(oJA, 4) + "<br/>" +
				 "PJb： " + round(oJB, 4) + "<br/>" +
				 "AB： " + round(AB, 4) + "<br/>" +
				 "R： " + round(R, 4) + "<br/>" +
				 "LS： " + round(LS, 4) + "<br/>" +
				 "TA： " + round(Ta, 4) + "<br/>" +
				 "TB： " + round(Tb, 4) + "<br/>" +
				 "LY： " + round(LY, 4) + "<br/>" +
				 "L： " + round(L, 4);
	window.top.client.alert(values, '计算结果');
}