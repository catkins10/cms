package com.yuanluesoft.cms.templatemanage.actions.insertrecordlist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.templatemanage.forms.InsertRecordList;
import com.yuanluesoft.jeaf.business.util.FieldUtils;


/**
 * 重定向到新记录列表,如:网站栏目引用网上办事目录
 * @author linchuan
 *
 */
public class RedirectToNewView extends Load {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.htmleditor.actions.editordialog.Load#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.execute(mapping, form, request, response);
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/javascript");
		InsertRecordList insertForm = (InsertRecordList)form;
		if(insertForm.getView()!=null) {
			//获取内嵌视图列表选项文本
			String itemsText = FieldUtils.getSelectItemsText(insertForm.getFormDefine().getField("embedViews"), insertForm, request);
			response.getWriter().write("document.getElementById('tdInsertEmbedViewButton').style.display = " + (itemsText==null || itemsText.isEmpty() ? "'none'" : "''") + ";");
			response.getWriter().write("DropdownField.setListValues('embedViews', \"" + (itemsText==null ? "" : itemsText) + "\");");
			//获取记录链接选项文本
			itemsText = FieldUtils.getSelectItemsText(insertForm.getFormDefine().getField("recordLink"), insertForm, request);
			response.getWriter().write("document.getElementsByName('recordLink')[0].value='';");
			response.getWriter().write("DropdownField.setListValues('recordLink', \"" + (itemsText==null ? "" : itemsText) + "\");");
		}
		return null;
	}
}