<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="100px">申报年度</td>
		<td class="tdtitle" nowrap="nowrap" width="180px">申报时间</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">是否列入重点项目</td>
		<td class="tdtitle" nowrap="nowrap" width="180px">审批日期</td>
	</tr>
	<ext:iterate id="declare" property="declares">
		<tr valign="top" align="center">
			<td class="tdcontent"><ext:write name="declare" property="declareYear"/></td>
			<td class="tdcontent"><ext:write name="declare" property="declareTime" format="yyyy-MM-dd HH:mm"/></td>
			<td class="tdcontent">
				<ext:notEmpty name="declare" property="approvalTime">
					<ext:equal value="1" name="declare" property="isKeyProject">已列入</ext:equal>
					<ext:notEqual value="1" name="declare" property="isKeyProject">未列入</ext:notEqual>
				</ext:notEmpty>
			</td>
			<td class="tdcontent" align="center"><ext:write name="declare" property="approvalTime" format="yyyy-MM-dd HH:mm"/></td>
		</tr>
	</ext:iterate>
</table>