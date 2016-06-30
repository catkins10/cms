package com.yuanluesoft.telex.base.actions.selectunit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.telex.base.forms.UnitSearch;

/**
 * 
 * @author linchuan
 *
 */
public class Search extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	UnitSearch unitSearchForm = (UnitSearch)form;
    	String hql = "from Org Org" +
    				 " where Org.directoryType='unit'" +
    				 " and Org.directoryName like '%" + unitSearchForm.getKey() + "%'" +
    				 " order by Org.directoryName";
    	DatabaseService databaseService = (DatabaseService)getService("databaseService");
    	unitSearchForm.setUnits(databaseService.findRecordsByHql(hql));
        return mapping.findForward("load");
    }
}