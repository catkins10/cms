<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script src="<%=request.getContextPath()%>/jeaf/common/js/common.js" type="text/javascript"></script>
<script type="text/javascript">
	//在地图中显示坐标,由继承者实现
	function showCoordinateInMap(latitude, longitude) {
	
	}
	
	//加载地图,用来选择城市,由继承者实现
	function loadMapForSelectCity(latitude, longitude) {
	
	}
	
	var latitude = <ext:write name="map" property="latitude"/>; //纬度
	var longitude = <ext:write name="map" property="longitude"/>; //经度
	window.onload = function() {
		var queryString = location.search;
		if(<ext:write name="map" property="selectCity"/>) { //选择城市
			doLoadMapForSelectCity();
		}
		else if(latitude!=-1) {
			showCoordinateInMap(latitude, longitude);
		}
		//显示坐标
		window.showCoordinate = function(latitude, longitude) {
			showCoordinateInMap(latitude, longitude);
		};
	};
	
	//执行加载地图,如果出错1秒后重试
	function doLoadMapForSelectCity() {
		try {
			loadMapForSelectCity(latitude, longitude);
		}
		catch(e) {
			window.setTimeout(doLoadMapForSelectCity, 1000);
		}
	}
	
	//完成城市的选择
	function onCitySelected(placeName) {
		var queryString = location.search;
	   	var fieldName = '<ext:write name="map" property="fieldName"/>';
	   	var script = '<ext:write name="map" property="script" filter="false"/>';
	   	var opener;
	   	try {
	   		opener = DialogUtils.getDialogOpener();
	   	}
	   	catch(e) {
	   		opener = window.parent;
	   	}
	   	if(fieldName!='') {
	   		var field = opener.document.getElementsByName(fieldName)[0];
	   		if(field) {
	   			field.value = placeName;
	   		}
	   	}
	   	if(script!='') {
	   		script = script.replace('\{city\}', placeName);
	   		opener.setTimeout(script, 10);
	   	}
	   	if('dialog'=='<ext:write name="map" property="displayMode"/>') {
	   		DialogUtils.closeDialog();
	   	}
	}
	
	//按经纬度查询地名,callback函数:function(placeName);
	var getPlaceNameCallback;
	function getPlaceName(latitude, longitude, cityOnly, callback) {
		getPlaceNameCallback = callback;
		ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + '/jeaf/gps/placeName.js.shtml?latitude=' + latitude + '&longitude=' + longitude + '&cityOnly=' + cityOnly, 'getPlaceNameScript');
	}
</script>