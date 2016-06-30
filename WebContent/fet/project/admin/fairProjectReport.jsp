<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html>
<head>
	<title>活动选择</title>
	<link type="text/css" href="<%=request.getContextPath()%>/jeaf/form/css/form.css" rel="stylesheet">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
</head>
<body>
    <ext:form action="/admin/writeFairProjectReport">
      <center>
        <br><br>
    	<table border="0" cellpadding="3" cellspacing="0" width="300px">
    		<tr>
    			<td width="80px" nowrap="nowrap" align="right">活动名称：</td>
    			<td width="100%">
	    			<ext:field property="fairName"/>
		    	</td>
    		</tr>
    		<tr>
    			<td align="right">界别：</td>
    			<td><ext:field property="fairNumber" value=""/></td>
    		</tr>
    		<tr>
    			<td colspan="2" align="center" style="padding-top:10px">
    				<input type="button" class="button" value="确定" style="width:50px" onclick="FormUtils.submitForm()"/>&nbsp;
    				<input type="button" class="button" value="取消" style="width:50px" onclick="window.close()"/>
    			</td>
    		</tr>
    	</table>
      </center>
    </ext:form>
</body>
</html>
