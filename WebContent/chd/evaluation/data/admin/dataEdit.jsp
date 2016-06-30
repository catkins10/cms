<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function onDataTypeChanged() {
		var dataType = document.getElementsByName("dataType");
		document.getElementById("trFile").style.display = (dataType[0].checked ? "" : "none");
		document.getElementById("trLink").style.display = (dataType[1].checked ? "" : "none");
		document.getElementById("trPlace").style.display = (dataType[2].checked ? "" : "none");
	}
	function formOnSubmit() {
		var dataType = document.getElementsByName("dataType");
		if(dataType[0].checked && FormUtils.getAttachmentCount('attachments')==0) {
			alert("尚未上传文件");
			return false;
		}
		return true;
	}
</script>
<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">评价细则</td>
		<td class="tdcontent"><ext:field property="directoryName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评价要点</td>
		<td class="tdcontent"><ext:field property="point"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">资料名称</td>
		<td class="tdcontent"><ext:field property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">资料类型</td>
		<td class="tdcontent"><ext:field property="dataType" onclick="onDataTypeChanged()"/></td>
	</tr>
	<tr id="trFile" style="<ext:notEqual value="0" property="dataType">display:none</ext:notEqual>">
		<td class="tdtitle" nowrap="nowrap">文件上传</td>
		<td class="tdcontent"><ext:field property="attachments"/></td>
	</tr>
	<tr id="trLink" style="<ext:notEqual value="1" property="dataType">display:none</ext:notEqual>">
		<td class="tdtitle" nowrap="nowrap">链接地址</td>
		<td class="tdcontent">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="100%">
						<ext:field property="link"/>
					</td>
					<td class="tdtitle" nowrap="nowrap" style="padding-left: 3px">
						<input type="button" class="button" style="width:66px" value="打开链接" onclick="window.open(document.getElementsByName('link')[0].value)"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr id="trPlace" style="<ext:notEqual value="2" property="dataType">display:none</ext:notEqual>">
		<td class="tdtitle" nowrap="nowrap">存放位置</td>
		<td class="tdcontent"><ext:field property="place"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">提交时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">责任人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field property="remark"/></td>
	</tr>
</table>