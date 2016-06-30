<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<script type="text/javascript">
<!--
//重置选择的条目。取出快速过滤时产生的html高亮标签
function resetSelects(target){
 var value=target.value;

 var reg=new RegExp("<[^/]{1,}>","g"); //创建正则RegExp对象,以<开头>结尾，不包含‘/’，。{1，}匹配前面的字符一次或多个即标签
 value=value.replace(reg,'');//去除开始标签
 reg=new RegExp("</[^/]{1,}>","g");//只包含一个/
 value=value.replace(reg,'');//去除结尾标签
 target.value=value;
}
</script>
<table  width="100%" border="0" cellpadding="3" cellspacing="0" >
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td  nowrap="nowrap">参赛代表团</td>
		<td  ><ext:field property="player"/></td>
		<td  nowrap="nowrap">比赛项目名称</td>
		<td  ><ext:field property="gameName" onchange="resetSelects(this)"/></td>
	</tr>
	<tr>
		<td  nowrap="nowrap">分数</td>
		<td ><ext:field property="score"/></td>
		<td  nowrap="nowrap">金牌数</td>
		<td><ext:field property="goldMedalCount"/></td>
	</tr>
	<tr>
		<td  nowrap="nowrap">银牌数</td>
		<td ><ext:field property="silverMedalCount"/></td>
		<td  nowrap="nowrap">铜牌数</td>
		<td ><ext:field property="bronzeMedalCount"/></td>
	</tr>
</table>
