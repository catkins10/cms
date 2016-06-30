function deg(value) {
	var neg = 1;
	if(value < 0) {
		neg = -1;
		value = Math.abs(value);
	}
	var result = Math.floor(value);
    value = value - result;
    value = Math.floor(value * 100) + (Math.round(value * 10000) % 100) / 60;
    return (result + value / 60) * neg;
}
function dms(value) { //角度
	var neg = 1
	if(value < 0) {
	    neg = -1
	    value = Math.abs(value)
	}
	var result = Math.floor(value);
	value = value - result;
	value = Math.round(value * 3600);
	value = Math.floor(value / 60) / 100 + (value % 60) / 10000;
	return (result + value) * neg;
}
function degreesCos(degrees) { //角度COS
	return Math.cos(degrees * Math.PI / 180);
}
function degreesSin(degrees) { //角度SIN
	return Math.sin(degrees * Math.PI / 180);
}
function degreesTan(degrees) { //角度TAN
	return Math.tan(degrees * Math.PI / 180);
}
function toDouble(strValue) { //字符转换为数字
	var result = Number(strValue);
	return isNaN(result) ? 0 : result;
}
function round(value, numDigitsAfterDecimal) { //四舍五入
	var plus = Math.pow(10, numDigitsAfterDecimal);
	return Math.round(value * plus) / plus;
}
//解析数据
function parseLine(line, delimiter) {
    var parseResult = line.split(delimiter);
    var resultCount = 0;
    for(var i = 0; i<parseResult.length; i++) {
        parseResult[i] = parseResult[i].replace(/[ \t]/gi, '');
        if(parseResult[i]!="") {
            parseResult[resultCount] = parseResult[i];
            if(resultCount != i) {
                parseResult[i] = "";
            }
            resultCount++;
        }
    }
    return parseResult.slice(0, resultCount);
}
function sign(num) { //此函数共有5种返回值, 分别是 1, -1, 0, -0, NaN. 代表的各是正数, 负数, 正零, 负零, NaN.
	if(isNaN(Number(num))) {
		return "NaN";
	}
	if(num>0) {
		return 1;
	}
	if(num==0) {
		return num;
	}
	return -1;
}