<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveUnitConfig">
	<ext:tab>
		<ext:tabBody tabId="basic">
			<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
				<col>
				<col valign="middle" width="100%">
				<tr>
					<td class="tdtitle" nowrap="nowrap">单位名称</td>
					<td class="tdcontent"><ext:field property="unitName"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">IP地址</td>
					<td class="tdcontent"><ext:field property="ip"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">JDBC URL</td>
					<td class="tdcontent"><ext:field property="jdbcUrl"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">数据库用户名</td>
					<td class="tdcontent"><ext:field property="dbUserName"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">数据库密码</td>
					<td class="tdcontent"><ext:field property="dbPassword"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">采集时间点</td>
					<td class="tdcontent"><ext:field property="captureTime"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">采集周期(天)</td>
					<td class="tdcontent"><ext:field property="captureDays"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">创建人</td>
					<td class="tdcontent"><ext:field property="creator"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">创建时间</td>
					<td class="tdcontent"><ext:field property="created"/></td>
				</tr>
			</table>
		</ext:tabBody>
		<ext:tabBody tabId="sqls">
			<script>
				function newMonitorSql() {
					DialogUtils.openDialog('<%=request.getContextPath()%>/cms/monitor/monitorSql.shtml?id=<ext:write property="id"/>', 600, 400);
				}
				function openMonitorSql(monitorSqlId) {
					DialogUtils.openDialog('<%=request.getContextPath()%>/cms/monitor/monitorSql.shtml?id=<ext:write property="id"/>&monitorSql.id=' + monitorSqlId, 600, 400);
				}
			</script>
			<div style="padding-bottom:5px">
				<input type="button" class="button" value="添加SQL" onclick="newMonitorSql()">
			</div>
			<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
				<tr height="23px" valign="bottom" align="center">
					<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
					<td class="tdtitle" nowrap="nowrap" width="120px">采集内容</td>
					<td class="tdtitle" nowrap="nowrap" width="100%">SQL</td>
					<td class="tdtitle" nowrap="nowrap" width="110">最后采集时间</td>
				</tr>
				<ext:iterate id="monitorSql" indexId="monitorSqlIndex" property="sqls">
					<tr style="cursor:pointer" valign="top" onclick="openMonitorSql('<ext:write name="monitorSql" property="id"/>')">
						<td class="tdcontent" align="center"><ext:writeNumber name="monitorSqlIndex" plus="1"/></td>
						<td class="tdcontent"><ext:write name="monitorSql" property="captureContent"/></td>
						<td class="tdcontent"><ext:write name="monitorSql" property="captureSql"/></td>
						<td class="tdcontent" align="center"><ext:field writeonly="true" name="monitorSql" property="lastCaptureTime"/></td>
					</tr>
				</ext:iterate>
			</table>
		</ext:tabBody>
	</ext:tab>
</ext:form>