<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.foodtimetest.coupon.model.*"%>
<%@ page import="com.foodtimetest.store.model.*"%>

<%--(此為從資料庫取出的couponVO, 也可以是輸入格式有錯誤時的couponVO物件)--%> 
<%  CouponVO couponVO = (CouponVO) request.getAttribute("couponVO");%>
<%  StoreVO storeVO = (StoreVO) request.getAttribute("storeVO");%>
--<%= couponVO==null %>--  ${couponVO.storId} 
<%// 只要有輸入資料按送出，上面那行就會變成false+selected編號%>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
<title>優惠券資料修改 - update_coupon_input.jsp</title>

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
	<tr><td>
		 <h3>優惠券資料修改 - update_coupon_input.jsp</h3>
<!-- 		 <h4><a href="select_page.jsp"><img src="images/back1.gif" width="100" height="32" border="0">回首頁</a></h4> -->
		 <a href="${pageContext.request.contextPath}/coupon/coupon.do"><img src="images/back1.gif" width="100" height="32" border="0">回首頁 </a>
	</td></tr>
</table>

<h3>資料修改:</h3>

<%-- 錯誤表列 --%>
<c:if test="${not empty errorMsgs}">
	<font style="color:red">請修正以下錯誤:</font>
	<ul>
		<c:forEach var="message" items="${errorMsgs}">
			<li style="color:red">${message}</li>
		</c:forEach>
	</ul>
</c:if>

<FORM METHOD="post" ACTION="${pageContext.request.contextPath}/coupon/coupon.do" name="form1">
<table>
	<tr>
		<td>店家:<font color=red><b>*</b></font></td>
		<td><%=couponVO.getStorId()%></td>
		<td><input type="hidden" name="storId" value="<%=couponVO.getStorId()%>"/></td> 
	</tr>
	
	<tr>
		<td>優惠券編號:<font color=red><b>*</b></font></td>
		<td><%=couponVO.getCouId()%></td>
	</tr>
	<tr>
		<td>優惠券類型:</td>
		<td><input type="TEXT" name="couType" value="<%=couponVO.getCouType()%>" size="45"/></td>
	</tr>
	<tr>
		<td>優惠券說明:</td>
		<td><input type="TEXT" name="couDes"   value="<%=couponVO.getCouDes()%>" size="45"/></td>
	</tr>
	<tr>
		<td>最低消費金額限制:</td>
		<td><input type="TEXT" name="couMinOrd" value="<%=couponVO.getCouMinOrd()%>" size="45"/></td> 
	</tr>
	<tr>
		<td>使用期限:</td>
		
		<td><input type="TEXT" name="couDate" value="<%=couponVO.getCouDate()%>" size="45" id="cou_date"/></td>
	</tr>


<%-- 	<jsp:useBean id="storeSvc" scope="page" class="com.foodtimetest.store.model.StoreService" /> --%>
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
<input type="hidden" name="action" value="update">
<input type="hidden" name="couId" value="<%=couponVO.getCouId()%>">
<input type="submit" value="送出修改"></FORM>
</body>



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
</html>