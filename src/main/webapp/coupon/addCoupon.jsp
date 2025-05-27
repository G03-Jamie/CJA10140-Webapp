<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.foodtimetest.coupon.model.*"%> 


<%
CouponVO couponVO = (CouponVO) request.getAttribute("couponVO");//	把取出的 Object 強制轉型成 CouponVO 類別
%>
--<%= couponVO == null%>--${couponVO.couId}


<%
// 只要有輸入資料按送出，上面那行就會變成false+selected編號
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>優惠券資料新增 - addCoupon.jsp</title>
<style>
table#table-1 {
	background-color: #CCCCFF;
	border: 2px solid black;
	text-align: center;
}

table#table-1 h4 {
	color: red;
	display: block;
	margin-bottom: 1px;
}

h4 {
	color: blue;
	display: inline;
}
</style>
<style>
table {
	width: 450px;
	background-color: white;
	margin-top: 1px;
	margin-bottom: 1px;
}

table, th, td {
	border: 0px solid #CCCCFF;
}

th, td {
	padding: 1px;
}
</style>
</head>
<body bgcolor='white'>

	<table id="table-1">

		<%--回首頁 --%>
		<tr>
			<td>
				<h3>優惠券資料新增 - addCoupon.jsp</h3>
				<h4>
					<img src="images/tomcat.png"
						width="50" height="50" border="0"><br>
<!-- 						<a href="select_page.jsp">回首頁 </a> -->
						<a href="${pageContext.request.contextPath}/coupon/coupon.do">回首頁 </a>
					
				</h4>
			</td>
		</tr>
	</table>

	<h3>資料新增:</h3>
	<%-- 錯誤表列 --%>
	<c:if test="${not empty errorMsgs}">
		<span style="color: red">請修正以下錯誤:</span>
		<ul>
			<c:forEach var="message" items="${errorMsgs}"><%--是從 Servlet 傳來的 List --%>  
			<li style="color: red">${message}</li>
			</c:forEach>
		</ul>
	</c:if>

	<FORM METHOD="post"
		ACTION="coupon.do"
		name="formm1">
	<table>

		<tr>
			<td>店家編號:</td>
			<td><input type="text" name="storId"
				value="<%=(couponVO == null) ? "1" : couponVO.getStorId()%>"
				size="45" /></td>
		</tr>
		<tr>
			<td>優惠券類型:</td>
			<td><input type="text" name="couType"
				value="<%=(couponVO == null) ? "新開幕" : couponVO.getCouType()%>"
				size="45" /></td>
		</tr>
		<tr>
			<td>優惠券說明:</td>
			<td><input type="text" name="couDes"
				value="<%=(couponVO == null) ? "買500送500" : couponVO.getCouDes()%>"
				size="45" /></td>
		</tr>
		<tr>
			<td>最低消費金額限制:</td>
			<td><input type="text" name="couMinOrd" 
				value="<%=(couponVO == null) ? "500" : couponVO.getCouMinOrd()%>"
				size="45" /></td>
		</tr>
		<tr>
			<td>使用期限:</td>
			<td><input type="text" name="couDate" id="c_date"></td>
		</tr>
		
<%--店家選單 --%>	
<%-- <jsp:useBean id="storeSvc" scope="page" class="com.foodtimetest.store.model.StoreService" /> --%>
<!-- 	<tr> -->
<!-- 		<td>店家:<font color=red><b>*</b></font></td> -->
<!-- 		<td><select size="1" name="storId"> -->
<%-- 			<c:forEach var="storeVO" items="${storeSvc.all}"> --%>
<%-- 				<option value="${storeVO.storId}" ${(couponVO.storId==storeVO.storId)?'selected':'' } >${storeVO.storName}</option> --%>
<%-- 			</c:forEach> --%>
<!-- 		</select></td> -->
<!-- 	</tr> -->
	</table>
	<br>
	<input type="hidden" name="action" value="insert">
	<input type="submit" name="action" value="送出新增"></FORM>

<!-- =========================================以下為 datetimepicker 之相關設定========================================== -->

<% 
  java.sql.Timestamp couDate = null;  
  try {
	  couDate = couponVO.getCouDate();
  } catch (Exception e) {
	  couDate = new java.sql.Timestamp(System.currentTimeMillis());
  }
%>



<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/datetimepicker/jquery.datetimepicker.css" />
<script src="<%=request.getContextPath()%>/datetimepicker/jquery.js"></script>
<script src="<%=request.getContextPath()%>/datetimepicker/jquery.datetimepicker.full.js"></script>

<style>
  .xdsoft_datetimepicker .xdsoft_datepicker {
           width:  300px;   /* width:  300px; */
  }
  .xdsoft_datetimepicker .xdsoft_timepicker .xdsoft_time_box {
           height: 151px;   /* height:  151px; */
  }
</style>

<script>
$.datetimepicker.setLocale('zh');
$('#c_date').datetimepicker({
	theme: '',
	timepicker:true, //有日期 + 時間（年月日＋時分秒）
	format: 'Y-m-d H:i:s',
	value: new Date(),
    minDate: 0  //只能選今天以後
});
</script>



</body>
</html>