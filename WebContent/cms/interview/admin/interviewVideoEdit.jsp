<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function selectVideo() {
		DialogUtils.selectAttachment('/cms/interview/admin/selectInterviewVideo.shtml?interviewVideo.id=<ext:field writeonly="true" property="interviewVideo.id"/>', '<ext:write property="id"/>', 'videos', 600, 400, 'setVideoURL("{URL}")');
	}
	function setVideoURL(url) {
		document.getElementsByName("interviewVideo.videoUrl")[0].value = url;
		if(document.getElementsByName("interviewVideo.subject")[0].value=="") {
			document.getElementsByName("interviewVideo.subject")[0].value = StringUtils.utf8Decode(url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.')));
		}
	}
</script>
<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<tr>
		<td width="50px" align="right">视频：</td>
		<td><ext:field property="interviewVideo.videoUrl"/></td>
	</tr>
	<tr>
		<td align="right">标题：</td>
		<td><ext:field property="interviewVideo.subject"/></td>
	</tr>
</table>