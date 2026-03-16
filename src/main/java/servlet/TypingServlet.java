package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.TaskDAO;
import logic.TypingLogic;
import model.Task;
import model.TypingResult;

@WebServlet("/Typing")
public class TypingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//TaskDAOの呼び出し。データアクセス用のDAOを生成。
		TaskDAO dao = new TaskDAO();
		//ランダムに１件取得
		Task task = dao.findRandom();
		//JSPに渡す
		request.setAttribute("exerciseText", task.getText());
		//セッションにも保存(doPostで取り出せるようにする）
		HttpSession session = request.getSession();
		session.setAttribute("exerciseText", task.getText());
		//演習画面にフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/exercise.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//言語を日本語に設定
		request.setCharacterEncoding("UTF-8");
		//ユーザーが入力した値を取得。文字列がuserInputに格納される。
		String userInput = request.getParameter("userInput");
		if (userInput == null) { //nullなら空文字	        userInput = "";
	    } else {
	        userInput = userInput
	            .replace("\u200B", "")//ゼロ幅スペースという空白文字を削除
	            .replace("\r\n", "\n")//Windows の改行（CRLF）を LF（\n）に統一
	            .replace("\r", "\n")//古い Mac の改行（CR）も LF に統一
	            .trim();//文字列の前後の空白（スペース、タブ、改行）を削除
	    }
		//セッションに保存されている課題文を取得。
		String target = (String) request.getSession().getAttribute("exerciseText");
		//入力結果処理。TypingLogicクラスのevaluate()を呼び出して課題文とユーザー入力文を比較する。
		TypingResult result = TypingLogic.evaluate(userInput, target);
		//結果をセッションに保存する。
		HttpSession session = request.getSession();
		session.setAttribute("result",result);
		//セッションで保存した結果を表示するため結果ページへリダイレクト。
		response.sendRedirect(request.getContextPath() + "/Result");
	}
}
