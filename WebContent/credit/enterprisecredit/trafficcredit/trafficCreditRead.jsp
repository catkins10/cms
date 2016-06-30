<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">当事人姓名</td>
		<td class="tdcontent" ><ext:write property="person"/></td>
		<td class="tdtitle" nowrap="nowrap">身份证号码</td>
		<td class="tdcontent" ><ext:write property="idCard"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">性质</td>
		<td class="tdcontent"><ext:write property="nature"/></td>
		<td class="tdtitle" nowrap="nowrap">填报单位</td>
		<td class="tdcontent"><ext:write property="deparment"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">填报人</td>
		<td class="tdcontent"><ext:write property="writer"/></td>
		<td class="tdtitle" nowrap="nowrap">审核人</td>
		<td class="tdcontent"><ext:write property="auditor"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">交通肇事简要情况</td>
		<td class="tdcontent" colspan="3"><ext:write property="summary"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent" colspan="3"><ext:write property="remark"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>