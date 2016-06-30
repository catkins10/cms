package com.yuanluesoft.j2oa.todo.actions.todo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.todo.forms.Todo;

/**
 * 
 * @author LinChuan
*
 */
public class Load extends TodoAction {
     
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Todo formTodo = (Todo)form;
    	if(!OPEN_MODE_CREATE.equals(formTodo.getAct())) {
    		com.yuanluesoft.j2oa.todo.pojo.Todo pojoTodo = (com.yuanluesoft.j2oa.todo.pojo.Todo)getBusinessService(null).load(com.yuanluesoft.j2oa.todo.pojo.Todo.class, formTodo.getId());
    		if(pojoTodo.getIsHand()=='1') {
    			response.sendRedirect("hand.shtml?act=edit&id=" + formTodo.getId());
    			return null;
    		}
    	}
        return executeLoadAction(mapping, form, request, response);
    }
}