package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logic.AdminLogic;
import model.AdminModel;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ログイン画面表示
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
		dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 言語を日本語に設定
		request.setCharacterEncoding("UTF-8");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		//AdminLogicより管理者認証を行った管理者
		AdminLogic logic = new AdminLogic();
		AdminModel admin = logic.authenticate(username, password);
		
		if (admin != null) {
			HttpSession session = request.getSession();
			//ログイン状態を保持
			session.setAttribute("login", true);
			//認証成功したユーザー名を保持
			session.setAttribute("username", username);
			//管理者権限roleをセット
			session.setAttribute("role", admin.getRole());//super or normal
			//管理者画面へリダイレクト
			response.sendRedirect(request.getContextPath() + "/Admin");
		} else {
			//認証失敗時のエラーメッセージ
			request.setAttribute("errorMsg", "IDまたはパスワードが違います");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
			dispatcher.forward(request, response);
		}
	}
}
