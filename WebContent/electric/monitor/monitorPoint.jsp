<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/monitorPoint">
	<script src="<%=request.getContextPath()%>/dvs/js/dvsplugin.js"></script>
	<object id="dvsPlugin" classid="clsid:40D0015C-193B-4B18-A468-6C452D11C3D3" style="width:400; height:300;">
		<param name="dvsInfo" value="<ext:write property="dvsInfo"/>">
	</object>
	<br>
	<input type="button" value="上" onmousedown="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_UP_CONTROL, 6, 0);" onmouseup="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_UP_CONTROL, 6, 1);"/>
	<input type="button" value="下" onmousedown="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_DOWN_CONTROL, 6, 0);" onmouseup="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_DOWN_CONTROL, 6, 1);"/>
	<input type="button" value="左" onmousedown="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_LEFT_CONTROL, 6, 0);" onmouseup="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_LEFT_CONTROL, 6, 1);"/>
	<input type="button" value="右" onmousedown="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_RIGHT_CONTROL, 6, 0);" onmouseup="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_RIGHT_CONTROL, 6, 1);"/>
	<input type="button" value="变倍+" onmousedown="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_ZOOM_ADD_CONTROL, 6, 0);" onmouseup="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_ZOOM_ADD_CONTROL, 6, 1);"/>
	<input type="button" value="变倍-" onmousedown="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_ZOOM_DEC_CONTROL, 6, 0);" onmouseup="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_ZOOM_DEC_CONTROL, 6, 1);"/>
	<input type="button" value="调焦+" onmousedown="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_FOCUS_ADD_CONTROL, 6, 0);" onmouseup="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_FOCUS_ADD_CONTROL, 6, 1);"/>
	<input type="button" value="调焦-" onmousedown="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_FOCUS_DEC_CONTROL, 6, 0);" onmouseup="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_FOCUS_DEC_CONTROL, 6, 1);"/>
	<input type="button" value="光圈+" onmousedown="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_APERTURE_ADD_CONTROL, 6, 0);" onmouseup="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_APERTURE_ADD_CONTROL, 6, 1);"/>
	<input type="button" value="光圈-" onmousedown="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_APERTURE_DEC_CONTROL, 6, 0);" onmouseup="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_APERTURE_DEC_CONTROL, 6, 1);"/>
	<input type="button" value="转至预置点" onmousedown="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_POINT_MOVE_CONTROL, 128, 0);" onmouseup="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_POINT_MOVE_CONTROL, 6, 1);"/>
	<input type="button" value="设置预置点" onmousedown="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_POINT_SET_CONTROL, 128, 0);" onmouseup="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_POINT_SET_CONTROL, 6, 1);"/>
	<input type="button" value="删除预置点" onmousedown="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_POINT_DEL_CONTROL, 128, 0);" onmouseup="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_POINT_DEL_CONTROL, 6, 1);"/>
	<input type="button" value="点间巡航" onmousedown="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_POINT_LOOP_CONTROL, 6, 1);" onmouseup="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_POINT_LOOP_CONTROL, 6, 1);"/>
	<input type="button" value="灯光雨刷" onmousedown="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_LAMP_CONTROL, 6, 1);" onmouseup="PTZControl(document.getElementById('dvsPlugin'), 0, RV_PTZ_LAMP_CONTROL, 6, 1);"/>
	<input type="button" value="开始录像" onmousedown="startRecord(document.getElementById('dvsPlugin'), 0, 'C:\\Users\\linchuan\\AppData\\LocalLow\\dvs.h264');"/>
	<input type="button" value="停止录像" onmousedown="stopRecord(document.getElementById('dvsPlugin'), 0);"/>
	<input type="button" value="抓图" onmousedown="capturePicture(document.getElementById('dvsPlugin'), 0, 'C:\\Users\\linchuan\\AppData\\LocalLow\\dvs.bmp');"/>
</ext:form>