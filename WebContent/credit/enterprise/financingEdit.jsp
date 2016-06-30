<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">融资项目名称</td>
		<td class="tdcontent" ><ext:field property="projectName"/></td>
		<td class="tdtitle" nowrap="nowrap">资金需求额度</td>
		<td class="tdcontent" ><ext:field property="amount"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">可提供担保方式</td>
		<td class="tdcontent"><ext:field property="guarantyType"/></td>
		<td class="tdtitle" nowrap="nowrap">资金需求种类</td>
		<td class="tdcontent"><ext:field property="type"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">资金用途</td>
		<td class="tdcontent" colspan="3"><ext:field property="purpose"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">简介</td>
		<td class="tdcontent" colspan="3"><ext:field property="introduction"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>