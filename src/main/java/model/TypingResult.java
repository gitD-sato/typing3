package model;

public class TypingResult {
	private String userInput;//ユーザー入力テキスト
	private int inputLength;//入力文字数
	private int correctLength;//正解文字数
	private double accuracy;//正解率
	private int targetLength;//課題文の文字数
	private int mistakeCount; //間違い文字数
	private String highlightedResult;//間違いを強調する文字列
	private double elapsedTime;//経過時間(ms)
	private double speedCpm; //入力速度(CPM)
	
	public TypingResult() { //引数なし空コンストラクタ。javaBeansのルールとして。
	}
	
	//ユーザー入力テキストのゲッターとセッター
	public String getUserInput() {
		return this.userInput;
	}
	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}
	
	//入力文字数のゲッターとセッター
	public int getInputLength() {
		return this.inputLength;
	}
	public void setInputLength(int inputLength) {
		this.inputLength = inputLength;
	}
	
	//正解文字数のゲッターとセッター
	public int getCorrectLength() {
		return this.correctLength;
	}
	public void setCorrectLength(int correctLength) {
		this.correctLength = correctLength;
	}
	
	//正解率のゲッターとセッター
	public double getAccuracy() {
		return this.accuracy;
	}
	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
	
	//課題文文字数のゲッターとセッター
	public int getTargetLength() {
		return targetLength;
	}
	public void setTargetLength(int targetLength) {
		this.targetLength = targetLength;
	}
	
	//間違い文字数カウントのゲッターとセッター
	public int getMistakeCount() {
		return mistakeCount;
	}
	public void setMistakeCount(int mistakeCount) {
		this.mistakeCount = mistakeCount;
	}
	
	//間違い文字強調の文字列のゲッターとセッター
	public String getHighlightedResult() {
		return highlightedResult;
	}
	public void setHighlightedResult(String highlightedResult) {
		this.highlightedResult = highlightedResult;
	}
	
	//経過時間のゲッターとセッター
	public double getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(double elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	
	//入力速度のゲッターとセッター
	public double getSpeedCpm() {
		return speedCpm;
	}
	public void setSpeedCpm(double speedCpm) {
		this.speedCpm = speedCpm;
	}
	
	//追加：経過時間を「分秒」に変換して返す
	public String getElapsedTimeFormatted() {
		long totalSeconds = (long) elapsedTime;
		long minutes = totalSeconds / 60;
		long seconds = totalSeconds % 60;
		return String.format("%d分%d秒", minutes, seconds);
	}

}
