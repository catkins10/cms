<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/report/writeEnsureReport">
	<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td nowrap="nowrap">时间:</td>
			<td width="86px" nowrap="nowrap"><ext:field property="beginDate"/></td>
			<td nowrap="nowrap" style="padding:0 3 0 3">至</td>
			<td width="86px" nowrap="nowrap"><ext:field property="endDate"/></td>
			<td nowrap="nowrap">&nbsp;站点:</td>
			<td width="180px" nowrap="nowrap"><ext:field property="siteName"/></td>
			<td nowrap="nowrap">
				&nbsp;<input type="button" class="button" value="确定" onclick="FormUtils.submitForm()"/>
				<input type="button" class="button" value="单位配置" onclick="DialogUtils.openDialog('<%=request.getContextPath()%>/cms/siteresource/report/listEnsureUnitCategories.shtml?siteId=' + document.getElementsByName('siteId')[0].value, 600, 360)"/>
				<input type="button" class="button" value="栏目配置" onclick="DialogUtils.openDialog('<%=request.getContextPath()%>/cms/siteresource/report/listEnsureColumnConfigs.shtml?siteId=' + document.getElementsByName('siteId')[0].value, 600, 360)"/>
			</td>
		</tr>
	</table>
	<br/>
	<ext:notEmpty property="unitCategories">
		<div style="font-family:黑体; font-size:18px; text-align:center; padding-bottom:8px">
			<u><ext:write property="beginDate" format="yyyy"/></u>年<u><ext:write property="siteName"/></u>网站各部门信息保障统计表（<u><ext:write property="quarter"/></u>季度）										
		</div>
		<table align="center" style="border-collapse: collapse;" border="1" bordercolor="#000000" cellpadding="5" cellspacing="0" style="background-color: #ffffff">
			<tr align="center">
				<td><b></b></td>
				<td><b>序号</b></td>
				<td><b>单位</b></td>
				<ext:iterate id="month" property="months">
					<td><b><ext:write name="month"/>月份信息量</b></td>
					<td><b><ext:write name="month"/>月份100分值得分</b></td>
				</ext:iterate>
				<td><b>本季度信息量</b></td>
				<td><b>本季度得分</b></td>
			</tr>
			<ext:iterate id="unitCategory" indexId="unitCategoryIndex" property="unitCategories">
				<tr align="center">
					<td style="writing-mode:tb-rl;text-align:center;" rowspan="<ext:writeNumber name="unitCategory" property="unitCount"/>">
						<ext:write name="unitCategory" property="category"/>
					</td>
					<ext:iterate id="unitStat" indexId="unitStatIndex" length="1" name="unitCategory" property="unitStats">
						<td><ext:writeNumber name="unitStatIndex" plus="1"/></td>
						<td><ext:write name="unitStat" property="unitName"/></td>
						<ext:iterate id="monthStat" name="unitStat" property="monthStats">
							<td><ext:write name="monthStat" property="issueStat"/></td>
							<td><ext:write name="monthStat" property="score"/></td>
						</ext:iterate>
						<td><ext:writeNumber name="unitStat" property="issueStat"/></td>
						<td><ext:writeNumber name="unitStat" property="score"/></td>
					</ext:iterate>
				</tr>
				<ext:iterate id="unitStat" indexId="unitStatIndex" offset="1" name="unitCategory" property="unitStats">
					<tr align="center">
						<td><ext:writeNumber name="unitStatIndex" plus="1"/></td>
						<td><ext:write name="unitStat" property="unitName"/></td>
						<ext:iterate id="monthStat" name="unitStat" property="monthStats">
							<td><ext:write name="monthStat" property="issueStat"/></td>
							<td><ext:write name="monthStat" property="score"/></td>
						</ext:iterate>
						<td><ext:writeNumber name="unitStat" property="issueStat"/></td>
						<td><ext:writeNumber name="unitStat" property="score"/></td>
					</tr>
				</ext:iterate>
			</ext:iterate>
		</table>
	</ext:notEmpty>
</ext:form>