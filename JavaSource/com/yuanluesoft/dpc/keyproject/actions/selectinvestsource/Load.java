package com.yuanluesoft.dpc.keyproject.actions.selectinvestsource;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.dpc.keyproject.forms.SelectInvestSource;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectInvestSource;
import com.yuanluesoft.dpc.keyproject.service.KeyProjectService;
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
    	SelectInvestSource selectForm = (SelectInvestSource)dialog;
    	selectForm.setTitle("资金来源");
    	KeyProjectService keyProjectService = (KeyProjectService)getService("keyProjectService");
    	List investSources = keyProjectService.listKeyProjectInvestSources();
    	Tree tree = new Tree("资金来源", "资金来源", "root", null);
    	for(Iterator iterator = (investSources==null ? null : investSources.iterator()); iterator!=null && iterator.hasNext();) {
    		KeyProjectInvestSource investSource = (KeyProjectInvestSource)iterator.next();
    		if(investSource.getSource()==null || investSource.getSource().equals("")) {
    			continue;
    		}
    		boolean hasChildren = (investSource.getChildSource()!=null && !investSource.getChildSource().equals(""));
    		TreeNode node = tree.appendChildNode(investSource.getSource(), investSource.getSource(), "investSource", null, hasChildren);
    		if(hasChildren) {
    			String[] children = investSource.getChildSource().split(",");
    			for(int i=0; i<children.length; i++) {
    				node.appendChildNode(children[i], children[i], "childSource", null, false);
    			}
    		}
    	}
    	selectForm.setTree(tree);
    }
}