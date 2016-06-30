<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%	
	String formName = (String)request.getAttribute("formName");
	if(formName==null) {
		formName = ((org.apache.struts.action.ActionMapping)request.getAttribute("org.apache.struts.action.mapping.instance")).getName();
	}
%>
<ext:iterate id="field" name="<%=formName%>" property="fields">
	<tr style="<ext:equal value="hidden" name="field" property="fieldDefine.inputMode">display:none</ext:equal>">
		<td nowrap="nowrap" align="right"><ext:write name="field" property="fieldDefine.title"/>：</td>
		<td width="100%">
			<ext:notEqual value="hidden" name="field" property="fieldDefine.inputMode">
				<ext:field name="<%=formName%>" property="<%=((com.yuanluesoft.jeaf.form.model.DynamicFormField)pageContext.getAttribute("field")).getFieldDefine().getName()%>"/>
			</ext:notEqual>
			<ext:equal value="hidden" name="field" property="fieldDefine.inputMode">
				<input type="hidden" name="<ext:write name="field" property="fieldDefine.name"/>" value="<ext:write name="field" property="value"/>"/>
			</ext:equal>
		</td>
	</tr>
</ext:iterate>