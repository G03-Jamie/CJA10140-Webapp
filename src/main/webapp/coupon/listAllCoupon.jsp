<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.*" %>
<%@ page import="com.foodtimetest.coupon.model.*"%>

<%-- 此頁練習採用 EL 的寫法取值 --%>
<% 
	CouponService couSvc = new CouponService(); //用Service呼叫
	List<CouponVO> list = couSvc.getAll();
	pageContext.setAttribute("list",list);
	
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>所有優惠券資料 - listAllCoupon.jsp</title>

<style>
	table#table-1 {
	background-color: #CCCCFF;
		border: 2px solid black;
		text-align: center;
	}
	table#table-1 h4{
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
	width: 800px;
	background-color: white;
	margin-top: 5px;
	margin-bottom: 5px;
	}
	table, th, td{
	border: 1px solid #CCCCFF;
	}
	}
	h4 {
	color: blue;
	display: inline;
	}
	th, td {
		padding: 5px;
		text-align: center;
	}
</style>


</head>
<body bgcolor='white'>

<h4>此頁練習採用 EL 的寫法取值:</h4>
<table id="table-1">
	<tr><td>
	    <h3>所有優惠券資料 - listAllCoupon.jsp</h3>
<!-- 	    <a href="select_page.jsp"><img src="images/back1.gif" width="100" height="32" border="0">回首頁</a> -->
         <h4>
	    <a href="${pageContext.request.contextPath}/coupon/coupon.do"><img src="images/back1.gif" width="100" height="32" border="0">回首頁 </a>
	    </h4>
	</td></tr>
</table>

<table>
     <%--table欄位名稱 --%>
	<tr>
		<th>優惠券編號</th>
		<th>店家編號</th>
		<th>優惠券類型</th>
		<th>優惠券說明</th>
		<th>最低消費金額限制</th>
		<th>使用期限</th>
		<th>修改</th>
		<th>刪除</th>
	</tr>
	
	 <%--顯示查詢分頁 --%>
	<%@ include file="page1.file" %> 
	<c:forEach var="couponVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">

     <%--顯示每個欄位內容 --%>
    <tr>
    	<td>${couponVO.couId }</td>
    	<td>${couponVO.storId }</td>
    	<td>${couponVO.couType }</td>
    	<td>${couponVO.couDes }</td>
    	<td>${couponVO.couMinOrd }</td>
    	<td><fmt:formatDate value="${couponVO.couDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
    	
    	<%--修改按鈕 --%>
    	<td>
    		<FORM METHOD="post" ACTION="${pageContext.request.contextPath}/coupon/coupon.do" style="margin-bottom: 0px;"> <%-- 傳送給 /coupon/coupon.do 這支 Servlet 來處理。--%> 
    			<input type="submit" value="修改"> <%--hidden使用者不需要看見（避免誤刪、誤改），但後端需要用 --%>
    			<input type="hidden" name="couId" value="${couponVO.couId}"> <%-- 表單送出時將 couponVO.couId 傳給後端的 request.getParameter("couId")--%>
    			<input type="hidden" name="action" value="getOne_For_Update"></FORM> <%--告訴 Servlet 處理getOne_For_Update 功能--%>
    	</td>
    	<td>
    		<FORM METHOD="post" ACTION="${pageContext.request.contextPath}/coupon/coupon.do" style="margin-bottom: 0px;">
    			<input type="submit" value="刪除">
    			<input type="hidden" name="couId" value="${couponVO.couId}">
    			<input type="hidden" name="action" value="delete"></FORM>
    	</td>
    	</tr>   		
    </c:forEach>
    </table>
    
<%@ include file="page2.file" %>
</body>
</html>