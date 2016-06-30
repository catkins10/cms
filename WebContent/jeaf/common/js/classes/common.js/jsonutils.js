JsonUtils = function() {

};
//判断对象是否相同
JsonUtils.isEqual = function(obj0, obj1) {
	if(!obj0 && !obj1) {
		return true;
	}
	if(!obj0 || !obj1) {
		return false;
	}
	return JsonUtils._compareAttributes(obj0, obj1) && JsonUtils._compareAttributes(obj1, obj0);
};
//属性比较
JsonUtils._compareAttributes = function(obj0, obj1) {
	for(var key in obj0){
		if(key=='uuid') {
			if(obj0.uuid.substring(0, obj0.uuid.indexOf('@'))!=obj1.uuid.substring(0, obj1.uuid.indexOf('@'))) {
				return false;
			}
		}
		else if(!eval('obj0.' + key + '==obj1.' + key)) {
			return false;
		}
	}
	return true;
};
//添加一个JSON对象
JsonUtils.addJsonObject = function(jsonObjectPool, className, jsonObject) {
    jsonObject.uuid = className + "@" + ("" + Math.random()).substring(2);
    jsonObjectPool.push(jsonObject);
    return jsonObject;
};
//json对象转换为文本
JsonUtils.stringify = function(jsonObject) {
	try {
		return JSON.stringify(jsonObject);
	}
	catch(e) {
	
	}
	return JsonUtils._doStringify(jsonObject);
};
JsonUtils._doStringify = function(jsonObject) {
	var jsonText = '{';
	var firstKey = true;
	for(var key in jsonObject) {
		jsonText += (firstKey ? "" : ",") + "\"" + key + "\":";
		firstKey = false;
		var value = eval('jsonObject.' + key);
		jsonText += JsonUtils._doStringifyValue(value);
	}
	return jsonText + "}";
};
JsonUtils._doStringifyValue = function(value) {
	if(value==null) {
		return "null";
	}
	else if((typeof value)=="string") { //文本
		return "\"" + value.replace(/\\/g, "\\\\").replace(/"/g, "\\\"").replace(/\r/g, "\\r").replace(/\n/g, "\\n").replace(/\t/g, "\\t").replace(/\f/g, "\\f").replace(/\0/g, "\\0") + "\"";
	}
	else if((typeof value)=="number" || (typeof value)=="boolean" || (value instanceof Number) || (value instanceof Boolean)) { //数字或布尔
		return value;
	}
	else if(value instanceof Date) { //日期
		return "\"" + value.format("yyyy-MM-dd HH:mm:ss") + "\"";
	}
	else if(value.push && value.pop) { //数组
		var jsonText = "[";
		for(var i=0; i < value.length; i++) {
			jsonText += (i==0 ? "" : ",") + JsonUtils._doStringifyValue(value[i]);
		}
		return jsonText + "]";
	}
	else if((typeof value)=="object") { //对象
		return JsonUtils._doStringify(value);
	}
	return "null";
};