<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function selectImage() {
		DialogUtils.selectAttachment('/cms/interview/admin/selectInterviewImage.shtml?interviewImage.id=<ext:field writeonly="true" property="interviewImage.id"/>', '<ext:write property="id"/>', 'images', 600, 400, 'setImageURL("{URL}")');
	}
	function setImageURL(url) {
		document.getElementsByName("interviewImage.imageUrl")[0].value = url;
		if(document.getElementsByName("interviewImage.subject")[0].value=="") {
			document.getElementsByName("interviewImage.subject")[0].value = StringUtils.utf8Decode(url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.')));
		}
	}
</script>
<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<tr>
		<td width="50px" align="right">图片：</td>
		<td><ext:field property="interviewImage.imageUrl"/></td>
	</tr>
	<tr>
		<td align="right">标题：</td>
		<td><ext:field property="interviewImage.subject"/></td>
	</tr>
</table>