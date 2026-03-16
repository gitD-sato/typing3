package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.TypingResult;

@WebServlet("/Result")
public class ResultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//TypingServletのdoPostの処理で保存された判定結果をセッションから取得。
		HttpSession session = request.getSession();
		TypingResult result = (TypingResult) session.getAttribute("result");
		//課題文をセッションから取得
		String exerciseText = (String) session.getAttribute("exerciseText");
		//結果の時間計算
		Long startTime = (Long) session.getAttribute("startTime");
		if ( startTime != null && result != null) { //開始と結果両方揃ったら処理を実行
			long endTime = System.currentTimeMillis(); //結果表示時が終了時間
			long elapsed = endTime - startTime; //ミリ秒
			long elapsedSeconds = elapsed / 1000; //秒に変換(整数)
			//結果に格納（秒単位）
			result.setElapsedTime(elapsedSeconds);
			//タイピング速度を分にして、入力文字数÷経過分数で1分あたりの文字数を計算
			if (elapsedSeconds > 0) {
				double minutes = elapsedSeconds / 60.0;
				result.setSpeedCpm(result.getInputLength() / minutes);
			}
			session.removeAttribute("startTime");//計算後は開始時刻は削除
		}
		//結果が残り続けないように、一度セッションから取り出したら削除。
		session.removeAttribute("result");
		session.removeAttribute("exerciseText");
		//JSPに渡すためにリクエストスコープへ格納。
		request.setAttribute("result",result);
		request.setAttribute("exerciseText", exerciseText);
		//結果画面にフォワード。
		RequestDispatcher dispatcher = 
		request.getRequestDispatcher("/WEB-INF/jsp/result.jsp");
		dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 処理なし。
		doGet(request, response);
	}
}
