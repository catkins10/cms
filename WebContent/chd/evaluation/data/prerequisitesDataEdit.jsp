<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<div style="padding-bottom: 3px">
	年度：<ext:field property="declareYear" style="width:100px"/>
</div>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom" align="center">
		<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="40%">必备条件</td>
		<td class="tdtitle" nowrap="nowrap" width="40%">完成情况</td>
		<td class="tdtitle" nowrap="nowrap" width="20%">备注</td>
	</tr>
	<ext:iterate id="prerequisitesData" indexId="prerequisitesDataIndex" property="prerequisitesDataList">
		<tr>
			<td class="tdcontent" align="center"><ext:writeNumber name="prerequisitesDataIndex" plus="1"/></td>
			<td class="tdcontent"><ext:write name="prerequisitesData" property="prerequisites"/></td>
			<td class="tdcontent">
				<input type="text" class="field" name="result_<ext:write name="prerequisitesData" property="prerequisitesId"/>" value="<ext:write name="prerequisitesData" property="result"/>"/>
			</td>
			<td class="tdcontent">
				<input type="text" class="field" name="remark_<ext:write name="prerequisitesData" property="prerequisitesId"/>" value="<ext:write name="prerequisitesData" property="remark"/>"/>
			</td>
		</tr>
	</ext:iterate>
</table>