package com.yuanluesoft.dpc.investmentproject.actions.selectindustry;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.dpc.investmentproject.forms.SelectIndustry;
import com.yuanluesoft.dpc.investmentproject.pojo.InvestmentProjectIndustry;
import com.yuanluesoft.dpc.investmentproject.service.InvestmentProjectService;
import com.yuanluesoft.jeaf.dialog.actions.listdialog.OpenListDialog;
import com.yuanluesoft.jeaf.dialog.forms.SelectDialog;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.tree.model.TreeNode;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends OpenListDialog {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogAction#initDialog(com.yuanluesoft.jeaf.dialog.forms.SelectDialog, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initDialog(SelectDialog dialog, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initDialog(dialog, sessionInfo, request);
		SelectIndustry selectForm = (SelectIndustry)dialog;
    	selectForm.setTitle("行业");
    	InvestmentProjectService investmentProjectService = (InvestmentProjectService)getService("investmentProjectService");
    	List industries = investmentProjectService.listIndustries();
    	Tree tree = new Tree("行业", "行业", "root", null);
    	for(Iterator iterator = (industries==null ? null : industries.iterator()); iterator!=null && iterator.hasNext();) {
    		InvestmentProjectIndustry industry = (InvestmentProjectIndustry)iterator.next();
    		if(industry.getIndustry()==null || industry.getIndustry().equals("")) {
    			continue;
    		}
    		boolean hasChildren = (industry.getChildIndustry()!=null && !industry.getChildIndustry().equals(""));
    		TreeNode node = tree.appendChildNode(industry.getIndustry(), industry.getIndustry(), "industry", null, hasChildren);
    		if(hasChildren) {
    			String[] children = industry.getChildIndustry().split(",");
    			for(int i=0; i<children.length; i++) {
    				node.appendChildNode(children[i], children[i], "childIndustry", null, false);
    			}
    		}
    	}
    	selectForm.setTree(tree);
	}
}