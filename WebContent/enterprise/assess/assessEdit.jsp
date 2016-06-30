<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@page import="com.yuanluesoft.enterprise.assess.pojo.*"%>
<%@page import="com.yuanluesoft.jeaf.util.ListUtils"%>

<ext:iterate id="assessResult" property="results">
	<b><ext:write name="assessResult" property="personName"/></b>
	<ext:select id="assessClassify" property="assessClassifies" select="id" nameValue="assessResult" propertyValue="classifyId">
		<table width="100%" class="table" border="1" cellpadding="3" cellspacing="0">
			<tr align="center">
				<td width="40%" nowrap="nowrap" class="tdtitle" align="center">考核内容</td>
				<td width="60%" nowrap="nowrap" class="tdtitle" align="center">考核说明及评分标准</td>
				<ext:iterate id="assessActivity" name="assessClassify" property="activities">
					<td nowrap nowrap="nowrap" class="tdtitle" align="center">&nbsp;<ext:write name="assessActivity" property="activity"/>&nbsp;</td>
				</ext:iterate>
			</tr>
			<ext:iterate id="assessStandard" name="assessClassify" property="standards">
				<tr style="background:#ffffff" valign="top">
					<td class="tdcontent"><ext:write name="assessStandard" property="content"/></td>
					<td class="tdcontent"><ext:write name="assessStandard" property="standard"/></td>
					<ext:iterate id="assessActivity" name="assessClassify" property="activities">
<%						AssessResult assessResult = (AssessResult)pageContext.getAttribute("assessResult");
						AssessStandard assessStandard = (AssessStandard)pageContext.getAttribute("assessStandard");
						AssessActivity assessActivity = (AssessActivity)pageContext.getAttribute("assessActivity");
						AssessIndividualResult assessIndividualResult = (AssessIndividualResult)ListUtils.findObjectByProperty(ListUtils.getSubListByProperty(assessResult.getIndividualResults(), "contentId", new Long(assessStandard.getId())), "activityId", new Long(assessActivity.getId()));
						pageContext.setAttribute("assessIndividualResult", assessIndividualResult);%>
						<td class="tdcontent" align="center">
							<ext:equal propertyCompare="activityName" name="assessActivity" property="activity">
								<input type="text" value="<ext:write name="assessIndividualResult" property="result" format="#.##"/>" name="result_<ext:write name="assessResult" property="personId"/>_<ext:write name="assessStandard" property="id"/>_<ext:write name="assessActivity" property="id"/>"><input type="hidden" name="maxValue" value="<ext:write name="assessStandard" property="maxValue"/>">
							</ext:equal>
							<ext:notEqual propertyCompare="activityName" name="assessActivity" property="activity">
								<ext:write name="assessIndividualResult" property="result" format="#.##"/>
							</ext:notEqual>
						</td>
					</ext:iterate>
				</tr>
			</ext:iterate>
			<tr style="background:#ffffff; font-weight:bold" align="center" height="25px">
				<td class="tdcontent" colspan="2">合计</td>
				<td class="tdcontent" colspan="<ext:writeNumber name="assessClassify" property="activities"/>">
					<ext:notEqual value="0.0" name="assessResult" property="result">
						<ext:write name="assessResult" property="result" format="#.##"/>
					</ext:notEqual>
				</td>
			</tr>
		</table>
	</ext:select>
	<br><br>
</ext:iterate>

<html:hidden property="teamId"/>
<script>
function formOnSubmit() {
	var inputs = document.getElementsByTagName("input");
	for(var i=0; i<inputs.length; i++) {
		if(inputs[i].name.substring(0, "result_".length)!="result_") {
			continue;
		}
		if(inputs[i].value=="") {
			alert("未输入考核分数");
			inputs[i].focus();
			return;
		}
		var value = Number(inputs[i].value);
		if("" + value=="NaN" || value<0 || value>Number(inputs[i].nextSibling.value)) {
			alert("考核分数不正确");
			inputs[i].focus();
			return;
		}
	}
	return true;
}
</script>