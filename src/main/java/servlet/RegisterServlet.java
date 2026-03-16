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
import utilSecurity.SecurityUtil;


@WebServlet("/Register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	//doGetで登録画面の表示をする
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 登録画面へフォワード
	    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/register.jsp");
	    dispatcher.forward(request, response);
	}

	//doPostで登録の処理を行う
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//管理者ログインチェック、スーパー管理人以外は弾く
		HttpSession session = request.getSession(false);
		if (session == null || !"super".equals(session.getAttribute("role"))) {
			response.sendRedirect(request.getContextPath() + "/Login");
			return;
		}
		//言語を日本語に設定
		request.setCharacterEncoding("UTF-8");
		//名前とパスワード取得
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		// 入力のチェック、空文字ならエラーメッセージと登録画面にフォワード
		if (username == null || username.isEmpty()) {
			request.setAttribute("errorMsg", "ユーザー名を入力してください");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/register.jsp");
			dispatcher.forward(request, response);
			return;
		}
		//パスワード妥当性チェック
		if (!SecurityUtil.isValidPassword(password)) {
			request.setAttribute("errorMsg", "パスワードは8～20文字の英数字で入力してください");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/register.jsp");
			dispatcher.forward(request, response);
			return;
		}
		//AdminModelにセット
		AdminModel admin = new AdminModel();
		admin.setUsername(username);
		admin.setPasswordHash(password);//DAO側でハッシュ化する設計
		AdminLogic logic = new AdminLogic();
		boolean result = logic.create(admin);
		if(result) {
			//登録成功→ログイン画面へ
			response.sendRedirect(request.getContextPath() + "/Login");
			return;
		} else {
			//登録失敗→エラーメッセージを表示
			request.setAttribute("errorMsg", "登録に失敗しました。既に存在するユーザー名かもしれません。");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/register.jsp");
			dispatcher.forward(request, response);
		}
	}
}
