<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table">
  <col valign="middle" width="80px">
  <col valign="middle" width="100%">
  <tr>
    <td class="tdtitle" nowrap="nowrap">用户名</td>
    <td class="tdcontent"><ext:field property="userName"/></td>
  </tr>
  <tr>
    <td class="tdtitle" nowrap="nowrap">费用类别</td>
    <td class="tdcontent"><ext:field onchange="getChargeStandard()" property="selectedCategory"/>
  </tr>
  <tr>
    <td class="tdtitle" nowrap="nowrap">标准</td>
    <td class="tdcontent"><ext:field property="money" onfocus="select()"/></td>
  </tr>
  <tr>
    <td class="tdtitle" nowrap="nowrap">单位</td>
    <td id="unit" class="tdcontent"><ext:write property="unit" /></td>
  </tr>
</table>
<script>
function getChargeStandard() {
	ScriptUtils.appendJsFile(document, "getChargeStandard.shtml?chargeCategory=" + StringUtils.utf8Encode(document.getElementsByName("selectedCategory")[0].value) + "&seq=" + Math.random(), "scriptGetChargeStandard");
}
function afterGetChargeStandard(standard) {
	if(standard!="NOSESSIONINFO" && standard!="") {
		var index = standard.lastIndexOf("/");
		//if(new Number(document.getElementsByName("money")[0].value)==0) {
		//	document.getElementsByName("money")[0].value = new Number((index==-1 ? standard : standard.substring(0, index - 1)).replace('元', ''));
		//}
		document.getElementById("unit").innerHTML = (index==-1 ? "" : standard.substring(index + 1));
	}
}
</script>