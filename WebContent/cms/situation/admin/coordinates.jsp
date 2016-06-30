<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" class="tdtitle" nowrap="nowrap" width="36px">序号</td>
		<td align="center" class="tdtitle" nowrap="nowrap" width="160px">单位</td>
		<td align="center" class="tdtitle" nowrap="nowrap" width="160px">协调单位</td>
		<td align="center" class="tdtitle" nowrap="nowrap" width="100%">原因</td>
		<td align="center" class="tdtitle" nowrap="nowrap" width="110px">协调时间</td>
	</tr>
	<ext:iterate id="coordinate" indexId="coordinateIndex" property="coordinates">
		<tr valign="top">
			<td class="tdcontent" align="center"><ext:writeNumber name="coordinateIndex" plus="1"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="coordinate" property="unitName"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="coordinate" property="coordinateUnitName"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="coordinate" property="coordinateReason"/></td>
			<td class="tdcontent" align="center"><ext:field writeonly="true" name="coordinate" property="coordinateTime"/></td>
		</tr>
	</ext:iterate>
</table>