<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newCharge() {
		DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/reimburse/charge.shtml?id=<ext:write property="id"/>', 550, 360);
	}
	function openCharge(chargeId) {
		DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/reimburse/charge.shtml?id=<ext:write property="id"/>&charge.id=' + chargeId, 550, 360);
	}
</script>
<%if(!"true".equals(request.getAttribute("chargeReadonly"))) { %>
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加记录" style="width:80px" onclick="newCharge()">
	</div>
<%}%>
<table id="tableBill" width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td nowrap="nowrap" class="tdtitle" width="20px"></td>
		<td nowrap="nowrap" class="tdtitle" width="20px"></td>
		<td nowrap="nowrap" class="tdtitle" width="100px">费用类别</td>
		<td nowrap="nowrap" class="tdtitle" width="70px">费用标准</td>
		<td nowrap="nowrap" class="tdtitle" width="80px">金额</td>
		<td nowrap="nowrap" class="tdtitle" width="110px">发生时间</td>
		<td nowrap="nowrap" class="tdtitle" width="60%">用途</td>
		<td nowrap="nowrap" class="tdtitle" width="40%">备注</td>
	</tr>
	<ext:iterate id="charge" indexId="chargeIndex" property="charges">
		<tr>
			<td class="tdcontent" align="center" onclick="openCharge('<ext:write name="charge" property="id"/>')"><ext:writeNumber plus="1" name="chargeIndex"/></td>
			<td class="tdcontent" align="center">
				<ext:iterateAttachment id="bill" applicationName="j2oa/reimburse" nameRecordId="charge" propertyRecordId="id" attachmentType="bills" length="1">
					<img src="img/expand.gif" onclick="showBill(this)" title="查看票据">
				</ext:iterateAttachment>
			</td>
			<td class="tdcontent" onclick="openCharge('<ext:write name="charge" property="id"/>')"><ext:field writeonly="true" name="charge" property="chargeCategory"/></td>
			<td class="tdcontent" align="center" onclick="openCharge('<ext:write name="charge" property="id"/>')"><ext:write name="charge" property="chargeStandard"/></td>
			<td class="tdcontent" align="center" onclick="openCharge('<ext:write name="charge" property="id"/>')"><ext:write name="charge" property="money"/></td>
			<td class="tdcontent" align="center" onclick="openCharge('<ext:write name="charge" property="id"/>')"><ext:write name="charge" property="time" format="yyyy-MM-dd HH:mm"/></td>
			<td class="tdcontent" onclick="openCharge('<ext:write name="charge" property="id"/>')"><ext:write name="charge" property="purpose"/></td>
			<td class="tdcontent" onclick="openCharge('<ext:write name="charge" property="id"/>')"><ext:write name="charge" property="remark"/></td>
		</tr>
		<ext:iterateAttachment id="bill" applicationName="j2oa/reimburse" nameRecordId="charge" propertyRecordId="id" attachmentType="bills" length="1">
			<tr style="display:none">
				<td class="tdcontent" colspan="8">
					<ext:iterateImage id="billImage" applicationName="j2oa/reimburse" nameRecordId="charge" propertyRecordId="id" imageType="bills">
						<ext:img nameImageModel="billImage" autoWidth="true" width="700"/><br>
					</ext:iterateImage>
				</td>
			</tr>
		</ext:iterateAttachment>
	</ext:iterate>
</table>
<div align="right" style="padding-top:8px">合计：<font style="text-decoration:underline">&nbsp;<ext:write property="money"/>&nbsp;</font>元&nbsp;&nbsp;&nbsp;大写：<ext:write filter="false" property="moneyCapital"/>&nbsp;&nbsp;&nbsp;</div>
<script>
function  showBill(img) {
	var show = img.src.indexOf("expand")!=-1;
	document.getElementById("tableBill").rows[img.offsetParent.parentNode.rowIndex + 1].style.display = (show ? "" : "none");
	img.src = (show ? "img/collapse.gif" : "img/expand.gif");
}
</script>