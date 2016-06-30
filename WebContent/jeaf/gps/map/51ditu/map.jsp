<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>地图</title>
	<jsp:include page="/jeaf/gps/map/mapCommon.jsp"/>
	<script language="javascript" src=http://api.51ditu.com/js/maps.js></script>
	<script type="text/javascript">
	   	var map; //地图
	    var moveLsitener;
	    
		//在地图中显示坐标,由继承者实现
		function showCoordinateInMap(latitude, longitude) {
			if(!map) {
				initMap(latitude, longitude, 5);
			}
			var marker = new LTMarker(new LTPoint(longitude * 100000, latitude * 100000));
			map.addOverLay(marker); //添加该标记
		    getPlaceName(latitude, longitude, false, function(placeName) {
		   		marker.openInfoWinHtml('<div style="text-align:center;width:100%;">' + placeName + '</div>'); //在该标记上显示一个信息浮窗
	        });
		}
		
		//加载地图,用来选择城市,由继承者实现
		function loadMapForSelectCity(latitude, longitude) {
			if(!map) {
				if(latitude==-1) {
					initMap(29.296592961872214,108.90366070747379, 12);
				}
				else {
					initMap(latitude, longitude, 10);
				}
			}
			LTEvent.addListener(map,"click",onMapCLick); //在地图被点击时，执行onMapCLick函数
			LTEvent.addListener(map,"dblclick",onDblClick); //绑定事件注册
		}
	
		//初始化地图
		function initMap(latitude, longitude, zoom) {
			map = new LTMaps("divMap");
			map.centerAndZoom(new LTPoint(longitude * 100000, latitude * 100000), zoom); //将原经纬度左移5位
			map.addControl(new LTStandMapControl()); //显示比例工具条
			map.handleKeyboard();//启用键盘
			map.handleMouseScroll();//启用鼠标滚轮功能支持
			
			//显示比如尺
			var scaleControl = new LTScaleControl();
			scaleControl.units=[[1000,"km"],[1,"m"]];//设置比例尺控件的单位，默认为[[1000,"公里"],[1,"米"]]
			map.addControl(scaleControl);
		}
		
		//根据坐标解析地名
		function getPlaceName(latitude, longitude, cityOnly, callback) {
			var reg = new LTRegoLoader();
			LTEvent.bind(reg, "loaded", reg, function(obj) {
				callback.call(null, obj.t.replace(/-/g, ''));
			});
			reg.loadRego(new LTPoint(longitude * 100000, latitude * 100000));
		}
		
		//在地图被点击时执行的函数，p是点击的位置相对于地图左上角的像素位置
		function onMapCLick(p) {
			map.clearOverLays();
			//用户自己的代码
			var point = map.getClickLatLng(p);//根据像素位置p求出经纬度坐标
			map.setCenterAtLatLng(point);
			if(map.getCurrentZoom() > 9) {
				map.zoomTo(9);
			}
			getPlaceName(point.getLatitude()/100000, point.getLongitude()/100000, true, function(placeName) {
				var marker = new LTMarker(point);//创建并添加标记
				map.addOverLay(marker);
				marker.openInfoWinHtml('<div style="text-align:center;width:100%;">' + placeName + '<br/><br/><input type="button" class="button" value="确定" onclick="onCitySelected(\'' + placeName + '\')"><br/><br/></div>').setTitle("位置");	//在该标记上显示一个信息浮窗
			});
		}
	
		//定义在双击的时候执行的函数
		function onDblClick() {
			moveLsitener = LTEvent.addListener(map,"moveend",onMoveEnd); //因为系统默认双击的时候会将地图定位到中心，因此，只需要定义地图在定位到中心完成之后放大地图即可
		}
		
		//定义地图在定位到中心完成之后执行的函数
		function onMoveEnd() {
			LTEvent.removeListener(moveLsitener);//删除事件注册
			map.zoomIn();//放大地图
		}
	</script>
</head>
<body style="margin:0px; overflow:hidden; border-width: 0px;">
	<div id="divMap" style="width: 100%; height: 100%; text-align: center; font-size:12px;"></div>
</body>
</html>