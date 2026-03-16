package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/Logout")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// セッションを取得（存在しない場合場合はnull)
		//ログイン中かどうかの確認のため、セッションがあれば取得
		HttpSession session = request.getSession(false);
		if(session != null) {
			//セッションを破棄（ログイン情報を消して「未ログイン状態」に戻す）
			session.invalidate();
		}
		//ログイン画面にリダイレクト
		response.sendRedirect(request.getContextPath() + "/Login");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 処理なし
		doGet(request, response);
	}

}
