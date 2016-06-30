<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col align="left">
	<col width="33%">
	<col align="left">
	<col width="33%">
	<col align="left">
	<col width="33%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">证书编号</td>
		<td class="tdcontent"><ext:field property="number"/></td>
		<td class="tdtitle" nowrap="nowrap">准考证号</td>
		<td class="tdcontent"><ext:field property="testAdmissionTicketNumber"/></td>
		<td class="tdtitle" nowrap="nowrap">身份证号</td>
		<td class="tdcontent"><ext:field property="personId"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdcontent"><ext:field property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">性别</td>
		<td class="tdcontent"><ext:field property="sex"/></td>
		<td class="tdtitle" nowrap="nowrap">出生日期</td>
		<td class="tdcontent"><ext:field property="birthday"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">文化程度</td>
		<td class="tdcontent"><ext:field property="educationLevel"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="phone"/></td>
		<td class="tdtitle" nowrap="nowrap">鉴定职业</td>
		<td class="tdcontent"><ext:field property="profession"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">鉴定级别</td>
		<td class="tdcontent"><ext:field property="level"/></td>
		<td class="tdtitle" nowrap="nowrap">鉴定科目</td>
		<td class="tdcontent"><ext:field property="subject"/></td>
		<td class="tdtitle" nowrap="nowrap">鉴定分类</td>
		<td class="tdcontent"><ext:field property="type"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">工作单位</td>
		<td class="tdcontent"><ext:field property="company"/></td>
		<td class="tdtitle" nowrap="nowrap">工作年限</td>
		<td class="tdcontent"><ext:field property="serviceYears"/></td>
		<td class="tdtitle" nowrap="nowrap">评定成绩</td>
		<td class="tdcontent"><ext:field property="mark"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">上报机构</td>
		<td class="tdcontent"><ext:field property="authority"/></td>
		<td class="tdtitle" nowrap="nowrap">理论成绩</td>
		<td class="tdcontent"><ext:field property="theoryMark"/></td>
		<td class="tdtitle" nowrap="nowrap">理论情况</td>
		<td class="tdcontent"><ext:field property="theorySituation"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">实操成绩</td>
		<td class="tdcontent"><ext:field property="practiceMark"/></td>
		<td class="tdtitle" nowrap="nowrap">实操情况</td>
		<td class="tdcontent"><ext:field property="practiceSituation"/></td>
		<td class="tdtitle" nowrap="nowrap">外语成绩</td>
		<td class="tdcontent"><ext:field property="foreignMark"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">外语情况</td>
		<td class="tdcontent"><ext:field property="foreignSituation"/></td>
		<td class="tdtitle" nowrap="nowrap">综合成绩</td>
		<td class="tdcontent"><ext:field property="generalMark"/></td>
		<td class="tdtitle" nowrap="nowrap">综合情况</td>
		<td class="tdcontent"><ext:field property="generalSituation"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">成绩认定日期</td>
		<td class="tdcontent"><ext:field property="markDecidedDate"/></td>
		<td class="tdtitle" nowrap="nowrap">发证日期</td>
		<td class="tdcontent" colspan="2"><ext:field property="sendDate"/></td>
		
	</tr>
</table>
