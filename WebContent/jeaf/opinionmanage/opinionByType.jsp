<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<jsp:include page="opinionModify.jsp" />
<table width="100%" border="0" cellpadding="3" cellspacing="0" bordercolor="black" bgcolor="">
  <tr>
    <ext:notEmpty property="opinionPackage.opinionList">
      <td valign="top" width="50%" style="border-right:0 solid black">
	    <jsp:include page="opinionListByType.jsp" />
      </td>
    </ext:notEmpty>
    <td valign="top">
      <ext:notEmpty property="opinionPackage.opinionType">
     	<jsp:include page="opinionInput.jsp" />
      </ext:notEmpty>
    </td>
  </tr>
</table>