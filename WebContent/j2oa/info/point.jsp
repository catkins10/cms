<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/savePoint">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">得分项目：</td>
			<td><ext:field property="item" onchange="onItemChange()"/></td>
		</tr>
		<tr style="display:none" id="trMagazines">
			<td nowrap="nowrap">刊物：</td>
			<td><ext:field property="magazineNames"/></td>
		</tr>
		<tr style="display:none" id="trHigherMagazines">
			<td nowrap="nowrap">刊物：</td>
			<td><ext:field property="higherMagazineNames"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">积分：</td>
			<td><ext:field property="point"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">稿酬：</td>
			<td><ext:field property="remuneration"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">简讯积分：</td>
			<td><ext:field property="briefPoint"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">简讯稿酬：</td>
			<td><ext:field property="briefRemuneration"/></td>
		</tr>
	</table>
	<script>
		function onItemChange() {
			var item = document.getElementsByName("item")[0].value;
			document.getElementById("trMagazines").style.display = item=="0" ? "" : "none";
			document.getElementById("trHigherMagazines").style.display = ",3,6,9,12,".indexOf("," + item + ",")!=-1 ? "" : "none";
			DialogUtils.adjustDialogSize();
		}
		onItemChange();
	</script>
</ext:form>