<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">纳税人识别号</td>
		<td class="tdcontent" ><ext:field property="number"/></td>
		<td class="tdtitle" nowrap="nowrap">纳税人名称</td>
		<td class="tdcontent" ><ext:field property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">征收项目</td>
		<td class="tdcontent"><ext:field property="project"/></td>
		<td class="tdtitle" nowrap="nowrap">累计欠税(元)</td>
		<td class="tdcontent"><ext:field property="totalTaxes"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>