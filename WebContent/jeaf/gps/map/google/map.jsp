<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>地图</title>
	<jsp:include page="/jeaf/gps/map/mapCommon.jsp"/>
	<script src="http://maps.google.com/maps/api/js?v=3.1&sensor=false&language=cn" type="text/javascript"></script>
	<script type="text/javascript">
	    var map; //地图
	    var infoWindow; //信息窗口,用来显示提示信息
	    var oldMarker = null; //坐标标注
	    var geocoder; //地名解析
		//在地图中显示坐标,由继承者实现
		function showCoordinateInMap(latitude, longitude) {
			if(!map) {
				initMap(latitude, longitude, 8);
			}
			var latlng = new google.maps.LatLng(latitude, longitude);
	        map.setCenter(latlng);
	        if(oldMarker!=null) { //上一次查询地址是否为空，为空则清除地图上的旧标记
               	oldMarker.setMap(null);
            }
            marker = new google.maps.Marker({ //marker标签
                position: latlng, //定义标记的位置
                map: map //定义在map中显示标记
            });
            oldMarker = marker;
	        getPlaceName(latitude, longitude, false, function(placeName) {
	    		infoWindow.setContent(placeName); //设置信息的内容,也就是地名
	            infoWindow.open(map, marker); //打开信息窗口,一般与map和标记关联
            });
		}
		
		//加载地图,用来选择城市,由继承者实现
		function loadMapForSelectCity(latitude, longitude) {
			if(!map) {
				if(latitude==-1) {
					initMap(29.296592961872214,108.90366070747379, 5);
				}
				else {
					initMap(latitude, longitude, 7);
				}
			}
			google.maps.event.addListener(map, "click", function(event) {
	            if(oldMarker != null) {
		        	oldMarker.setMap(null);
		        }
		        var marker = new google.maps.Marker({ position: event.latLng, map: map });
		        oldMarker = marker;
		        map.setCenter(event.latLng);
	            getPlaceName(event.latLng.lat(), event.latLng.lng(), true, function(placeName) {
	                if(map.getZoom()<8) {
	                	map.setZoom(8); //分辨率
	                }
	                address = placeName;
	                infoWindow.setContent(placeName + '<br/><br/><input type="button" class="button" value="确定" onclick="onCitySelected(\'' + placeName + '\')"/>'); //设置信息的内容,也就是地名
		            infoWindow.open(map, marker); //打开信息窗口,一般与map和标记关联
	            });
	        });
		}
	
		//初始化地图
		function initMap(latitude, longitude, zoom) {
			//初始化地图
	        var myOptions = {
	            zoom: zoom, //地图缩放级别
	            center: new google.maps.LatLng(latitude, longitude), //控制地图中心显示的坐标
	            mapTypeId: google.maps.MapTypeId.ROADMAP //地图显示的类型。有地图(ROADMAP)、卫星图片(SATELLITE)、混合(HYBRID)、地形(TERRAIN)四种类型
	        }
		    map = new google.maps.Map(document.getElementById("divMap"), myOptions); //创建地图
		    infoWindow = new google.maps.InfoWindow(); //初始化一个信息窗口，用来显示提示信息
		}
		
		//根据坐标解析地名
		function getPlaceName(latitude, longitude, cityOnly, callback) {
			if(!geocoder) {
				geocoder = new google.maps.Geocoder();
			}
			var latlng = new google.maps.LatLng(latitude, longitude);
			geocoder.geocode({"latLng": latlng }, function (results, status) {
	            if(status == google.maps.GeocoderStatus.OK) {
	                if(results[1]) {
	                	callback.call(null, results[1].formatted_address.replace("中国", ""));
	                }
	            }
	        });
		}
	</script>
</head>
<body style="margin:0px; overflow:hidden; border-width: 0px;">
	<div id="divMap" style="width: 100%; height: 100%; text-align: center; font-size:12px;"></div>
</body>
</html>