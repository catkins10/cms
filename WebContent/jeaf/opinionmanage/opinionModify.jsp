<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function modifyOpinion(opinionId) {
		<ext:equal value="true" property="opinionPackage.modifiable">
			DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/opinionmanage/opinion.shtml?mainRecordClassName=<ext:write property="opinionPackage.mainRecordClassName"/>&id=' + opinionId, 500, 300);
		</ext:equal>
	}
</script>