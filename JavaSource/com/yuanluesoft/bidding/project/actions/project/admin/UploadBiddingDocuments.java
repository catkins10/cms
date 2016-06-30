package com.yuanluesoft.bidding.project.actions.project.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 上传投标文件,在前端页面实现
 * @author yuanlue
 *
 */
public class UploadBiddingDocuments extends ProjectAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        throw new Exception("Can not run at background.");
    }
}