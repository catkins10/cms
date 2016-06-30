function compute() {
    var radiusA = Math.abs(toDouble(document.getElementsByName("radiusA")[0].value));
    if(document.getElementsByName("adjustEnabled")[0].checked) {
        radiusA = -radiusA;
    }
    FuQuXian(toDouble(document.getElementsByName("driftAngleA")[0].value),
 	 		 toDouble(document.getElementsByName("driftAngleB")[0].value),
    		 toDouble(document.getElementsByName("baseLineLength")[0].value),
    		 radiusA,
    		 toDouble(document.getElementsByName("lsa")[0].value),
    		 toDouble(document.getElementsByName("lsb")[0].value));
}
//相切复曲线设计
function FuQuXian(Ja, Jb, AB, R0, LA, LS) {
    oJA = Ja
    oJB = Jb
    Ja = deg(Ja)
    Jb = deg(Jb)
    Ra = Math.abs(R0);
    SA = degreesSin(Ja);
    SB = degreesSin(Jb);
    CA = degreesCos(Ja);
    CB = degreesCos(Jb);
    while(true) {
        T = (Ra + LA * LA / 24 / Ra - Ra * CA) / SA;
        a = (1 - CB) / SB;
        b = T - AB;
        c = LS * LS / 24 / SB;
        d = b * b - 4 * a * c;

        R = 0;
        if(d < 0) {
            alert("LS=" + LS + "太大");
            return;
        }
        R = (-b + Math.pow(d, 0.5)) / 2 / a;

        Rb = R;
        if(R0 > 0) {
        	break;
        }
        if((Ra / Rb) >= 1.5) {
            Ra = 1.5 * Rb
        }
        else if((Ra / Rb) <= (1 / 1.5)) {
            Ra = 1 / 1.5 * Rb;
        }
        else {
            break;
        }
    }
    Ta = LA / 2 + (Ra - (Ra + LA * LA / 24 / Ra) * CA) / SA - Math.pow(LA, 3) / 240 / Ra / Ra;
    Tb = LS / 2 + (Rb - (Rb + LS * LS / 24 / Rb) * CB) / SB - Math.pow(LS, 3) / 240 / Rb / Rb;
    Ya = (Ja * Math.PI / 180 - LA / 2 / Ra) * Ra;
    Yb = (Jb * Math.PI / 180 - LS / 2 / Rb) * Rb;
    L = LA + Ya + Yb + LS;
	
	var values = "PJa： " + round(oJA, 4) + "<br/>" +
				 "PJb： " + round(oJB, 4) + "<br/>" +
				 "AB： " + round(AB, 4) + "<br/>" +
				 "RA： " + round(Ra, 4) + "<br/>" +
				 "LSa： " + round(LA, 4) + "<br/>" +
				 "RB： " + round(Rb, 4) + "<br/>" +
				 "LSb： " + round(LS, 4) + "<br/>" +
				 "TA： " + round(Ta, 4) + "<br/>" +
				 "TB： " + round(Tb, 4) + "<br/>" +
				 "LYa： " + round(Ya, 4) + "<br/>" +
				 "LYb： " + round(Yb, 4) + "<br/>" +
				 "L： " + round(L, 4);
	window.top.client.alert(values, '计算结果');
}