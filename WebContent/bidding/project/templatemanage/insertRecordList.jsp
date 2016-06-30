<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertRecordList">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/recordlist/js/insertRecordListExtend.js"></script>
	<script type="text/javascript">
	//显示扩展属性,由继承者实现
	function showExtendProperties(recordListProperties, recordListTextContent) {
		if(recordListProperties=="") {
			return;
		}
		showPropertyValue('categories', StringUtils.getPropertyValue(recordListProperties, "category"));
		showPropertyValue('procedures', StringUtils.getPropertyValue(recordListProperties, "procedure"));
		showPropertyValue('cities', StringUtils.getPropertyValue(recordListProperties, "city"));
		showPropertyValue('biddingModes', StringUtils.getPropertyValue(recordListProperties, "biddingMode"));
	}
	function showPropertyValue(propertyName, propertyValue) {
		var multiboxes = document.getElementsByName(propertyName);
		for(var i=0; i<multiboxes.length; i++) {
			multiboxes[i].checked = (',' + propertyValue + ',').indexOf(',' + multiboxes[i].value + ',')!=-1;
		}
	}
	//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
	function getExtendPropertiesAsText() {
		var category = generatePropertyValue("categories");
		var procedure = generatePropertyValue("procedures");
		var city = generatePropertyValue("cities");
		var biddingMode = generatePropertyValue("biddingModes");
		var properties = (category=="" ? "" : "&category=" + category) +
						 (procedure=="" ? "" : "&procedure=" + procedure) +
						 (city=="" ? "" : "&city=" + city) +
						 (biddingMode=="" ? "" : "&biddingMode=" + biddingMode);
		return properties=="" ? properties : properties.substring(1);
	}
	function generatePropertyValue(propertyName) {
		var values = '';
		var multiboxes = document.getElementsByName(propertyName);
		for(var i=0; i<multiboxes.length; i++) {
			if(multiboxes[i].checked) {
				values += (values=="" ? "" : ",") + multiboxes[i].value;
			}
		}
		return values;
	}
	</script>
	<table id="tableResources" border="0" width="100%" cellspacing="3" cellpadding="0px">
		<col align="right">
		<col width="100%">
		<tr valign="middle">
			<td nowrap="nowrap" valign="top" style="padding-top: 5px">项目分类：</td>
			<td><ext:field property="categories"/></td>
		</tr>
		<tr valign="middle">
			<td nowrap="nowrap" valign="top" style="padding-top: 5px">招标内容：</td>
			<td><ext:field property="procedures"/></td>
		</tr>
		<tr valign="middle">
			<td nowrap="nowrap" valign="top" style="padding-top: 5px">地区：</td>
			<td><ext:field property="cities"/></td>
		</tr>
		<tr valign="middle">
			<td nowrap="nowrap">招标方式：</td>
			<td><ext:field property="biddingModes"/></td>
		</tr>
	</table>
</ext:form>