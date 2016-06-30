function compute() {
	var length = toDouble(document.getElementsByName("length")[0].value);
	TuQuXian(toDouble(document.getElementsByName("driftAngle")[0].value),
    		 document.getElementsByName("option")[0].value=="外距控制" ? "E" : "T",
    		 length,
    		 length);
}
//凸型曲线设计
function TuQuXian(PJ, strA, T, E) {
    oPJ = PJ;
    PJ = deg(PJ);
    if(strA != "E") {
        a = PJ * Math.PI / 180;
        R = T / ((1 + a * a / 24) * degreesTan(PJ / 2) + a / 2 - Math.pow(a, 3) / 240);
        LS = a * R;
        T1 = (R + LS * LS / 24 / R) * degreesTan(PJ / 2) + LS / 2 - Math.pow(LS, 3) / 240 / R / R;
    }
    else {
        JP = PJ * Math.PI / 180;
        R = E / ((1 + JP * JP / 24) / degreesCos(PJ / 2) - 1);
        LS = R * JP;
        T1 = (R + LS * LS / 24 / R) * degreesTan(PJ / 2) + LS / 2 - Math.pow(LS, 3) / 240 / R / R;
    }
    LY = (PJ * Math.PI / 180 - LS / R) * R;
    L = LS + LY + LS;
    E = (R + LS * LS / 24 / R) / degreesCos(PJ / 2) - R;

	var values = "PJ： " + round(oPJ, 4) + "<br/>" +
				 "R： " + round(R, 4) + "<br/>" +
				 "LS： " + round(LS, 4) + "<br/>" +
				 "T： " + round(T1, 4) + "<br/>" +
				 "E： " + round(E, 4) + "<br/>" +
				 "LY： " + round(LY, 4) + "<br/>" +
				 "L： " + round(L, 4);
	window.top.client.alert(values, '计算结果');
}