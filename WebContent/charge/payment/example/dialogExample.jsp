<%@ page contentType="text/html; charset=UTF-8"%>

<html>
<head>
	<title>支付：对话框方式</title>
	<script type="text/javascript" src="https://passport.9191edu.com/charge/payment/js/payment.js"></script>
	<script type="text/javascript">
		function payment() {
			openPaymentDialog(document.getElementsByName("applicationName")[0].value, document.getElementsByName("payReason")[0].value, document.getElementsByName("providerId")[0].value, document.getElementsByName("providerName")[0].value, document.getElementsByName("money")[0].value, document.getElementsByName("providerMoney")[0].value);
		}
		function onPrepay(prepayBillId) {
			var url = "http://www.9191edu.com/charge/payment/example.shtml";
			document.forms[0].action = url + "?prepayBillId=" + prepayBillId;
			document.forms[0].submit();
		}
	</script>
	<style>
		body,input{font-size:12px}
	</style>
</head>
<body>
	<form action="/" method="post">
		　　　应用名称：<input name="applicationName" type="text"><br>
		　　　支付目的：<input name="payReason" type="text"><br>
		服务提供者ＩＤ：<input name="providerId" type="text"><br>
		服务提供者姓名：<input name="providerName" type="text"><br>
		　　　　　金额：<input name="money" type="text"><br>
		服务提供者报酬：<input name="providerMoney" type="text"><br>
		　　　　　　　　　　<input type="button" value="付款" onclick="payment()">
	</form>
</body>
</html>