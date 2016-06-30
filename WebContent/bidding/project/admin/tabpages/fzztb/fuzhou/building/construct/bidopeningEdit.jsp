<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<b>项目开标情况</b>
<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="bidopening.projectName"/></td>
		<td class="tdtitle" nowrap="nowrap">开标室</td>
		<td class="tdcontent"><ext:field property="bidopening.room"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">开标时间</td>
		<td class="tdcontent"><ext:field property="bidopening.beginTime"/></td>
		<td class="tdtitle" nowrap="nowrap">截标时间</td>
		<td class="tdcontent"><ext:field property="bidopening.endTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top" nowrap="nowrap">开标情况描述</td>
		<td class="tdcontent" colspan="3"><ext:field property="bidopening.description"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">记录人</td>
		<td class="tdcontent"><ext:field property="bidopening.recorder"/></td>
		<td class="tdtitle" nowrap="nowrap">监标人</td>
		<td class="tdcontent"><ext:field property="bidopening.surverllant"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标人代表</td>
		<td class="tdcontent"><ext:field property="bidopening.linkman"/></td>
		<td class="tdtitle" nowrap="nowrap">公示期限(天)</td>
		<td class="tdcontent"><ext:field property="bidopening.publicLimit"/></td>
	</tr>
</table>
<br>
<b>开标明细</b>
<table valign="middle" width="100%" style="table-layout:fixed" border="1" cellpadding="0" cellspacing="0" class="table">
	<col width="36px">
	<col width="100%">
	<col width="60px">
	<col width="100px">
	<col width="100px">
	<col width="60px">
	<col width="60px">
	<col width="70px">
	<col width="70px">
	<col width="36px">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap">序号</td>
		<td class="tdtitle" nowrap="nowrap">投标人</td>
		<td class="tdtitle" nowrap="nowrap">密封情况</td>
		<td class="tdtitle" nowrap="nowrap">投标报价</td>
		<td class="tdtitle" nowrap="nowrap">执业证书编号</td>
		<td class="tdtitle" nowrap="nowrap">工期</td>
		<td class="tdtitle" nowrap="nowrap">保证金</td>
		<td class="tdtitle" nowrap="nowrap">质量目标</td>
		<td class="tdtitle" nowrap="nowrap">项目经理</td>
		<td class="tdtitle" nowrap="nowrap">签名</td>
	</tr>
	<ext:iterate id="grade" property="grades">
		<tr align="center">
			<td class="tdcontent"><ext:write name="grade" property="serial"/></td>
			<td class="tdcontent"><ext:write name="grade" property="enterpriseName"/></td>
			<td class="tdcontent"><ext:write name="grade" property="seal"/></td>
			<td class="tdcontent"><ext:write name="grade" property="price" format="#"/></td>
			<td class="tdcontent"><ext:write name="grade" property="certificate"/></td>
			<td class="tdcontent"><ext:write name="grade" property="timeLimit"/></td>
			<td class="tdcontent"><ext:write name="grade" property="pledge"/></td>
			<td class="tdcontent"><ext:write name="grade" property="quality"/></td>
			<td class="tdcontent"><ext:write name="grade" property="manager"/></td>
			<td class="tdcontent"><ext:write name="grade" property="sign"/></td>
		</tr>
	</ext:iterate>
</table>