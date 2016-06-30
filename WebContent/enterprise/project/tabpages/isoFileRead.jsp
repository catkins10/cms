<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<ext:iterate id="isoFile" property="isoFiles">
	<b><ext:write name="isoFile" property="activityInstance.name"/><ext:notEmpty name="isoFile" property="projectTeam">(<ext:write name="isoFile" property="projectTeam.stage"/>)</ext:notEmpty>：</b><br>
<%	
	com.yuanluesoft.enterprise.project.model.IsoFile isoFile = (com.yuanluesoft.enterprise.project.model.IsoFile)pageContext.getAttribute("isoFile");
%>
	<div style="padding-top:5px; padding-bottom:16px">
		<ext:iterateAttachment id="attachment" indexId="attachmentIndex" propertyRecordId="id" propertyApplicationName="formDefine.applicationName" attachmentType="<%=isoFile.getAttachmentType()%>">
			<a href="<ext:write name="attachment" property="urlInline"/>" target="_blank">
				 <ext:writeNumber plus="1" name="attachmentIndex"/>、<ext:write name="attachment" property="title"/>
			</a>&nbsp;
		</ext:iterateAttachment>
	</div>
</ext:iterate>