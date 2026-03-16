package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logic.AdminLogic;
import logic.TaskLogic;
import model.AdminModel;
import model.Task;
import utilSecurity.SecurityUtil;

@WebServlet("/Admin")

public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 共通：一覧データをセット
	 * 
	 * @param request タスク一覧と管理者一覧を格納するためのHTTPリクエスト
	 */
	private void loadLists(HttpServletRequest request) {
		TaskLogic taskLogic = new TaskLogic();
		List<Task> taskList = taskLogic.getTaskList();
		request.setAttribute("taskList",taskList);
		AdminLogic adminLogic = new AdminLogic();
		List<AdminModel> adminList = adminLogic.findAll();
		request.setAttribute("adminList", adminList);
	}
      
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * ログインチェック
		 * セッションがない、またはloginしていない場合はログイン画面へ
		 */
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("login") == null) {
			response.sendRedirect(request.getContextPath() + "/Login");
			return;
		}
		//エラーメッセージ（一回だけ表示）
		Object msg = session.getAttribute("errorMsg");
		if (msg != null) {
			request.setAttribute("errorMsg", msg);
			session.removeAttribute("errorMsg");
		}
		//成功メッセージ
		Object success = session.getAttribute("successMsg");
		if (success != null) {
			request.setAttribute("successMsg", success);
			session.removeAttribute("successMsg");
		}
		//一覧データ。JSPで${}として使える
		loadLists(request);
		//管理者画面にフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/admin.jsp");
		dispatcher.forward(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//管理者ログインチェック
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.sendRedirect(request.getContextPath() + "/Login");
			return;
		}
		//権限チェック
		String role = (String) session.getAttribute("role");
		//actionを先に取得
		String action = request.getParameter("action");
		//管理者操作はスーパー管理人のみ許可
		if(action.equals("deleteAdmin") || action.equals("updateAdmin")) {
			if (!"admin".equals(role) && !"super".equals(role)) {
				response.sendRedirect(request.getContextPath() + "/Login");
				return;
			}
		}
		//文字コード日本語に設定
		request.setCharacterEncoding("UTF-8");
		//タスクの作成、更新、削除終わりで/Adminに戻る
		String redirect = request.getContextPath() + "/Admin";
		//フォームのactionで処理を分岐する。
		switch (action) {
		case "create":
			createTask(request);
			break;
		case "delete":
			deleteTask(request);
			break;
		case "update":
			updateTask(request);
			break;
		case "deleteAdmin":
			deleteAdmin(request, session, response);
			return;//deleatAdmin()内でリダイレクト済みのため、二重リダイレクトしないようにリターン
		case "updateAdmin":
			if (!updateAdmin(request,response, session)) {
				return; //エラー時はforward 済み
			}
			redirect += "#admin-section"; //成功時この位置にリダイレクト
			break;
		}
		response.sendRedirect(redirect); //正常処理後は管理者画面に戻る
	}
	//課題文のCRUD
	
	/**
	 * "text"を取得しタスク作成処理をする。
	 * 
	 * @param request クライアントから送信されたHTTPリクエスト
	 */
	private void createTask(HttpServletRequest request) {
		String text = request.getParameter("text");
		if(text != null && !text.isEmpty()) { //null,空文字でないならタスクを作成
		   Task task = new Task();
		   task.setText(text);
		   TaskLogic logic = new TaskLogic();
		   logic.create(task);//DAOを通してDBにINSERT
		}
	}
	
	/**
	 * 削除対象のIDを取得し削除処理を行う
	 * 
	 * @param request 削除対象ID("id")を含むHTTPリクエスト
	 */
	private void deleteTask(HttpServletRequest request) {
		try {
			int id = Integer.parseInt(request.getParameter("id"));//idを取得し、intに変換
			TaskLogic logic = new TaskLogic();
			logic.delete(id); //daoを通してdeleteが実行
		} catch (NumberFormatException e) {
			// 不正なIDは無視
		}
	}
	
	/**
	 * リクエストから取得したIDとテキストを更新する処理
	 * 
	 * @param request 更新対象のidとtextをを含むHTTPリクエスト
	 */
	private void updateTask(HttpServletRequest request) {
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			String text = request.getParameter("text");//フォームからテキストを受け取る
			if (text != null && !text.isEmpty()) {
				Task task = new Task();//タスクを作成してidとテキストをセット
				task.setId(id);
				task.setText(text);
				TaskLogic logic = new TaskLogic();//daoを通してupdateを実行
				logic.update(task);
			}
		} catch (NumberFormatException e) {
			//不正なIDは無視
		}
	}
	
	//管理者CRUD(権限チェック付き)
	
	/**
	 * 管理者削除処理を行う。
	 * 権限チェックを行い、許可されていれば指定ユーザーを削除する。
	 * 
	 * @param request 削除対象のIDを含むHTTPリクエスト
	 * @param session 権限とログイン中のユーザー名を取得するためのセッション
	 * @param response リダイレクトを行うためのレスポンス
	 * @throws IOException リダイレクト失敗時の例外発生
	 */
	private void deleteAdmin(HttpServletRequest request,HttpSession session, HttpServletResponse response) throws IOException {
		String role = (String) session.getAttribute("role");
		String loginUser = (String) session.getAttribute("username");
		String username = request.getParameter("username");//削除ボタンを押した管理者のusernameを取得
		//super以外は他人を消せない
		if (!"super".equals(role) && !username.equals(loginUser)) {
			session.setAttribute("errorMsg", "他の管理者を削除する権限がありません");
			response.sendRedirect(request.getContextPath() + "/Admin#admin-section");
			return ;
		}
		//削除処理
		AdminLogic logic = new AdminLogic();
		logic.delete(username);
		//処理後は元の位置へ
		response.sendRedirect(request.getContextPath() + "/Admin#admin-section");
		return;
	}
	
	/**
	 * 管理者のパスワード更新処理。
	 * 権限チェックとバリデーションを通過した場合のみ更新。
	 * 
	 * @param request ユーザー名と新パスワードを取得するHTTPリクエスト
	 * @param response 処理結果に応じてリダイレクトを行うためのHTTPレスポンス
	 * @param session ログイン中ユーザーのroleやユーザー名を取得
	 * @return 更新が成功した場合はtrue、エラー時はfalse
	 * @throws ServletException サーブレット処理中の例外
	 * @throws IOException      リダイレクト処理に失敗した場合に発生
	 */
	private boolean updateAdmin(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String role = (String) session.getAttribute("role");
		//今ログインしている人の権限とusernameを取得
		String loginUser = (String) session.getAttribute("username");
		//super以外の人はパスワード変更不可
		if(!"super".equals(role) && !username.equals(loginUser)) {
			session.setAttribute("errorMsg", "他の管理者のパスワードは変更できません");
			response.sendRedirect(request.getContextPath() + "/Admin#admin-section");
			return false;
		}
	    //バリデーション
		if (!SecurityUtil.isValidPassword(password)) {
			session.setAttribute("errorMsg", "パスワードは8～20文字の英数字で入力してください");
			response.sendRedirect(request.getContextPath() + "/Admin#admin-section");
		    return false;
		}
		//正常処理ロジックを通してDAO→DBへUPDATE
		AdminModel admin = new AdminModel();
		admin.setUsername(username);
		admin.setPasswordHash(password);
		AdminLogic logic = new AdminLogic();
		logic.update(admin);
		//成功メッセージをセット
		session.setAttribute("successMsg", "パスワードを変更しました");
		return true;			
	}
}
