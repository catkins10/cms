function compute() {
   FuQuBXian(toDouble(document.getElementsByName("driftAngleA")[0].value),
 	 		 toDouble(document.getElementsByName("driftAngleB")[0].value),
    		 toDouble(document.getElementsByName("baseLineLength")[0].value),
    		 toDouble(document.getElementsByName("radiusA")[0].value),
    		 toDouble(document.getElementsByName("lsa")[0].value),
    		 -1);
}
//不相切复曲线设计
function FuQuBXian(Ja, Jb, AB, R0, LSa, LSb) {
    oJA = Ja;
    oJB = Jb;
    Ja = deg(Ja);
    Jb = deg(Jb);

    Ra = Math.abs(R0);
    Pa = Math.pow(LSa, 2) / 24 / Ra - Math.pow(LSa, 4) / 2384 / Math.pow(Ra, 3);
    Qa = LSa / 2 - Math.pow(LSa, 3) / 240 / Math.pow(Ra, 2);
    T1 = (Ra + Pa) * degreesTan(Ja / 2);
    Ta = T1 + Qa;
    LYa = Math.PI / 180 * Ja * Ra - LSa / 2;
    L1 = LYa + LSa;
    T2 = AB - T1;
    Pb = Pa;
    Rb = T2 / degreesTan(Jb / 2) - Pb;
    LSb = LSa * Math.sqrt(Rb) / Math.sqrt(Ra);
    Qb = LSb / 2 - Math.pow(LSb, 3) / 240 / Math.pow(Rb, 2);
    Tb = T2 + Qb;
    LYb = Math.PI / 180 * Jb * Rb - LSb / 2;
    L2 = LYb + LSb;
    L = L1 + L2;

	var values = "PJa： " + round(oJA, 4) + "<br/>" +
				 "PJb： " + round(oJB, 4) + "<br/>" +
				 "AB： " + round(AB, 4) + "<br/>" +
				 "RA： " + round(Ra, 4) + "<br/>" +
				 "LSa： " + round(LSa, 4) + "<br/>" +
				 "RB： " + round(Rb, 4) + "<br/>" +
				 "LSb： " + round(LSb, 4) + "<br/>" +
				 "TA： " + round(Ta, 4) + "<br/>" +
				 "TB： " + round(Tb, 4) + "<br/>" +
				 "LYa： " + round(LYa, 4) + "<br/>" +
				 "LYb： " + round(LYb, 4) + "<br/>" +
				 "L： " + round(L, 4);
	window.top.client.alert(values, '计算结果');
}