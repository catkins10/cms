package com.yuanluesoft.telex.base.actions.selectunit;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.telex.base.forms.SelectUnit;
import com.yuanluesoft.telex.base.model.TelegramUnit;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SelectUnit selectForm = (SelectUnit)form;
        OrgService orgService = getOrgService();
        //获取第一级单位分类列表
        List telegramUnits = orgService.listChildDirectories(0, "category", null, null, false, false, null, 0, 0);
        //转换为TelegramUnit模型
        if(telegramUnits!=null) {
        	for(int i=0; i<telegramUnits.size(); i++) {
        		Org category = (Org)telegramUnits.get(i);
        		TelegramUnit telegramUnit = new TelegramUnit();
        		telegramUnit.setCategory(category.getDirectoryName());
        		//获取单位列表
        		telegramUnit.setUnits(orgService.listChildDirectories(category.getId(), "unit", null, null, false, false, null, 0, 0));
        		telegramUnits.set(i, telegramUnit);
        	}
        }
        selectForm.setTelegramUnits(telegramUnits);
        return mapping.findForward("load");
    }
}