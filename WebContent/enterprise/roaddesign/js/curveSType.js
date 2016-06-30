function compute() {
 	var radiusA = Math.abs(toDouble(document.getElementsByName("radiusA")[0].value));
    if(document.getElementsByName("adjustEnabled")[0].checked) {
        radiusA = -radiusA;
    }
    SQuXian(toDouble(document.getElementsByName("driftAngleA")[0].value),
 	 		toDouble(document.getElementsByName("driftAngleB")[0].value),
    		toDouble(document.getElementsByName("baseLineLength")[0].value),
    		radiusA,
    		toDouble(document.getElementsByName("lsa")[0].value),
    		toDouble(document.getElementsByName("lsb")[0].value));
}
//S型曲线设计,EA EB出入大, RB差一点
function SQuXian(Ja, Jb, AB, R0, LA, LS) {
	if(R0==0) {
		alert("Ra不能为0");
        return;
	}
	if(LA==0) {
		alert("LSa不能为0");
        return;
	}
    oJA = Ja;
    oJB = Jb;
    Ja = deg(Ja);
    Jb = deg(Jb);
    Ra = Math.abs(R0);
    while(true) {
        Ta = (Ra + LA * LA / 24 / Ra) * degreesTan(Ja / 2) + LA / 2 - Math.pow(LA, 3) / 240 / Ra / Ra;
        T = AB - Ta;
        a = degreesTan(Jb / 2);
        b = LS / 2 - T;
        c = LS * LS / 24 * a;

        d = b * b - 4 * a * c;
        R = 0;
        if(d < 0) {
            alert("LS=" + LS + "太大");
            return;
        }
        R = (-b + Math.pow(d, 0.5)) / 2 / a;

        Rb = R;
        if(R == 0) {
            alert("LSb输入错误");
            return;
        }
        while(true) {
            Tb = (Rb + LS * LS / 24 / Rb) * degreesTan(Jb / 2) + LS / 2 - Math.pow(LS, 3) / 240 / Rb / Rb;
            var DT = T - Tb;
            if(isNaN(DT) || Math.abs(DT) < 0.001) {
            	break;
            }
            //ret = ret & "DT=" & DT & vbCrLf
            //ret = ret & "R=" & R & vbCrLf
            Rb = Rb + DT / degreesTan(Jb / 2);
        }
        if(R0 > 0) {
        	break;
        }

        AA = Math.sqrt(Ra * LA);
        BA = Math.sqrt(Rb * LS);
        if(AA / BA >= 1.5) {
            Ra = 1.5 * 1.5 * BA * BA / LA;
        }
        else if(AA / BA <= 1 / 1.5) {
            Ra = Math.pow(1 / 1.5, 2) * BA * BA / LA;
        }
        else {
            break;
        }
    }
    Yb = (Jb * Math.PI / 180 - LS / Rb) * Rb;
    L1 = LS + Yb + LS;
    P = LS * LS / 24 / Rb - Math.pow(LS, 4) / 2384 / Math.pow(Rb, 3);
    EB = (Rb + P) / degreesCos(Jb / 2) - Rb;

    P = LA * LA / 24 / Ra - Math.pow(LA, 4) / 2384 / Math.pow(Ra, 3);
    EA = (Ra + P) / degreesCos(Ja / 2) - Ra;
    Ya = (Ja * Math.PI / 180 - LA / Ra) * Ra;
    L0 = LA + Ya + LA;

	var values = "PJa： " + round(oJA, 4) + "<br/>" +
				 "PJb： " + round(oJB, 4) + "<br/>" +
				 "AB： " + round(AB, 4) + "<br/>" +
				 "RA： " + round(Ra, 4) + "<br/>" +
				 "LSa： " + round(LA, 4) + "<br/>" +
				 "TA： " + round(Ta, 4) + "<br/>" +
				 "EA： " + round(EA, 4) + "<br/>" +
				 "LYa： " + round(Ya, 4) + "<br/>" +
				 "La： " + round(L0, 4) + "<br/>" +
				 "RB： " + round(Rb, 4) + "<br/>" +
				 "LSb： " + round(LS, 4) + "<br/>" +
				 "TB： " + round(Tb, 4) + "<br/>" +
				 "EB： " + round(EB, 4) + "<br/>" +
				 "LYb： " + round(Yb, 4) + "<br/>" +
				 "Lb： " + round(L1, 4);
	window.top.client.alert(values, '计算结果');
}