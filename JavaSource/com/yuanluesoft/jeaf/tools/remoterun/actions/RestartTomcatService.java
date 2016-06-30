package com.yuanluesoft.jeaf.tools.remoterun.actions;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.tools.forms.Tools;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author yuanluesoft
 *
 */
public class RestartTomcatService extends org.apache.struts.action.Action {
   
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String path = new File(Environment.getWebinfPath()).getAbsolutePath();
    	//Runtime.getRuntime().exec("\"" + path + "\\jeaf\\tools\\RestartService.exe\" \"" + path.replaceAll("\\\\", "\\\\\\\\") + "\\\\jeaf\\\\tools\\\\restartTomcat.bat\"");
    	Runtime.getRuntime().exec("\"" + path + "\\jeaf\\tools\\RestartService.exe\" \"" + path + "\\jeaf\\tools\\restartTomcat.bat\"");
    	Tools formTools = (Tools)form;
        formTools.setActionResult("Tomcat已经重新启动！");
        return mapping.findForward("result");
    }
}