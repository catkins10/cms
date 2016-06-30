<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<div style="padding-bottom: 3px">
	<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td>年度：</tr>
			<td><ext:field property="dataYear" style="width:100px"/></td>
			<td>月份：</td>
			<td><ext:field property="dataMonth" style="width:100px"/></td>
		</tr>
	</table>
</div>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom" align="center">
		<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="40%">指标</td>
		<td class="tdtitle" nowrap="nowrap" width="40%">完成情况</td>
		<td class="tdtitle" nowrap="nowrap" width="20%">备注</td>
	</tr>
	<ext:iterate id="indicatorData" indexId="indicatorDataIndex" property="indicatorDataList">
		<tr>
			<td class="tdcontent" align="center"><ext:writeNumber name="indicatorDataIndex" plus="1"/></td>
			<td class="tdcontent"><ext:write name="indicatorData" property="indicator"/></td>
			<td class="tdcontent">
				<input type="text" class="field" name="data_<ext:write name="indicatorData" property="indicatorId"/>" value="<ext:write name="indicatorData" property="data"/>"/>
			</td>
			<td class="tdcontent">
				<input type="text" class="field" name="remark_<ext:write name="indicatorData" property="indicatorId"/>" value="<ext:write name="indicatorData" property="remark"/>"/>
			</td>
		</tr>
	</ext:iterate>
</table>