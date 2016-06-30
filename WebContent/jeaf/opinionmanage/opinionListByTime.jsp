<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table id="opinionList" width="100%" border="0" cellpadding="3" cellspacing="0" bordercolor="#2D5C7A" bgcolor="" style="table-layout:fixed">
  <ext:iterate id="currentOpinion" property="opinionPackage.opinionList">
    <ext:notEmpty name="currentOpinion" property="opinionType">
       <tr ondblclick="modifyOpinion('<ext:write name="currentOpinion" property="id"/>')">
          <td colspan="2"><ext:write name="currentOpinion" property="opinionType"/>意见:</td>
        </tr>
    </ext:notEmpty>
      <tr>
        <td colspan="2"><pre style="word-wrap:break-word">&nbsp;&nbsp;&nbsp;&nbsp;<ext:write name="currentOpinion" property="opinion"/></pre></td>
      </tr>
      <tr>
        <td style="border-bottom:1 solid #ccccff">&nbsp; </td>
        <td style="border-bottom:1 solid #ccccff; padding-right: 20px" align="right"><ext:write name="currentOpinion" property="personName"/>&nbsp;<ext:write name="currentOpinion" property="created" format="yyyy-MM-dd"/><ext:empty name="currentOpinion" property="opinionType">&nbsp;(<ext:write name="currentOpinion" property="activityName"/><ext:notEmpty name="currentOpinion" property="agentName"><ext:notEqual name="currentOpinion" property="agentName" nameCompare="currentOpinion" propertyCompare="personName">,<ext:write name="currentOpinion" property="agentName"/>代</ext:notEqual></ext:notEmpty>)</ext:empty></td>
      </tr>
  </ext:iterate>
</table>