<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">被处罚人（单位）</td>
		<td class="tdcontent" ><ext:field property="person"/></td>
		<td class="tdtitle" nowrap="nowrap">身份证号或其他证件名称</td>
		<td class="tdcontent" ><ext:field property="cardNum"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">被处罚人地址</td>
		<td class="tdcontent"><ext:field property="personAddr"/></td>
		<td class="tdtitle" nowrap="nowrap">处罚决定书号</td>
		<td class="tdcontent"><ext:field property="bookNum"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">违法地点</td>
		<td class="tdcontent"><ext:field property="addr"/></td>
		<td class="tdtitle" nowrap="nowrap">违法情况</td>
		<td class="tdcontent"><ext:field property="summary"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">处罚情况</td>
		<td class="tdcontent" colspan="3"><ext:field property="result"/></td>
		
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">其他</td>
		<td class="tdcontent" colspan="3"><ext:field property="summary"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent" colspan="3"><ext:field property="remark"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>