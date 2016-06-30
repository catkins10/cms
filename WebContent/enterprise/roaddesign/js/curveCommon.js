//获取曲线名称
function getCurveName(curveType) {
    if(curveType==11) {
    	return "切基双交点曲线";
	}
    if(curveType==12) {
    	return "切基双交点曲线";
    }
	if(curveType==21) {
		return "与基线不相切双交点复曲线";
	}
	if(curveType==22) {
		return "与基线不相切双交点复曲线";
	}
	if(curveType==31) {
		return "卵型曲线";
	}
    if(curveType==32) {
		return "卵型曲线";
	}
    return "单圆曲线";
}