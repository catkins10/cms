<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/onlinesignup/js/signup.js"></script>
<%
	request.setAttribute("editabled", "true");
%>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdcontent"><ext:field property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">性别</td>
		<td class="tdcontent"><ext:field property="sex"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">毕业学校</td>
		<td class="tdcontent"><ext:field property="school"/></td>
		<td class="tdtitle" nowrap="nowrap" >身份证号</td>
		<td class="tdcontent"><ext:field property="idCard"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">中考成绩（应届生填）</td>
		<td class="tdcontent"><ext:field property="score"/></td>
		<td class="tdtitle" nowrap="nowrap">准考证号（应届生填）</td>
		<td class="tdcontent"><ext:field property="candidateNo"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所在地</td>
		<td class="tdcontent">
			<select name="s_province" id="s_province" style="width: 32%;" class="field required" onclick="" onchange="setCity(this.value);setArea(this,'province');">
	        	<option selected="selected" value="">省</option>
	        </select>
	        <select name="s_city" id="s_city" style="width: 32%;" class="field required" onchange="setCountry(this.value);setArea(this,'city')">
        		<option selected="selected" value="">市</option>
        	</select>
        	<select name="s_country" id="s_country" style="width: 32%;" class="field required" onchange="setArea(this,'country');setAddr();">
        		<option selected="selected" value="">县</option>
        	</select>
        </td>
		<td class="tdtitle" nowrap="nowrap">家庭住址</td>
		<td class="tdcontent"><ext:field property="addr"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">邮政编码</td>
		<td class="tdcontent"><ext:field property="postalCode"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="phone"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">报读专业</td>
		<td class="tdcontent"><ext:field property="major"/></td>
		<td class="tdtitle" nowrap="nowrap">电子邮箱</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorMail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">状态</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="status"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field  property="created"/></td>
	</tr>
</table>
<div style="visibility:hidden;">
<ext:field property="province"></ext:field>
<ext:field property="city"></ext:field>
<ext:field property="country"></ext:field>
</div>
<script language="javascript" type="text/javascript">
	setTimeout(fillProvince,10);

</script>