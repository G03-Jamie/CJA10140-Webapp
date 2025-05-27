<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.foodtimetest.coupon.model.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<%-- 此頁暫練習採用 Script 的寫法取值 --%>

<%
  CouponVO couponVO = (CouponVO) request.getAttribute("couponVO"); //CouponServlet.java(Concroller), 存入req的couponVO物件
%>

<html>
<head>
<title>優惠券資料 - listOneCoupon.jsp</title>

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
	width: 600px;
	background-color: white;
	margin-top: 5px;
	margin-bottom: 5px;
  }
  table, th, td {
    border: 1px solid #CCCCFF;
  }
  th, td {
    padding: 5px;
    text-align: center;
  }
</style>

</head>
<body bgcolor='white'>

<table id="table-1">
	<tr><td>
		 <h3>優惠券資料 - listOneCoupon.jsp</h3>
		 <h4><a href="${pageContext.request.contextPath}/coupon/coupon.do"><img src="images/back1.gif" width="100" height="32" border="0">回首頁</a></h4>
	</td></tr>
</table>

<table>
	<tr>
		<th>優惠券編號</th>
		<th>店家編號</th>
		<th>優惠券說明</th>
		<th>優惠券類型</th>
		<th>最低消費金額限制</th>
		<th>使用期限</th>
	</tr>
<%-- 	<c:forEach var="coupon" items="${couponList}"> --%>
		<tr>
			<td><%=couponVO.getCouId()%></td>
			<td><%=couponVO.getStorId()%></td>
			<td><%=couponVO.getCouDes()%></td>
			<td><%=couponVO.getCouType()%></td>
			<td><%=couponVO.getCouMinOrd()%></td>
			<td><%=couponVO.getCouDate()%></td>
		</tr>
<%-- 	</c:forEach> --%>
</table>

</body>
</html>