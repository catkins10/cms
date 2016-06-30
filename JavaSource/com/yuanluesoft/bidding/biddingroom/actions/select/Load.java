package com.yuanluesoft.bidding.biddingroom.actions.select;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.biddingroom.forms.Select;
import com.yuanluesoft.bidding.biddingroom.service.BiddingRoomService;
import com.yuanluesoft.jeaf.dialog.actions.SelectDialogAction;
import com.yuanluesoft.jeaf.dialog.forms.SelectDialog;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends SelectDialogAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeOpenDialogAction(mapping, form, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogAction#initDialog(com.yuanluesoft.jeaf.dialog.forms.SelectDialog, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initDialog(SelectDialog dialog, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initDialog(dialog, sessionInfo, request);
		Select selectForm = (Select)dialog;
    	BiddingRoomService biddingRoomService = (BiddingRoomService)getService("biddingRoomService");
    	//获取所有开评标室列表
    	if(!selectForm.isFreeRoomOnly()) {
    		selectForm.setAllRooms(biddingRoomService.listRooms(selectForm.getRoomType(), selectForm.getCity()));
    	}
    	//获取空闲开评标室列表
    	if(selectForm.getBeginTime()!=null && selectForm.getEndTime()!=null) {
    	 	selectForm.setFreeRooms(biddingRoomService.listFreeRooms(selectForm.getRoomType(), selectForm.getCity(), selectForm.getForProjectId(), selectForm.getBeginTime(), selectForm.getEndTime()));
    	}
    	selectForm.setTitle("选择" + selectForm.getRoomType() + "室");
	}
}