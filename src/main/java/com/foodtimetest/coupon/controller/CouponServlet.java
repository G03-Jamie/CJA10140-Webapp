package com.foodtimetest.coupon.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;

import com.foodtimetest.coupon.model.*;



@WebServlet(name = "Couponservlet", urlPatterns = { "/coupon/coupon.do" })
public class CouponServlet extends HttpServlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		

		    // 沒有 action，代表是初次載入 select_page.jsp → 載入不重複店家編號清單
		    if (action == null) {
		        CouponService couponSvc = new CouponService();
		        List<CouponVO> distinctStorList = couponSvc.getDistinctStorId(); // 取得不重複商家id
		        req.setAttribute("distinctStorList", distinctStorList);
		        
		        RequestDispatcher dispatcher = req.getRequestDispatcher("/coupon/select_page.jsp");
		        dispatcher.forward(req, res);
		        return; // 轉交
		    }

		// ****************優惠券查詢 ****************//

		if ("getOne_For_Display".equals(action)) {// 來自select_page.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			String str = req.getParameter("couId");
			if (str == null || (str.trim()).length() == 0) {
				errorMsgs.add("請輸入優惠券編號");
			}
			// Send the use back to the form, if there were errors
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher failureView = req.getRequestDispatcher("/coupon/select_page.jsp");
				failureView.forward(req, res);
				return;// 有errorMsgs就跳頁，不處理後續流程，程式中斷

			}

			Integer couId = null;
			try {
				couId = Integer.valueOf(str);
			} catch (Exception e) {
				errorMsgs.add("優惠券編號格式不正確");
			}
			// Send the use back to the form, if there were errors
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher failureView = req.getRequestDispatcher("/coupon/select_page.jsp");
				failureView.forward(req, res);
				return;// 有errorMsgs，程式中斷
			}

			/*************************** 2.開始查詢資料 *****************************************/
			CouponService couponSvc = new CouponService();
			CouponVO couponVO = couponSvc.getOneCoupon(couId);
			if (couponVO == null) {
				errorMsgs.add("查無資料");
			}
			// 如果有錯誤，將使用者導回原本的輸入表單頁面
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher failureView = req.getRequestDispatcher("/coupon/select_page.jsp");
				failureView.forward(req, res);
				return;// 程式中斷
			}

			/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
			req.setAttribute("couponVO", couponVO);
			String url = "/coupon/listOneCoupon.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 listOneCoupon.jsp
			successView.forward(req, res);
		}

		// ****************優惠券修改- 查資料，顯示修改表單 ListAll
		// ********************************************//
		if ("getOne_For_Update".equals(action)) {// 來自listAllEmp.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 ****************************************/
			Integer couId = Integer.valueOf(req.getParameter("couId"));

			/*************************** 2.開始查詢資料 ****************************************/
			CouponService couSvc = new CouponService();
			CouponVO couponVO = couSvc.getOneCoupon(couId);

			/*************************** 3.查詢完成,準備轉交(Send the Success view) ************/
			req.setAttribute("couponVO", couponVO); // 資料庫取出的couponVO物件,存入req
			String url = "/coupon/update_coupon_input.jsp"; //
			RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交 update_coupon_input.jsp
			successView.forward(req, res);
		}

		// ****************優惠券修改- 接收修改後的資料並更新 DB
		

		if ("update".equals(action)) {// 來自update_emp_input.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			/*************************** 1.接收請求參數 ****************************************/

			 
			Integer couId = Integer.valueOf(req.getParameter("couId"));

			Integer storId = Integer.valueOf(req.getParameter("storId"));

			String couType = req.getParameter("couType"); // 優惠券類型
			String couTypeReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";// 以下練習正則(規)表示式(regular-expression)
			if (couType == null || couType.trim().length() == 0) {
				errorMsgs.add("優惠券類型: 請勿空白");
			} else if (!couType.trim().matches(couTypeReg)) {
				errorMsgs.add("優惠券類型: 只能是中、英文字母、數字和_，, 且長度必需在2到10之間");
			}

			String couDes = req.getParameter("couDes"); // 優惠券說明
			if (couDes == null || couDes.trim().length() == 0) { // == null 防呆，避免code欄位名稱打錯
				errorMsgs.add("優惠券說明請勿空白");
			}

			Integer couMinOrd = null;
			try {
				couMinOrd = Integer.valueOf(req.getParameter("couMinOrd").trim()); // 最低消費金額限制
			} catch (NumberFormatException e) {
				couMinOrd = 0; // 輸入錯誤，新增時不會跳null，會顯示0.0
				errorMsgs.add("最低消費金額請填數字或0");
			}

			java.sql.Timestamp couDate = null;// 使用期限
			try {
				couDate = java.sql.Timestamp.valueOf(req.getParameter("couDate").trim());
				java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
				if (couDate.before(now)) {
					errorMsgs.add("使用期限不能早於現在時間！");
				}

			} catch (IllegalArgumentException e) {
				couDate = new java.sql.Timestamp(System.currentTimeMillis());
				errorMsgs.add("請輸入日期、時間!");
			}

			// 把所有使用者填的資料一個一個塞進 couponVO 裡

			CouponVO couponVO = new CouponVO();
			couponVO.setCouId(couId);
			couponVO.setStorId(storId);
			couponVO.setCouType(couType);
			couponVO.setCouDes(couDes);
			couponVO.setCouMinOrd(couMinOrd);
			couponVO.setCouDate(couDate);

			// Send the use back to the form, if there were errors
			if (!errorMsgs.isEmpty()) {
				req.setAttribute("couponVO", couponVO);// 含有輸入格式錯誤的couponVO物件,也存入req
				RequestDispatcher failureView = req.getRequestDispatcher("/coupon/update_coupon_input.jsp");
				failureView.forward(req, res);
				return;// 程式中斷//驗證格式錯誤，導回修改表單 update_coupon_input.jsp，欄位內容還會保留（不會全清空）
			}

			/***************************
			 * 2.開始修改資料(前面的資料格式OK)
			 *****************************************/
			CouponService couSvc = new CouponService();
			couponVO = couSvc.updateCoupon(couId, storId, couDes, couType, couMinOrd, couDate);
			// 新增沒有couId，修改有couId作為where條件使用

			/*************************** 3.修改完成,準備轉交(Send the Success view) *************/
			req.setAttribute("couponVO", couponVO); // 資料庫update成功後,正確的的couponVO物件,存入req/
			System.out.println("✅ 有執行 update，couId=" + couponVO.getCouId());
			
			String url = "/coupon/listOneCoupon.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url);
			successView.forward(req, res);
		}

		// *****************優惠券 新增
		// ****************************************************//
		if ("insert".equals(action)) {

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			/*************************** 1.接收請求參數 ****************************************/
			Integer storId = Integer.valueOf(req.getParameter("storId"));

			String couType = req.getParameter("couType"); // 優惠券類型
			String couTypeReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";// 以下練習正則(規)表示式(regular-expression)
			if (couType == null || couType.trim().length() == 0) {
				errorMsgs.add("優惠券類型: 請勿空白");
			} else if (!couType.trim().matches(couTypeReg)) {
				errorMsgs.add("優惠券類型: 只能是中、英文字母、數字和_，, 且長度必需在2到10之間");
			}

			String couDes = req.getParameter("couDes"); // 優惠券說明
			if (couDes == null || couDes.trim().length() == 0) { // == null 防呆，避免code欄位名稱打錯
				errorMsgs.add("優惠券說明請勿空白");
			}

			Integer couMinOrd = null;
			try {
				couMinOrd = Integer.valueOf(req.getParameter("couMinOrd").trim()); // 最低消費金額限制
			} catch (NumberFormatException e) {
				couMinOrd = 0; // 輸入錯誤，新增時不會跳null，會顯示0.0
				errorMsgs.add("最低消費金額請填數字或0");
			}

			java.sql.Timestamp couDate = null;// 使用期限
			try {
				couDate = java.sql.Timestamp.valueOf(req.getParameter("couDate").trim());
				java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
				if (couDate.before(now)) {
					errorMsgs.add("使用期限不能早於現在時間！");
				}

			} catch (IllegalArgumentException e) {
				couDate = new java.sql.Timestamp(System.currentTimeMillis());
				errorMsgs.add("請輸入日期、時間!");
			}

			// 到這行表示格式都修正，丟回前端顯示填的資料
			// 把所有使用者填的資料一個一個塞進 couponVO 裡
			CouponVO couponVO = new CouponVO();

			couponVO.setStorId(storId);
			couponVO.setCouType(couType);
			couponVO.setCouDes(couDes);
			couponVO.setCouMinOrd(couMinOrd);
			couponVO.setCouDate(couDate);

			if (!errorMsgs.isEmpty()) {
				req.setAttribute("couponVO", couponVO);
				RequestDispatcher failureView = req.getRequestDispatcher("/coupon/addCoupon.jsp");// 導回使用者輸入的「新增頁面」顯示錯誤
				failureView.forward(req, res);
				return;
			}

			/*************************** 2.開始新增資料 ***************************************/
			CouponService couSvc = new CouponService();
			couponVO = couSvc.addCoupon(storId, couType, couDes, couMinOrd, couDate);

			/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
			String url = "/coupon/listAllCoupon.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
			successView.forward(req, res);
		}
		// *****************優惠券 刪除
				// ****************************************************//
		if ("delete".equals(action)) { // 來自listAllCoupon.jsp

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 ***************************************/
			Integer couId = Integer.valueOf(req.getParameter("couId"));

			/*************************** 2.開始刪除資料 ***************************************/
			CouponService couSvc = new CouponService();
			try{
				couSvc.deleteCoupon(couId);
				/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
				String url = "/coupon/listAllCoupon.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// 刪除成功後,轉交回送出刪除的來源網頁
				successView.forward(req, res);
			} catch (Exception e) {
			
				    res.setContentType("text/html; charset=UTF-8");
				    PrintWriter out = res.getWriter();
				    out.println("<html><body>");
				    out.println("<h3 style='color:red;'>關聯到會員持有優惠券，刪除失敗：</h3>");
				    out.println("<p>" + e.getMessage() + "</p>");
				    out.println("<a href='" + req.getContextPath() + "/coupon/listAllCoupon.jsp'>回優惠券列表</a>");
				    out.println("</body></html>");
			
			
			
			}

			
		}
		
		// ****************各別店家優惠券查詢 ****************//

				if ("getOne_Storecoupon_Display".equals(action)) {// 來自select_page.jsp的請求

					List<String> errorMsgs = new LinkedList<String>();
					// Store this set in the request scope, in case we need to
					// send the ErrorPage view.
					req.setAttribute("errorMsgs", errorMsgs);

					/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
					String str = req.getParameter("storId");
					if (str == null || (str.trim()).length() == 0) {
						errorMsgs.add("請輸入店家編號");
					}
					// Send the use back to the form, if there were errors
					if (!errorMsgs.isEmpty()) {
						RequestDispatcher failureView = req.getRequestDispatcher("/coupon/select_page.jsp");
						failureView.forward(req, res);
						return;// 有errorMsgs就跳頁，不處理後續流程，程式中斷

					}

					Integer storId = null;
					try {
						storId = Integer.valueOf(str);
					} catch (Exception e) {
						errorMsgs.add("店家編號格式不正確");
					}
					// Send the use back to the form, if there were errors
					if (!errorMsgs.isEmpty()) {
						RequestDispatcher failureView = req.getRequestDispatcher("/coupon/select_page.jsp");
						failureView.forward(req, res);
						return;// 有errorMsgs，程式中斷
					}

					/*************************** 2.開始查詢資料 *****************************************/
					CouponService couponSvc = new CouponService();
					List <CouponVO> couponList = couponSvc.getStorCoupon(storId);
					if (couponList == null  || couponList.isEmpty()) {
						errorMsgs.add("查無資料");
					}
					// 如果有錯誤，將使用者導回原本的輸入表單頁面
					if (!errorMsgs.isEmpty()) {
						RequestDispatcher failureView = req.getRequestDispatcher("/coupon/select_page.jsp");
						failureView.forward(req, res);
						return;// 程式中斷
					}

					/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
					req.setAttribute("storCoupons", couponList);
					String url = "/coupon/listAllCoupon2.jsp";
					RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 listAllOneCoupon2.jsp
					successView.forward(req, res);
				}

	}
}
