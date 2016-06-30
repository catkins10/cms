package com.yuanluesoft.chd.evaluation.actions.admin.dataview;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.chd.evaluation.forms.admin.DataView;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationDirectory;
import com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewAction;

/**
 * 
 * @author linchuan
 *
 */
public class DataViewAction extends ViewFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplicationName(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "chd/evaluation";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "admin/data";
	}
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		DataView dataViewForm = (DataView)viewForm;
		EvaluationDirectoryService evaluationDirectoryService = (EvaluationDirectoryService)getService("chdEvaluationDirectoryService");
	    //获取用户对目录的权限
	    List popedoms = evaluationDirectoryService.listDirectoryPopedoms(dataViewForm.getDirectoryId(), sessionInfo);
	    if(popedoms==null || popedoms.isEmpty()) { //用户对当前目录没有任何权限
	    	view.addWhere("ChdEvaluationData.id=0"); //不显示文件
	    	return;
	    }
	    if(dataViewForm.getDirectoryId()!=0) { //不是根目录
	    	view.addJoin(", ChdEvaluationDirectorySubjection ChdEvaluationDirectorySubjection");
			String where = "subjections.directoryId=ChdEvaluationDirectorySubjection.directoryId" +
						   " and ChdEvaluationDirectorySubjection.parentDirectoryId=" +  dataViewForm.getDirectoryId();
			view.addWhere(where);
		}
	    ChdEvaluationDirectory directory = (ChdEvaluationDirectory)evaluationDirectoryService.getDirectory(dataViewForm.getDirectoryId()); //获取目录信息
		if(view.getActions()==null) {
	        view.setActions(new ArrayList());
	    }
	    if(popedoms.contains("fileUpload") && "plantRule".equals(directory.getDirectoryType())) { //用户是资料提供者,且当目录是评价项目（发电企业）
	    	//添加"上传文件"按钮
		    ViewAction viewAction = new ViewAction();
		    viewAction.setTitle("上传文件");
		    viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/chd/evaluation/admin/file.shtml?act=create&ruleId=" + directory.getId() + "', 470, 300)");
		    view.getActions().add(viewAction);
		}
	    
	    if(popedoms.contains("declare") && "plant".equals(directory.getDirectoryType())) { //用户是申报人,且当目录是发电企业
	    	//添加"申报星级企业"按钮
		    ViewAction viewAction = new ViewAction();
		    viewAction.setTitle("申报星级企业");
		    viewAction.setExecute("PageUtils.openurl('" + Environment.getContextPath() + "/chd/evaluation/admin/declare.shtml?act=create&plantId=" + directory.getId() + "', 'mode=fullscreen', 'declare')");
		    view.getActions().add(viewAction);
		}
		//添加"修改..."按钮
    	ViewAction viewAction = new ViewAction();
	    viewAction.setTitle((popedoms.contains("manager") ? "修改" : "查看") + evaluationDirectoryService.getDirectoryTypeTitle(directory.getDirectoryType()));
	    viewAction.setExecute("PageUtils.editrecord('chd/evaluation', 'admin/" + directory.getDirectoryType() + "', '" + directory.getId() + "', 'mode=fullscreen')");
	    view.getActions().add(viewAction);
	    if(popedoms.contains("manager")) { //用户是管理员
    	    //添加"新建..."按钮
    	    if("company".equals(directory.getDirectoryType())) { //公司
    	    	if(directory.getId()==0) { //根目录
    	    		viewAction = new ViewAction();
        		    viewAction.setTitle("添加发电企业类型");
        		    viewAction.setExecute("PageUtils.openurl('" + Environment.getContextPath() + "/chd/evaluation/admin/plantType.shtml?act=create&parentDirectoryId=" + directory.getId() + "', 'mode=fullscreen', 'plantType')");
        		    view.getActions().add(viewAction);
    	    	}
    		    viewAction = new ViewAction();
    		    viewAction.setTitle("添加发电企业");
    		    viewAction.setExecute("PageUtils.openurl('" + Environment.getContextPath() + "/chd/evaluation/admin/plant.shtml?act=create&parentDirectoryId=" + directory.getId() + "', 'mode=fullscreen', 'plant')");
    		    view.getActions().add(viewAction);

    		    viewAction = new ViewAction();
    		    viewAction.setTitle("添加分公司/子公司");
    		    viewAction.setExecute("PageUtils.openurl('" + Environment.getContextPath() + "/chd/evaluation/admin/company.shtml?act=create&parentDirectoryId=" + directory.getId() + "', 'mode=fullscreen', 'company')");
    		    view.getActions().add(viewAction);
    	    }
    	    else if("plantType".equals(directory.getDirectoryType()) || "rule".equals(directory.getDirectoryType())) { //发电企业类型、评价项目
    	    	viewAction = new ViewAction();
    		    viewAction.setTitle("添加评价项目");
    		    viewAction.setExecute("PageUtils.openurl('" + Environment.getContextPath() + "/chd/evaluation/admin/rule.shtml?act=create&parentDirectoryId=" + directory.getId() + "', 'mode=fullscreen', 'rule')");
    		    view.getActions().add(viewAction);
    		    if("rule".equals(directory.getDirectoryType())) {
    		    	viewAction = new ViewAction();
        		    viewAction.setTitle("添加评价细则");
        		    viewAction.setExecute("PageUtils.openurl('" + Environment.getContextPath() + "/chd/evaluation/admin/detail.shtml?act=create&parentDirectoryId=" + directory.getId() + "', 'mode=fullscreen', 'detail')");
        		    view.getActions().add(viewAction);
    		    }
    	    }
		    
		    //添加"调整优先级"按钮
    	    viewAction = new ViewAction();
		    viewAction.setTitle("调整优先级");
		    viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/chd/evaluation/admin/adjustDirectoryPriority.shtml?parentDirectoryId=" + directory.getId() + "', 640, 400)");
		    view.getActions().add(viewAction);
	    }
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetViewLocation(com.yuanluesoft.jeaf.view.forms.ViewForm, java.util.List, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetViewLocation(ViewForm viewForm, List location, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetViewLocation(viewForm, location, view, sessionInfo, request);
		EvaluationDirectoryService evaluationDirectoryService = (EvaluationDirectoryService)getService("chdEvaluationDirectoryService");
		DataView dataViewForm = (DataView)viewForm;
		location.remove(location.size()-1);
		String fullName = evaluationDirectoryService.getDirectoryFullName(dataViewForm.getDirectoryId(), "###", "company,plant");
		if(fullName!=null) {
			String[] names = fullName.split("###");
			for(int i=0; i<names.length; i++) {
				location.add(StringUtils.slice(names[i], 20, "..."));
			}
		}
		location.add("资料");
	}
}