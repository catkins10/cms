<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/inputDialog">
	<script>
		function doOk() {
			var values = "{";
			<ext:iterate property="inputs" id="input" indexId="inputIndex">
				<ext:notEqual value="0" name="inputIndex">values += ",";</ext:notEqual>
				values += "<ext:write name="input" property="name"/>:";
				var fields = document.getElementsByName("<ext:write name="input" property="name"/>");
				var value = "";
				if(fields[0].type!="radio" && fields[0].type!="checkbox") {
					value = fields[0].value;
				}
				else {
					for(var i=0; i<fields.length; i++) {
						if(fields[i].checked) {
							value += (value=="" ? "" : ",") + fields[i].value;
						}
					}
				}
				<ext:equal value="true" name="input" property="required">
					if(value=="") {
						alert("<ext:write name="input" property="title"/>不能为空");
						return;
					}
				</ext:equal>
				values += "'" + value.replace(/'/g, "\\'") + "'";
			</ext:iterate>
			values += "}";
			var script = document.getElementsByName("script")[0].value;
			if(script!="") {
				DialogUtils.getDialogOpener().setTimeout(script.replace(/{value}/g, values).replace(/{values}/g, values), 300);
			}
			else {
				eval("values=" + values + ";");
				DialogUtils.getDialogArguments().call(null, values);
			}
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<ext:iterate property="inputs" id="input">
			<ext:notEqual value="hidden" name="input" property="inputMode">
				<tr>
					<td nowrap="nowrap" valign="top" align="right" style="padding-top:7px;"><ext:write name="input" property="title"/>：</td>
					<td width="100%"><ext:field property="<%=((com.yuanluesoft.jeaf.business.model.Field)pageContext.getAttribute("input")).getName()%>"/></td>
				</tr>
			</ext:notEqual>
		</ext:iterate>
	</table>
</ext:form>