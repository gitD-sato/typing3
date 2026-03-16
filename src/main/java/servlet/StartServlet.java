package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/Start")
public class StartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 開始画面表示する。
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/start.jsp");
		dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//開始時刻をセッションに保存。（System～はJavaが持ってる機能）
		request.getSession().setAttribute("startTime", System.currentTimeMillis());
		//"/Typing"に課題文がセットされているので演習画面にリダイレクトする。
		//フォワードだとこのサーブレットにテキストがセットしてないので演習テキストが表示しなくなる。
		response.sendRedirect(request.getContextPath() + "/Typing");
	}
}
