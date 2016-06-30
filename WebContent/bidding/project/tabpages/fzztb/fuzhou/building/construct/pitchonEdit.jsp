<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<jsp:include flush="true" page="/bidding/project/admin/tabpages/fzztb/fuzhou/building/construct/pitchonEdit.jsp"/>
<br>
<center>
	<ext:button name="提交中标结果公示" onclick="submitPitchon()"/>
	<ext:contains property="formActions" propertyName="title" propertyValue="招标失败">
		<ext:button name="招标失败" onclick="biddingFailed()"/>
		<script>
		function biddingFailed() {
			if(!confirm('是否确定招标失败？')) {
				return false;
			}
			FormUtils.doAction('biddingFailed');
		}
		</script>
	</ext:contains>
</center>
<script>
function submitPitchon() {
	if(<ext:empty property="pitchon.pitchonEnterprise">true</ext:empty><ext:notEmpty property="pitchon.pitchonEnterprise">true</ext:notEmpty> && document.getElementsByName("pitchon.pitchonEnterprise")[0] && document.getElementsByName("pitchon.pitchonEnterprise")[0].value=="") {
		alert('中标人为空,不允许提交');
		return false;
	}
	if(!confirm('是否确定提交中标结果？')) {
		return false;
	}
	FormUtils.doAction('submitPitchon');
}
</script>