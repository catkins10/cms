package com.yuanluesoft.lss.cardquery.page.action;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.lss.cardquery.page.model.CardQueryModel;
import com.yuanluesoft.lss.cardquery.pojo.CardQuery;
import com.yuanluesoft.lss.cardquery.pojo.CardQueryConfig;

/**
 * 
 * @author kangshiwei
 *
 */
public class Load extends BaseAction {
	/*
	 * (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

    	CardQueryModel cardQueryModel=generateModel(mapping, form, request, response);
		request.setAttribute("record", cardQueryModel);// 指定要显示的记录。
        PageService pageService = (PageService)getService("pageService");
        pageService.writePage("lss/cardquery", "cardQueryDetail", request, response, false);
       	return null;
    }
/**
 * 
 * @param mapping
 * @param form
 * @param request
 * @param response
 * @return
 * @throws SystemUnregistException
 */
    private CardQueryModel generateModel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SystemUnregistException{
        long id=RequestUtils.getParameterLongValue(request,"id");
        DatabaseService databaseService=(DatabaseService) getService("databaseService");
        CardQuery cardQuery=(CardQuery) databaseService.findRecordById(CardQuery.class.getName(), id);
        if(cardQuery==null){
        	return null;
        }
    	
        CardQueryConfig cardQueryConfig=null;
//        找出对应的制卡类型备注
        List cardQueryConfigList=databaseService.findRecordsByHql("from com.yuanluesoft.lss.cardquery.pojo.CardQueryConfig CardQueryConfig" +
        		" where CardQueryConfig.cardType='"+cardQuery.getCardType()+"'");
        if(cardQueryConfigList!=null&&!cardQueryConfigList.isEmpty()){
        	cardQueryConfig=(CardQueryConfig) cardQueryConfigList.get(0);
        }
        return copyFields(cardQuery,cardQueryConfig);
    }
    /**
     * 拷贝属性
     * @param cardQueryModel
     * @param cardQuery
     */
    private CardQueryModel copyFields(CardQuery cardQuery,CardQueryConfig cardQueryConfig) {
    	CardQueryModel cardQueryModel=new CardQueryModel();
//    	拷贝制卡信息
    	if(cardQuery!=null){
    		cardQueryModel.setBatchNumber(cardQuery.getBatchNumber());
    		cardQueryModel.setCardType(cardQuery.getCardType());
    		cardQueryModel.setJurisdiction(cardQuery.getJurisdiction());
    		cardQueryModel.setMakeCardDate(cardQuery.getMakeCardDate());
    		cardQueryModel.setName(cardQuery.getName());
    		cardQueryModel.setMark(cardQuery.getMark());
    		cardQueryModel.setReceiveDate(cardQuery.getReceiveDate());
    		cardQueryModel.setSecurityNumber(cardQuery.getSecurityNumber());
    		cardQueryModel.setRemovedCardDate(cardQuery.getRemovedCardDate());
    		if(cardQuery.getSex()==0){
    		   cardQueryModel.setSexFront("男");
    		}else cardQueryModel.setSexFront("女");
    	}
//    	拷贝制卡备注信息
    	if(cardQueryConfig!=null){
    		cardQueryModel.setReceiveMark(cardQueryConfig.getReceiveMark());
    		cardQueryModel.setRemovedCardMark(cardQueryConfig.getRemovedCardMark());
    		cardQueryModel.setMakeCardMark(cardQueryConfig.getMakeCardMark());
    	}
    	return cardQueryModel;
    	
	}
}
