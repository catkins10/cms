<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>

<html>
<body>
	<form action="<%=request.getContextPath()%>/cms/inquiry/submitInquiry.shtml" method="post" id="form_<ext:write name="adminInquirySubject" property="id"/>" target="_blank" style="margin:0px">
		<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
		<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/inquiry/js/inquiry.js"></script>
		<style>
			.inquirySupplement {
				overflow: auto;
				width: 300px !important;
				height: 80px !important;
			}
		</style>
		<ext:write name="adminInquirySubject" property="subject"/><br/>
		<ext:notEmpty name="adminInquirySubject" property="description"><ext:write name="adminInquirySubject" property="description" filter="false"/><br/></ext:notEmpty>
		<ext:iterate id="inquiry" indexId="inquiryIndex" name="adminInquirySubject" property="inquiries">
			<ext:equal value="1" name="adminInquirySubject" property="isQuestionnaire">
				<ext:writeNumber name="inquiryIndex" plus="1"/>、<ext:write name="inquiry" property="description" filter="false"/><br/>
			</ext:equal>
			<ext:iterate id="option" name="inquiry" property="options">
				<ext:write name="option" property="inquiryBox" filter="false"/>
				<ext:empty name="option" property="description">
					<label for="<ext:write name="option" property="id"/>"><ext:write name="option" property="inquiryOption"/></label>
				</ext:empty>
				<ext:notEmpty name="option" property="description">
					<a href="<ext:write name="option" property="optionDetailUrl"/>" target="_blank"><ext:write name="option" property="inquiryOption"/></a>
				</ext:notEmpty>
				<ext:write name="option" property="supplementInputBox" filter="false"/>
				<br/>
			</ext:iterate>
		</ext:iterate>
		<br>
		<span style="text-align:center; display:block;">
			<input type="button" class="button" value="提交" onclick="submitInquiry('form_<ext:write name="adminInquirySubject" property="id"/>')"/>
			<input type="button" class="button" value="查看结果" onclick="showInquiryResult('form_<ext:write name="adminInquirySubject" property="id"/>')"/>
		</span>
		<INPUT name="inquiryIds" type="hidden" value="">
		<INPUT name="inquiryResult" type="hidden" value="">
		<html:hidden name="adminInquirySubject" property="siteId"/>
	</form>
</body>
</html>