<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
  <col>
  <col valign="middle" width="100%">
  <tr>
    <td class="tdtitle" nowrap="nowrap">归档到档案系统</td>
    <td class="tdcontent"><ext:field property="toArchives"/></td>
  </tr>
  <tr>
    <td class="tdtitle" nowrap="nowrap">归档到资料库</td>
    <td class="tdcontent"><ext:field property="toDatabank"/></td>
  </tr>
  <tr>
  	<td class="tdtitle" nowrap="nowrap">归档目录</td>
	<td class="tdcontent"><ext:field property="directoryName"/></td>
  </tr>
  <tr>
    <td class="tdtitle" nowrap="nowrap">是否按年度归档</td>
    <td class="tdcontent"><ext:field property="createDirectoryByYear"/></td>
  </tr>
</table>