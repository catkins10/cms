<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function selectAllFile(checked) {
		var checkboxes = document.getElementsByName("prophase.submittedFiles");
		for(var i=0; i<checkboxes.length; i++) {
			checkboxes[i].checked = checked;
		}
	}
</script>
<input type="button" class="button" style="width:60px" onclick="selectAllFile(true)" value="全部选中">
<input type="button" class="button" style="width:60px" onclick="selectAllFile(false)" value="全部取消">
<div style="font-size:5px">&nbsp;</div>
<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col>
	<ext:iterate id="prophaseFile" indexId="prophaseFileIndex" property="prophaseFiles">
		<tr>
			<td class="tdtitle" nowrap="nowrap"><ext:write name="prophaseFile" property="name"/></td>
			<td class="tdcontent" width="100%">
				&nbsp;<html:multibox property="prophase.submittedFiles" styleClass="checkbox"><ext:write name="prophaseFile" property="name"/></html:multibox>&nbsp;已提交
			</td>
		</tr>
	</ext:iterate>
</table>