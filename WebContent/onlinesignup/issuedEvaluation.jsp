<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/onlinesignup/js/signup.js"></script>

<table border="0" cellpadding="1" cellspacing="0">
	<tr>
		<td class="tdcontent">
			<select name="s_province" id="s_province" style="width: 100px" onchange="setCity(this.value);setArea(this,'province');">
	        	<option selected="selected" value="">省</option>
	        </select>
	        <select name="s_city" id="s_city" style="width: 100px"  onchange="setCountry(this.value);setArea(this,'city')">
        		<option selected="selected" value="">市</option>
        	</select>
        	<select name="s_country" id="s_country" style="width: 100px"  onchange="setArea(this,'country')">
        		<option selected="selected"  value="">县</option>
        	</select>
        </td>
        <td>&nbsp;<input type="button" class="button" value="确定" onclick="refreshView()"></td>
	</tr>
</table>
<div style="visibility:hidden;">
<div style="float: left; width: 30%;"><ext:field property="province"></ext:field></div>
<div style="float: left; width: 30%;"><ext:field property="city"></ext:field></div>
<div style="float: left; width: 30%;"><ext:field property="country"></ext:field></div>
</div>
<script language="javascript" type="text/javascript">
	setTimeout(fillProvince,10);

</script>