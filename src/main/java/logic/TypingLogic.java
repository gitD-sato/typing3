package logic;

import model.TypingResult;

public class TypingLogic {
	/**
	 * ユーザー入力を評価して結果を返すメソッド
	 * @param userInput ユーザーが入力した文字列
	 * @return TypingResult 入力文字数、課題文の文字数、正解文字数、正解率を持つオブジェクト
	 */
	public static TypingResult evaluate(String userInput, String target) {
		//新しいTypingResultを作成し、ユーザー入力をセットする。
		TypingResult result = new TypingResult();
		result.setUserInput(userInput);
		//入力文字数の計算
		int inputLength;
		//ユーザー入力が空でなければ文字数を取得し、nullなら０とする。
		if(userInput != null) {
			inputLength = userInput.length();
		} else {
			inputLength = 0;
		}
		//課題文の文字数
		int targetLength;
		//課題文が空でなければ文字数を取得し、nullなら０とする。
		if(target != null) {
			targetLength = target.length();
		} else {
			targetLength = 0;
		}
		//正解文字数を計算（文字ごとに比較）
		int correctLength;
		//ユーザー入力かつ課題文がnullでないなら、文字ごとに比較し一致数を数える。それ以外は０とする。
		if(userInput != null && target != null) {
			correctLength = countCorrectChars(userInput, target);
		} else {
			correctLength = 0;
		}
		// 正解率の計算
		double accuracy;
		// 課題文がない場合は正解率を0%にする
		if(targetLength == 0 ) {
			accuracy = 0.0; 
		} else {
			// 課題文がある場合は「正解文字数 ÷ 課題文の文字数 × 100」で正解率を計算
			accuracy = (double) correctLength / targetLength * 100;
			// 小数点第1位で四捨五入する
			accuracy = Math.round(accuracy * 10) / 10.0;
		}
		//結果をモデルにセット
		result.setInputLength(inputLength);
		result.setTargetLength(targetLength);
		result.setCorrectLength(correctLength);
		result.setAccuracy(accuracy);
		//間違い文字数の計算とセット
		result.setMistakeCount(targetLength - correctLength);
		//間違い文字列を生成
		result.setHighlightedResult(highlightMistakes(userInput, target));
		return result;
	}
	
	/**
	 * 入力文字列と課題文を先頭から比較し、同じ位置で一致した文字数を数える
	 * 入力が課題文より短い場合は、存在しない部分は不一致として扱う
	 * @param input ユーザー入力
	 * @param target 正解テキスト
	 * @return 一致した文字数
	 */
	private static int countCorrectChars(String input, String target) {
		//カウント初期値０、入力文字数、課題文文字数取得
		int count = 0;
		int inputLen = input.length();
		int targetLen = target.length();
		//課題文の長さ分（例えば10文字なら10文字分）ループ処理。
		for (int i = 0; i < targetLen; i++) {
			//入力が課題文より短い場合は不正解。同じ位置の文字が一致すれば正解としてcountと増加。
			if (  i < inputLen && input.charAt(i) == target.charAt(i)) {	
				count++ ;
			}
			// i >=inputLenの場合は自動的に不正解（countは増えない）
		}
		return count;
	}
	
	//正解文字と間違い文字
	private static String highlightMistakes(String input, String target) {
		//入力と正解文字どちらかがNullなら空文字（何も表示しない）を返す。NullPointerExceptionを避ける。
		if (input == null || target == null) return "";
		//文字をたくさん追加していくのでStringBuilderを使用。
		StringBuilder sb = new StringBuilder();
		int inputLen = input.length();//入力文字数
		int targetLen = target.length();//正解文字数
		int maxLen = Math.max(inputLen, targetLen);//どちらか長いほうを求める
		//最大文字数までループ処理
		for(int i = 0; i < maxLen; i++) {
			//その位置に入力文字があるなら、その文字を取り出し、ない（入力が短い）なら空白を入れる
			char inputChar = i < inputLen ? input.charAt(i) : ' ';
			//その位置に正解文字があるなら、その文字を取り出しない（正解が短い）なら空白を入れる
			char targetChar = i < targetLen ? target.charAt(i) : ' ';
			//入力がある位置なら正解ならそのまま、間違いなら<span class='mistake'>をつけ、
			//入力自体がない位置は、空白をまちがいとして扱う。
			if (i < inputLen) { //入力がある位置
				if (i < targetLen && inputChar == targetChar) {
					//正解の文字列にもこの位置に文字があるか？入力と正解の文字が同じか？
					sb.append(inputChar);//trueでそのまま表示
				} else {//それ以外は間違いとしてマークする。クラスに色を付ければ赤文字表示が可能。
					sb.append("<span class='mistake'>").append(inputChar).append("</span>");
				}
			} else {//そもそもこの位置に入力された文字が存在しない場合
				sb.append("<span class='mistake'> </span>");//空白を間違いとしてマーク
			}
		}
		return sb.toString();//完成した文章をStringに変換（toString()を使う）
	}
}
