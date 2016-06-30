function compute() {
	var y0 = doCompute(toDouble(document.getElementsByName("driftAngle")[0].value), toDouble(document.getElementsByName("ls")[0].value), toDouble(document.getElementsByName("t0")[0].value), toDouble(document.getElementsByName("radius")[0].value));
	document.getElementsByName("y0")[0].value = isNaN(y0) ? "无效" : y0;
}
//切线支距计算
function doCompute(PJ, L0, T0, R) {
    PJ = deg(PJ);
    LS = Math.abs(L0);
    P = LS * LS / 24 / R - Math.pow(LS, 4) / 2384 / Math.pow(R, 3);
    T = (R + P) * degreesTan(PJ / 2) + LS / 2 - Math.pow(LS, 3) / 240 / R / R;
    X = T - T0;
    L = X;
    var Y = 0;
    if(L >= LS) {
        FI = ((L - LS) / R + LS / 2 / R) * 180 / Math.PI;
        Y = LS * LS / 24 / R - Math.pow(LS, 4) / 2384 / Math.pow(R, 3) + R * (1 - degreesCos(FI));
    }
    else {
        Y = Math.pow(L, 3) / 6 / R / LS - Math.pow(L, 7) / 336 / Math.pow(R, 3) / Math.pow(LS, 3);
    }
    return round(Y, 3);
}