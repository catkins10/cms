<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
  <col valign="middle">
  <col valign="middle" width="100%">
  <tr>
    <td class="tdtitle" nowrap="nowrap">用户名</td>
    <td class="tdcontent"><ext:field writeonly="true" property="userName"/></td>
  </tr>
  <tr>
    <td class="tdtitle" nowrap="nowrap">费用类别</td>
    <td class="tdcontent"><ext:field writeonly="true" property="selectedCategory"/></td>
  </tr>
  <tr>
    <td class="tdtitle" nowrap="nowrap">标准</td>
    <td class="tdcontent"><ext:field writeonly="true" property="money"/></td>
  </tr>
  <tr>
    <td class="tdtitle" nowrap="nowrap">单位</td>
    <td class="tdcontent"><ext:write property="unit" /></td>
  </tr>
</table>