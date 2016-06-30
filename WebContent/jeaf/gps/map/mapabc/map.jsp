<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>地图</title>
	<jsp:include page="/jeaf/gps/map/mapCommon.jsp"/>
	<script type="text/javascript" src="http://app.mapabc.com/apis?&t=ajaxmap&v=2.1.2&key=f6c97a7f64063cfee7c2dc2157847204d4dbf09381478e56cdaa9877eef60f28684395052a4bd6a8"></script> 
	<script type="text/javascript">
	   	var map; //地图
	    
		//在地图中显示坐标,由继承者实现
		function showCoordinateInMap(latitude, longitude) {
			if(!map) {
				initMap(latitude, longitude, 8);
			}
			//信息提示窗口
			getPlaceName(latitude, longitude, false, function(placeName) {
		    	openInfoWindow(latitude, longitude, placeName);
		    });
		}
		
		//加载地图,用来选择城市,由继承者实现
		function loadMapForSelectCity(latitude, longitude) {
			if(!map) {
				if(latitude==-1) {
					initMap(29.296592961872214, 108.90366070747379, 4);
				}
				else {
					initMap(latitude, longitude, 7);
				}
			}
			map.addEventListener(map, MOUSE_CLICK, MclickMouse);//鼠标点击事件 
		} 
		
		//单击事件,获取地址和经纬度
		function MclickMouse(param) {
			getPlaceName(param.eventY, param.eventX, false, function(placeName) {	 
				openInfoWindow(param.eventY, param.eventX, '<div style="text-align:center;width:100%;">' + placeName + '<br/><br/><input type="button" class="button" value="确定" onclick="onCitySelected(\'' + placeName + '\')"></div>');
			});
			map.setCenter(new MLngLat(param.eventX, param.eventY));	    
			if(map.getZoomLevel() < 8) {
				map.setZoomLevel(8);
			}
		}
	
		//初始化地图
		function initMap(latitude, longitude, zoom) {
			var mapOptions = new MMapOptions();//构建地图辅助类   
		    mapOptions.zoom = zoom;//设置地图zoom级别   
		    mapOptions.center = new MLngLat(longitude, latitude);   //设置地图中心点   
		    mapOptions.toolbar = DEFAULT;//工具条   
		    mapOptions.toolbarPos = new MPoint(15,15);  //工具条位置   
		    mapOptions.overviewMap = SHOW;//是否显示鹰眼   
		    mapOptions.scale = SHOW;//是否显示比例尺   
		    mapOptions.returnCoordType = COORD_TYPE_OFFSET;//返回数字坐标   
		    mapOptions.zoomBox = true;//鼠标滚轮缩放和双击放大时是否有红框动画效果。   
		    map = new MMap("divMap", mapOptions); //地图初始 
		}
		
		//显示消息窗口
		function openInfoWindow(latitude, longitude, info) {
			var tipOption = new MTipOptions();
			//tipOption.title = "地址";
			tipOption.content = info;
			tipOption.tipType = IMG_BUBBLE_TIP;
			var markerOptions = new MMarkerOptions();   
		    markerOptions.canShowTip = true;
		    markerOptions.imageAlign = BOTTOM_CENTER;
		    markerOptions.imageUrl = "http://code.mapabc.com/images/lan_1.png";
		    markerOptions.tipOption = tipOption;     
			var marker = new MMarker(new MLngLat(longitude, latitude), markerOptions);   
		    marker.id = "addressMarker";
		    map.addOverlay(marker, false);
		    map.openOverlayTip("addressMarker"); //打开信息框
		}
	</script>
</head>
<body style="margin:0px; overflow:hidden; border-width: 0px;">
	<div id="divMap" style="width: 100%; height: 100%; text-align: center; font-size:12px;"></div>
</body>
</html>