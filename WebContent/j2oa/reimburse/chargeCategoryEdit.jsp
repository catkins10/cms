<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
  <col valign="middle">
  <col valign="middle" width="100%">
  <tr>
    <td class="tdtitle" nowrap="nowrap">费用类别</td>
    <td class="tdcontent"><ext:field property="category"/></td>
  </tr>
  <tr>
    <td class="tdtitle" nowrap="nowrap">默认标准(元)</td>
    <td class="tdcontent"><ext:field property="money" onfocus="select()"/></td>
  </tr>
  <tr>
    <td class="tdtitle" nowrap="nowrap">单位</td>
    <td class="tdcontent"><ext:field property="unit"/></td>
  </tr>
</table>