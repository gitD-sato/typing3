package setting;

public class SecuritySettings {
	/** パスワードの妥当性チェックのための正規表現文字列 */
	//- 英数字のみで構成され、長さが8〜20文字の文字列
	public static final String PASSWORD_REGEXP_STRING = "^[0-9a-zA-Z]{8,20}$";

}
