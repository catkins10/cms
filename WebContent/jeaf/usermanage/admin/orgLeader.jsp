<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

	<tr>
		<td class="tdtitle" nowrap="nowrap"><ext:write property="formDefine.title"/>领导</td>
		<td class="tdcontent" <%=(request.getAttribute("colspan")==null ? "" : "colspan=\"" + request.getAttribute("colspan") + "\"")%>><ext:field property="leaderNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主管领导</td>
		<td class="tdcontent" <%=(request.getAttribute("colspan")==null ? "" : "colspan=\"" + request.getAttribute("colspan") + "\"")%>><ext:field property="supervisorNames"/></td>
	</tr>