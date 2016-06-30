function compute() {
	LuanQuXian(toDouble(document.getElementsByName("driftAngleA")[0].value),
			   toDouble(document.getElementsByName("driftAngleB")[0].value),
			   toDouble(document.getElementsByName("baseLineLength")[0].value),
			   toDouble(document.getElementsByName("radiusA")[0].value),
			   toDouble(document.getElementsByName("lsa")[0].value),
			   toDouble(document.getElementsByName("lsb")[0].value));
}
//卵型曲线设计
function LuanQuXian(Ja, Jb, AB, Ra, LA, LB) {
    var DP = 0;
    var LS = 0;
    oJA = Ja;
    oJB = Jb;
    Ja = deg(Ja);
    Jb = deg(Jb);
    L1 = Ra * Ja * Math.PI / 180 - LA / 2;
    if(L1 < 0) {
        alert("LSa太大");
        return;
    }
    Pa = LA * LA / 24 / Ra - Math.pow(LA, 4) / 2384 / Math.pow(Ra, 3);
    Qa = LA / 2 - Math.pow(LA, 3) / 240 / Ra / Ra;
    T1 = (Ra + Pa) * degreesTan(Ja / 2);
    Ta = T1 + Qa;
    while(true) {
        T2 = AB - T1 - DP / degreesTan(Jb);
        a = 1;
        b = -T2 / degreesTan(Jb / 2);
        c = LB * LB / 24;
        d = b * b - 4 * a * c;
        R = 0;
        if(d < 0) {
            alert("LS=" + LS + "太大");
        }
        else {
            R = (-b + Math.pow(d, 0.5)) / 2 / a;
        }
        Rb = R;
        L2 = Rb * Jb * Math.PI / 180 - LB / 2;
        if(L2 < 0) {
            alert("LSb太大")
            return;
        }
        Pb = LB * LB / 24 / Rb - Math.pow(LB, 4) / 2384 / Math.pow(Rb, 3);
        Qb = LB / 2 - Math.pow(LB, 3) / 240 / Rb / Rb;
        T2 = (Rb + Pb) * degreesTan(Jb / 2);
        Tb = T2 + Qb - DP / degreesSin(Jb);
        var PF = Pb - Pa - DP;
        var R2 = Ra - Rb;
        RF = Ra * Rb / Math.abs(R2);
        FL = Math.pow(24 * RF * Math.abs(PF), 0.5);
        L1 = Ra * Ja * Math.PI / 180 - LA / 2 - FL / 2;
        L2 = Rb * Jb * Math.PI / 180 - LB / 2 - FL / 2;
        if(isNaN(L1) || isNaN(L2) || (L1 > 0 && L2 > 0)) {
        	break;
        }
        F1 = FL + 2 * L1 - 0.01;
        if(L2 < L1) {
        	F1 = FL + 2 * L2 - 0.01;
        }
        PF = F1 * F1 / 24 / RF - Math.pow(F1, 4) / 2384 / Math.pow(RF, 3);
        var P2 = Pb - Pa
        DP = Pb - Pa - PF * sign(P2);
        //ret = ret & "DP=" & DP & vbCrLf
    }
    L = LA + L1 + FL + L2 + LB;

	var values = "PJa： " + round(oJA, 4) + "<br/>" +
				 "PJb： " + round(oJB, 4) + "<br/>" +
				 "AB： " + round(AB, 4) + "<br/>" +
				 "DP： " + round(DP * sign(DP), 4) + "<br/>" +
				 "RA： " + round(Ra, 4) + "<br/>" +
				 "LSa： " + round(LA, 4) + "<br/>" +
				 "TA： " + round(Ta, 4) + "<br/>" +
				 "T1： " + round(T1, 4) + "<br/>" +
				 "LYa： " + round(L1, 4) + "<br/>" +
				 "LF： " + round(FL, 4) + "<br/>" +
				 "RB： " + round(Rb, 4) + "<br/>" +
				 "LSb： " + round(LB, 4) + "<br/>" +
				 "TB： " + round(Tb, 4) + "<br/>" +
				 "LYb： " + round(L2, 4) + "<br/>" +
				 "Lb： " + round(L, 4);
	window.top.client.alert(values, '计算结果');
}