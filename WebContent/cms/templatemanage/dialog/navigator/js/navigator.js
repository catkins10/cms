//获取导航栏名称列表,用\0分隔
function getNavigator(element) {
	while(element.tagName.toLowerCase()!="body") {
		if(element.id=="navigator") {
			return element;
		}
		element = element.parentElement;
	}
	return null;
}