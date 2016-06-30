/*
 * Created on 2006-5-12
 *
 */
package com.yuanluesoft.jeaf.view.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.report.excel.ExcelReportService;
import com.yuanluesoft.jeaf.report.pdf.PdfReportService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;
import com.yuanluesoft.jeaf.view.util.ViewUtils;
import com.yuanluesoft.jeaf.view.viewcustomize.service.ViewCustomizeService;

/**
 *
 * @author linchuan
 *
 */
public abstract class ViewFormAction extends BaseAction {
	
	/**
	 * 获取视图所在应用名称
	 * @param viewForm
	 * @param request
	 * @return
	 */
	public abstract String getViewApplicationName(ViewForm viewForm, HttpServletRequest request);
	
	/**
	 * 获取视图名称
	 * @param viewForm
	 * @param request
	 * @return
	 */
	public abstract String getViewName(ViewForm viewForm, HttpServletRequest request);
	    
	/**
	 * 初始化
	 * @param viewForm
	 * @param request
	 * @param sessionInfo
	 * @throws Exception
	 */
    public void initForm(ViewForm viewForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
    	if("get".equalsIgnoreCase(request.getMethod())) {
    		setFieldDefaultValue(viewForm, request); //设置字段默认值
    	}
	}
    
    /**
     * 权限控制
     * @param viewForm
     * @param request
     * @param sessionInfo
     * @throws PrivilegeException
     */
    public void checkPrivilege(ViewForm viewForm, HttpServletRequest request, SessionInfo sessionInfo) throws PrivilegeException {
    	
    }
	
    /**
     * 重置视图
     * @param viewForm
     * @param view
     * @param sessionInfo
     * @param request
     * @throws Exception
     */
    public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
    	
    }
    
    /**
     * 重置视图位置
     * @param viewForm
     * @param location
     * @param view
     * @param sessionInfo
     * @param request
     * @throws Exception
     */
    public void resetViewLocation(ViewForm viewForm, List location, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
    	
    }
	
	/**
	 * 执行自定义操作
	 * @param mapping
	 * @param viewForm
	 * @param request
	 * @param response
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		return mapping.findForward("load");
	}
	
    /**
     * 打印,生成PDF
     * @param form
     * @param request
     * @param response
     * @throws Exception
     */
	protected void printAsPdf(ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		((PdfReportService)getService("pdfReportService")).createPdfReport(viewForm.getViewPackage().getView(), viewForm.getViewPackage().getCategories(), (viewForm.getViewPackage().isSearchMode() ? viewForm.getViewPackage().getSearchConditions() : null), request, response, sessionInfo);
    }
    
    /**
     * 导成电子表格
     * @param form
     * @param request
     * @param response
     * @param sessionInfo
     * @throws Exception
     */
	protected void printAsExcel(ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		((ExcelReportService)getService("excelReportService")).createExcelReport(viewForm.getViewPackage().getView(), viewForm.getViewPackage().getCategories(), (viewForm.getViewPackage().isSearchMode() ? viewForm.getViewPackage().getSearchConditions() : null), request, response, sessionInfo);
    }
	
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			//会话检查
			SessionInfo sessionInfo = getSessionInfo(request, response);
			ViewForm viewForm = (ViewForm)form;
			//权限控制
			checkPrivilege(viewForm, request, sessionInfo);
			//获取视图
	    	ViewDefineService viewDefineService = (ViewDefineService)getService("viewDefineService");
	    	View view = viewDefineService.getView(getViewApplicationName(viewForm, request), getViewName(viewForm, request), sessionInfo);
			viewForm.getViewPackage().setView(view);
	    	//初始化表单
			initForm(viewForm, request, sessionInfo);
	    	//重置视图
	    	resetView(viewForm, view, sessionInfo, request);
			ActionForward actionForward = null;
			String viewAction = viewForm.getViewPackage().getCurrentViewAction();
			if("gotoPage".equals(viewAction)) { //翻页
				int curPage = viewForm.getViewPackage().getCurPage();
		    	String page = viewForm.getViewPackage().getPage();
		    	if(page.equals("next")) {
		    	    viewForm.getViewPackage().setCurPage(curPage+1);
		    	}
		    	else if(page.equals("prev")) {
		    	    viewForm.getViewPackage().setCurPage(curPage-1);
		    	}
		    	else if(page.equals("first")) {
		    	    viewForm.getViewPackage().setCurPage(1);
		    	}
		    	else if(page.equals("last")) {
		    	    viewForm.getViewPackage().setCurPage(viewForm.getViewPackage().getPageCount());
		    	}
		    	else {
		    	    viewForm.getViewPackage().setCurPage(new Integer(page).intValue());
		    	}
			}
			else if("sort".equals(viewAction)) { //排序
				viewForm.getViewPackage().setViewMode(View.VIEW_DISPLAY_MODE_NORMAL);
				ViewCustomizeService viewCustomizeService = (ViewCustomizeService)getService("viewCustomizeService");
				viewCustomizeService.saveSortColumn(view, viewForm.getViewPackage().getSortColumn(), sessionInfo.getUserId());
			}
			else if("refreshView".equals(viewAction)) { //刷新
				viewForm.getViewPackage().setViewMode(View.VIEW_DISPLAY_MODE_NORMAL);
				viewForm.getViewPackage().setRecordCount(0); //置0,使重新计算记录数
			}
			else if("switchToSearch".equals(viewAction)) { //切换到搜索
				viewForm.getViewPackage().setViewMode(View.VIEW_DISPLAY_MODE_CONDITION);
				viewForm.getViewPackage().setSearchMode(true);
			}
			else if("search".equals(viewAction)) { //搜索
				viewForm.getViewPackage().setRecordCount(0);
				viewForm.getViewPackage().setViewMode(View.VIEW_DISPLAY_MODE_NORMAL);
				viewForm.getViewPackage().setCurPage(1);
		    	viewForm.getViewPackage().setRecordCount(0);
				viewForm.getViewPackage().setSearchMode(true);
			}
			else if("finishSearch".equals(viewAction)) { //结束搜索
				viewForm.getViewPackage().setViewMode(View.VIEW_DISPLAY_MODE_NORMAL);
		    	viewForm.getViewPackage().setCurPage(1);
		    	viewForm.getViewPackage().setRecordCount(0);
		    	viewForm.getViewPackage().setSearchMode(false);
			}
			else if("openCategory".equals(viewAction)) { //打开所属分类的数据
				viewForm.getViewPackage().setCurPage(1);
				viewForm.getViewPackage().setRecordCount(0);
			}
			else if("printAsPdf".equals(viewAction)) { //打印
				printAsPdf(viewForm, request, response, sessionInfo);
				return null;
			}
			else if("printAsExcel".equals(viewAction)) { //导出为电子表格格式
				printAsExcel(viewForm, request, response, sessionInfo);
				return null;
			}
			else { //其它
				viewForm.getViewPackage().setCurPage(1);
				viewForm.getViewPackage().setRecordCount(0);
				viewForm.getViewPackage().setViewMode(View.VIEW_DISPLAY_MODE_NORMAL);
				actionForward = executeCutsomAction(mapping, viewForm, request, response, sessionInfo);
			}
			//填充视图包
			ViewUtils.getViewService(view).retrieveViewPackage(viewForm.getViewPackage(), view, 0, false, false, false, request, sessionInfo);
			//重置视图位置
			resetViewLocation(viewForm, viewForm.getViewPackage().getLocation(), view, sessionInfo, request);
			if(actionForward==null && (actionForward=mapping.findForward("load"))==null) {
				actionForward = mapping.getInputForward();
			}
			return actionForward;
		}
		catch(SessionException se) {
			return redirectToLogin(this, mapping, form, request, response, se, !"get".equalsIgnoreCase(request.getMethod()));
		}
		catch(PrivilegeException pe) {
			return redirectToLogin(this, mapping, form, request, response, pe, !"get".equalsIgnoreCase(request.getMethod()));
		}
	}
}