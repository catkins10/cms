<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function formOnSubmit() {
		if(document.getElementById('trCoordinateUnit').style.display!='none') {
			if(document.getElementsByName('coordinateUnitName')[0].value=='') {
				alert('协调的部门不能为空');
				return false;
			}
		}
		else if(document.getElementById('trTransactOpinion')) {
			if(document.getElementsByName('transactOpinion')[0].value=='') {
				alert('办理意见不能为空');
				return false;
			}
		}
		return true;
	}
</script>
<table width="430px" border="0" cellpadding="3" cellspacing="0">
	<ext:equal value="true" property="village">
		<tr>
			<td nowrap="nowrap" align="right">办理意见：</td>
			<td width="100%"><ext:field property="villageTransact" onchange="document.getElementById('trCoordinateUnit').style.display=document.getElementsByName('villageTransact')[0].value=='coordinate' ? '' : 'none';"/></td>
		</tr>
	</ext:equal>
	<ext:equal value="false" property="village">
		<tr>
			<td nowrap="nowrap">办理选项：</td>
			<td width="100%"><ext:field property="coordinateOtherUnit" onclick="var coordinateOtherUnit=document.getElementsByName('coordinateOtherUnit')[0].checked; document.getElementById('trTransactOpinion').style.display=coordinateOtherUnit ? 'none' : ''; document.getElementById('trCoordinateUnit').style.display=!coordinateOtherUnit ? 'none' : '';"/></td>
		</tr>
		<tr id="trTransactOpinion">
			<td nowrap="nowrap" align="right" valign="top">办理意见：</td>
			<td><ext:field property="transactOpinion"/></td>
		</tr>
	</ext:equal>
	<tr id="trCoordinateUnit" style="display:none">
		<td nowrap="nowrap" align="right">协调的部门：</td>
		<td><ext:field property="coordinateUnitName"/></td>
	</tr>
</table>