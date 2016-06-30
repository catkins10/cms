<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newBorrow() {
		DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/book/borrow.shtml?id=<ext:write property="id"/>', 500, 290);
	}
	function openBorrow(borrowId) {
		DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/book/borrow.shtml?id=<ext:write property="id"/>&borrow.id=' + borrowId, 500, 290);
	}
</script>
<ext:notEqual value="1" property="isBorrowing">
	<%if(!"true".equals(request.getAttribute("readonly"))) { %>
		<div style="padding-bottom:5px">
			<input type="button" class="button" value="借阅" style="width:80px" onclick="newBorrow()">
		</div>
	<%}%>
</ext:notEqual>	
<table id="tableBill" width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td width="100px" nowrap="nowrap" class="tdtitle">借阅人</td>
		<td width="120px" nowrap="nowrap" class="tdtitle">借阅时间</td>
		<td width="160px" nowrap="nowrap" class="tdtitle">拟归还时间/归还时间</td>
		<td width="50px" nowrap="nowrap" class="tdtitle">已归还</td>
		<td nowrap="nowrap" class="tdtitle">备注</td>
	</tr>
	<ext:iterate id="borrow" indexId="borrowIndex" property="borrows">
		<tr onclick="openBorrow('<ext:write name="borrow" property="id"/>')" align="center">
			<td class="tdcontent" align="left"><ext:write name="borrow" property="personName"/></td>
			<td class="tdcontent"><ext:write name="borrow" property="borrowTime" format="yyyy-MM-dd HH:mm"/></td>
			<td class="tdcontent"><ext:write name="borrow" property="returnTime" format="yyyy-MM-dd HH:mm"/></td>
			<td class="tdcontent"><ext:equal value="1" name="borrow" property="isReturned">√</ext:equal></td>
			<td class="tdcontent" align="left"><ext:write name="borrow" property="remark"/></td>
		</tr>
	</ext:iterate>
</table>