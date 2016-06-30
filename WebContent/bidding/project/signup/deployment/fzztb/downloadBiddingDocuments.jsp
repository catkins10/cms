<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>

<ext:form action="/downloadBiddingDocuments" applicationName="bidding/project/signup" pageName="downloadBiddingDocuments">
	报 名 号：<ext:write property="signUpNo"/><br><br>
	招标文件：
	<ext:iterateAttachment id="attachment" indexId="attachmentIndex" propertyRecordId="projectId" applicationName="bidding/project" attachmentType="biddingDocuments">
		<a href="<ext:write name="attachment" property="urlInline"/>" target="_blank">
			 <ext:writeNumber plus="1" name="attachmentIndex"/>、<ext:write name="attachment" property="title"/>
		</a>&nbsp;
	</ext:iterateAttachment><br><br>
	图　　纸：
	<ext:iterateAttachment id="attachment" indexId="attachmentIndex" propertyRecordId="projectId" applicationName="bidding/project" attachmentType="biddingDrawing">
		<a href="<ext:write name="attachment" property="urlInline"/>" target="_blank">
			 <ext:writeNumber plus="1" name="attachmentIndex"/>、<ext:write name="attachment" property="title"/>
		</a>&nbsp;
	</ext:iterateAttachment><br><br>
	其它文件：
	<ext:iterateAttachment id="attachment" indexId="attachmentIndex" propertyRecordId="projectId" applicationName="bidding/project" attachmentType="otherBiddingDocuments">
		<a href="<ext:write name="attachment" property="urlInline"/>" target="_blank">
			 <ext:writeNumber plus="1" name="attachmentIndex"/>、<ext:write name="attachment" property="title"/>
		</a>&nbsp;
	</ext:iterateAttachment>
	<script>
	function printSignUpNo() {
		window.open('<%=request.getContextPath()%>/bidding/project/signup/signUpPrint.shtml?signUpNo=<ext:write property="signUpNo"/>');
	}
	</script>
</ext:form>