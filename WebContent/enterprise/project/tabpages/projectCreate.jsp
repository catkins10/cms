<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="33%">
	<col>
	<col width="33%">
	<col>
	<col width="33%">
	<ext:equal value="create" property="act">
		<tr>
			<td class="tdtitle" nowrap="nowrap">名称</td>
			<td class="tdcontent" colspan="3"><ext:field property="name"/></td>
			<td class="tdtitle" nowrap="nowrap">项目类别</td>
			<td class="tdcontent"><ext:field property="type"/></td>
		</tr>
	</ext:equal>
	<ext:notEqual value="create" property="act">
		<tr>
			<td class="tdtitle" nowrap="nowrap">名称</td>
			<td class="tdcontent" colspan="3"><ext:field property="name"/></td>
			<td class="tdtitle" nowrap="nowrap">项目类别</td>
			<td class="tdcontent"><ext:field writeonly="true" property="type"/></td>
		</tr>
	</ext:notEqual>
	<tr>
		<td class="tdtitle" nowrap="nowrap">工程概况</td>
		<td class="tdcontent" colspan="5"><ext:field property="overview"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">桥梁规模</td>
		<td class="tdcontent"><ext:field property="bridgeScale"/></td>
		<td class="tdtitle" nowrap="nowrap">桥宽</td>
		<td class="tdcontent"><ext:field property="bridgeWidth"/></td>
		<td class="tdtitle" nowrap="nowrap">桥长</td>
		<td class="tdcontent"><ext:field property="bridgeLength"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">隧道规模</td>
		<td class="tdcontent"><ext:field property="tunnelScale"/></td>
		<td class="tdtitle" nowrap="nowrap">隧道宽</td>
		<td class="tdcontent"><ext:field property="tunnelWidth"/></td>
		<td class="tdtitle" nowrap="nowrap">隧道长</td>
		<td class="tdcontent"><ext:field property="tunnelLength"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">公路等级</td>
		<td class="tdcontent"><ext:field property="highwayLevel"/></td>
		<td class="tdtitle" nowrap="nowrap">公路时速</td>
		<td class="tdcontent"><ext:field property="highwaySpeed"/></td>
		<td class="tdtitle" nowrap="nowrap">公路里程</td>
		<td class="tdcontent"><ext:field property="highwayMileage"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目规模</td>
		<td class="tdcontent"><ext:field property="scale"/></td>
		<td class="tdtitle" nowrap="nowrap">项目建成时间</td>
		<td class="tdcontent"><ext:field property="completionDate"/></td>
		<td class="tdtitle" nowrap="nowrap">工作时间</td>
		<td class="tdcontent">
			<table valign="middle" width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="50%"><ext:field property="beginDate"/></td>
					<td nowrap="nowrap">&nbsp;至&nbsp;</td>
					<td width="50%"><ext:field property="endDate"/></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">业主单位</td>
		<td class="tdcontent"><ext:field property="owner"/></td>
		<td class="tdtitle" nowrap="nowrap">监理单位</td>
		<td class="tdcontent"><ext:field property="surveillanceUnit"/></td>
		<td class="tdtitle" nowrap="nowrap">文件编号</td>
		<td class="tdcontent"><ext:field property="sn"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目负责人</td>
		<td class="tdcontent"><ext:field property="projectLeader"/></td>
		<td class="tdtitle" nowrap="nowrap">是否招标项目</td>
		<td class="tdcontent"><ext:field property="isBidding"/></td>
		<td class="tdtitle" nowrap="nowrap">招标编号</td>
		<td class="tdcontent"><ext:field property="biddingNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所属地区</td>
		<td class="tdcontent"><ext:field property="city"/></td>
		<td class="tdtitle" nowrap="nowrap">ISO贯标</td>
		<td class="tdcontent"><ext:field property="iso"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td class="tdcontent" colspan="5"><ext:field property="attachments"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent" colspan="5"><ext:field property="remark"/></td>
	</tr>
</table>