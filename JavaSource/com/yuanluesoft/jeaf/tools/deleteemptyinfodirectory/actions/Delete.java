package com.yuanluesoft.jeaf.tools.deleteemptyinfodirectory.actions;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.infopublic.pojo.PublicInfoDirectory;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Delete extends BaseAction {
       
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(!request.getServerName().equals("localhost")) { //只允许在服务器上操作
    		throw new Exception("localhost only");
    	}
    	PublicInfoService publicInfoService = (PublicInfoService)getService("publicInfoService");
    	PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getService("publicDirectoryService");
    	DatabaseService databaseService = (DatabaseService)getService("databaseService");
    	//获取主目录列表
    	String hql = "select PublicMainDirectory.id from PublicMainDirectory PublicMainDirectory where PublicMainDirectory.parentDirectoryId=20226538026440000 order by PublicMainDirectory.id";
    	List mainDirectories = databaseService.findRecordsByHql(hql);
    	PrintWriter out = response.getWriter();
    	response.setCharacterEncoding("utf-8");
    	for(Iterator iterator = mainDirectories.iterator(); iterator.hasNext();) {
    		Number mainDirectoryId = (Number)iterator.next();
    		//获取信息目录
    		hql = "from PublicInfoDirectory PublicInfoDirectory where PublicInfoDirectory.parentDirectoryId=" + mainDirectoryId;
    		List infoDirectories = databaseService.findRecordsByHql(hql, ListUtils.generateList("subjections"));
    		if(infoDirectories==null || infoDirectories.isEmpty()) {
    			continue;
    		}
    		for(Iterator iteratorDirectory = infoDirectories.iterator(); iteratorDirectory.hasNext();) {
    			PublicInfoDirectory infoDirectory = (PublicInfoDirectory)iteratorDirectory.next();
    			if(infoDirectory.getDirectoryName().indexOf("其它")==-1 &&
    			   infoDirectory.getDirectoryName().indexOf("单位概况")==-1 &&
    			   infoDirectory.getDirectoryName().indexOf("工作动态")==-1 &&
    			   publicInfoService.getInfosCount("" + infoDirectory.getId(), false)==0) { //检查信息数量
    				out.println(infoDirectory.getDirectoryName() + "<br/>");
    				publicDirectoryService.delete(infoDirectory);
    			}
    		}
    	}
    	return null;
    }
}