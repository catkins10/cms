<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html:html>
<head>
	<link href="<%=request.getContextPath()%>/edu/css/application.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/edu/skins/v2/default/css/form.css.jsp" rel="stylesheet" type="text/css" />
	<script>
		function openChargeTransaction(id, type) {
			var url;
			switch(type) {
			case 1: //充值卡充值
				url = "<%=request.getContextPath()%>/charge/topup/cardTopUpTransaction.shtml";
				break;
				
			case 2: //代缴费
				url = "<%=request.getContextPath()%>/charge/topup/agencyTopupTransaction.shtml";
				break;
				
			case 3: //服务费扣除
				url = "<%=request.getContextPath()%>/charge/servicemanage/serviceDeductTransaction.shtml";
				break;
				
			case 4: //增值服务费用支付
				url = "<%=request.getContextPath()%>/charge/payment/paymentTransaction.shtml";
				break;
				
			case 5: //打入9191edu个人账户的报酬
				url = "<%=request.getContextPath()%>/charge/laborage/payoffTransaction.shtml";
				break;
			
			default:
				return;
			}
			location = url + "?act=open&id=" + id;
		}
	</script>
</head>
<body>
	<ext:form action="/displayChargeTransactions">
		<table border="0" cellpadding="3" cellspacing="3">
			<tr>
				<td nowrap>开始时间：</td>
				<td><ext:field property="beginDate" style="width:90px"/></td>
				<td nowrap>结束时间：</td>
				<td><ext:field property="endDate" style="width:90px"/></td>
				<td nowrap>交易类型：</td>
				<td><ext:field property="typeTitle" itemsProperty="typeTitles" style="width:120px"/></td>
				<td><input type="button" onclick="FormUtils.doAction('listChargeTransactions')" class="button" value="查询"/></td>
			</tr>
		</table>
		<jsp:include page="/edu/view/viewPackage.jsp" />
	</ext:form>
</body>
</html:html>