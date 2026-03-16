package model;

public class AdminModel {
	private int adminId; //ADMINS主キー
	private String username; //管理者ネーム
	private String passwordHash; //管理者パスワード
	private String role;//スーパー管理人orノーマル管理人
	
	public AdminModel() { //空のコンストラクタ
	}
	
	public int getAdminId() { //idのゲッターとセッター
		return this.adminId;
	}
	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}
	
	public String getUsername() { //管理者ネームのセッターとゲッター
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPasswordHash() { //パスワードのゲッターとセッター
		return this.passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	public String getRole() {
		return this.role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	

}
