package utilSecurity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import setting.SecuritySettings;

/**
 * セキュリティ関連ユーティリティクラス
 * - パスワードハッシュ化（SHA-256）
 */
public class SecurityUtil {
	
/**
 * パスワードをSHA-256でハッシュ化します。
 * @param password 平文パスワード
 * @return ハッシュ化された文字列（16進数）
 */
 public static String hashPassword(String password) {
	 try {
		 //messegeDigestはハッシュを計算するクラス。
		 //SHA-256ハッシュアルゴリズムを使うインスタンスを取得
		 MessageDigest md = MessageDigest.getInstance("SHA-256");
		 //パスワードをバイト配列に入れてSHA-256で変換。
		 //結果は32バイトのハッシュ値として返る。
		 byte[] hash = md.digest(password.getBytes());
		 //上の値を１６進数に変換。（数値とアルファベットの文字列）
		 StringBuilder sb = new StringBuilder();
		 for (byte b : hash) {
			 sb.append(String.format("%02x",b));
		 }
		 return sb.toString(); //ハッシュ化されたパスワードを返す
	 } catch (NoSuchAlgorithmException e) {
		 e.printStackTrace();
		 return null;
	 }
 }
 /**
  * 入力されたパスワードとDB保存済みハッシュを比較します。
  * @param plain 入力された平文パスワード
  * @param hashed DBに保存されているハッシュ値
  * @return 一致すればtrue
  */
 public static boolean checkPassword(String plain, String hashed) {
	 String hashedInput = hashPassword(plain); //上のhashPassword()呼びだし結果を代入
	 return hashedInput != null && hashedInput.equals(hashed);
 }
 
 /**
  * パスワードの妥当性チェック（正規表現）
  * @param password 入力されたパスワード
  * @return 妥当ならtrue
  */
  public static boolean isValidPassword(String password) {
	  return password != null && password.matches(SecuritySettings.PASSWORD_REGEXP_STRING);
  }
}
