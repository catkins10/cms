ListUtils = function() {

};
//按属性从列表中查找对象
ListUtils.findObjectByProperty = function(list, propertyName, propertyValue) {
	var objects = ListUtils.findObjectsByProperties(list, [{name:propertyName, value:propertyValue}], 1);
	return objects ? objects[0] : null;
};
//按属性从列表中查找对象列表
ListUtils.findObjectsByProperty = function(list, propertyName, propertyValue) {
	return ListUtils.findObjectsByProperties(list, [{name:propertyName, value:propertyValue}], 0);
};
//按属性列表从列表中查找对象,properties:[{name:属性1名称, value:属性1值},...,{name:属性n名称, value:属性n值}]
ListUtils.findObjectsByProperties = function(list, properties, max) {
	var objects = [];
	for(var i=0; i < (list ? list.length : 0) && (max <= 0 || objects.length < max); i++) {
		var j = 0;
		for(; j < properties.length; j++) {
			var value;
			eval('try{value=list[i].' + properties[j].name + ';}catch(e){}');
			if(value!=properties[j].value) {
				break;
			}
		}
		if(j == properties.length) {
			objects.push(list[i]);
		}
	}
	return objects.length==0 ? null : objects;
};
//按类型从列表中查找对象列表
ListUtils.findObjectsByType = function(list, type, max) {
	var objects = [];
	for(var i=0; i < (list ? list.length : 0) && (max <= 0 || objects.length < max); i++) {
		var match = eval('list[i] instanceof ' + type);
		if(match) {
			objects.push(list[i]);
		}
	}
	return objects.length==0 ? null : objects;
};
//定位对象
ListUtils.indexOf = function(list, object) {
	for(var i=0; i<(list ? list.length : 0); i++) {
		if(list[i]==object) {
			return i;
		}
	}
	return -1;
};
//按属性从列表中查找对象
ListUtils.removeObject = function(list, object) {
	var index = ListUtils.indexOf(list, object);
	if(index!=-1) {
		list.splice(index, 1);
	}
};
//按属性从列表中删除对象
ListUtils.removeObjectByProperty = function(list, propertyName, propertyValue) {
	return ListUtils.removeObjectByProperties(list, [{name:propertyName, value:propertyValue}]);
};
//按属性从列表中删除对象
ListUtils.removeObjectByProperties = function(list, properties) {
	for(var i=(list ? list.length - 1 : -1); i>=0; i--) {
		var j = 0;
		for(; j < properties.length; j++) {
			var value;
			eval('try{value=list[i].' + properties[j].name + ';}catch(e){}');
			if(value!=properties[j].value) {
				break;
			}
		}
		if(j == properties.length) {
			var obj = list[i];
			list.splice(i, 1);
			return obj;
		}
	}
};