package com.yuanluesoft.jeaf.gps.actions.gpslocationjs;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.gps.model.Location;
import com.yuanluesoft.jeaf.gps.pojo.GpsLocation;
import com.yuanluesoft.jeaf.gps.service.GpsService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	response.setContentType("text/javascript");
    	response.setCharacterEncoding("utf-8");
    	PrintWriter writer = response.getWriter();
    	long recordId = RequestUtils.getParameterLongValue(request, "recordId");
    	//GPS定位
    	GpsService gpsService = (GpsService)getService("gpsService");
    	Location location = gpsService.getLocation(RequestUtils.getParameterStringValue(request, "gpsTerminalNumber"), true);
    	if(location==null) {
    		String[] fields = {"coordinate.latitude", "coordinate.longitude", "speed", "angle", "altitude", "placeName", "created"};
    		for(int i=0; i<fields.length; i++) {
    			writeLocationScript(writer, recordId, fields[i], null, true);
    		}
    		return null;
    	}
    	
		//保存位置
    	DatabaseService databaseService = (DatabaseService)getService("databaseService");
    	GpsLocation gpsLocation = (GpsLocation)databaseService.findRecordByHql("from GpsLocation GpsLocation where GpsLocation.recordId=" + recordId);
    	if(gpsLocation!=null) {
			gpsLocation.setPlaceName(location.getPlaceName()); //位置
			databaseService.updateRecord(gpsLocation);
		}
    	else {
			gpsLocation = new GpsLocation();
			gpsLocation.setId(UUIDLongGenerator.generateId()); //ID
			gpsLocation.setRecordId(recordId); //主记录ID
			gpsLocation.setPlaceName(location.getPlaceName()); //位置
			databaseService.saveRecord(gpsLocation);
		} 
    	
    	//输出脚本
    	writeLocationScript(writer, recordId, "coordinate.latitude", "" + location.getCoordinate().getLatitude(), false); //纬度
    	writeLocationScript(writer, recordId, "coordinate.longitude", "" + location.getCoordinate().getLongitude(), false); //经度
    	writeLocationScript(writer, recordId, "speed", location.getSpeed(), false); //行驶速度
    	writeLocationScript(writer, recordId, "angle", location.getAngle(), false); //方向角度
    	writeLocationScript(writer, recordId, "altitude", "" + location.getAltitude(), false); //海拔高度
    	writeLocationScript(writer, recordId, "placeName", location.getPlaceName()==null ? "未知" : location.getPlaceName(), false); //地名
    	writeLocationScript(writer, recordId, "created", DateTimeUtils.formatTimestamp(location.getCreated(), null), false); //查询定位的时间
    	//在地图中标注
    	String script = "var mapFrame = document.frames['map'];";
    	script += "if(mapFrame){mapFrame.showCoordinate(" + location.getCoordinate().getLatitude() + ", " + location.getCoordinate().getLongitude() + ");}";
    	writer.write(script);
    	return null;
    }
    
    /**
     * 输出脚本
     * @param writer
     * @param recordId
     * @param fieldName
     * @param fieldValue
     */
    private void writeLocationScript(PrintWriter writer, long recordId, String fieldName, String fieldValue, boolean failed) {
    	String script = "var locationField = document.getElementById('location." + fieldName + "_" + recordId + "');";
    	if(!failed) {
    		script += "if(locationField){locationField.innerHTML='" + fieldValue + "';locationField.style.visibility='visible';}";
    	}
    	else {
    		script += "if(locationField && locationField.innerHTML=='正在定位...')locationField.innerHTML='定位失败';";
    	}
    	writer.write(script);
    }
}