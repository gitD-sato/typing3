package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.AdminModel;
import utilSecurity.SecurityUtil;

public class AdminDAO {
	//H2データベースの接続先のURLとユーザー名とパスワード
		private final String JDBC_URL ="jdbc:h2:~/typing2";		
		private final String DB_USER ="sa";
		private final String DB_PASS ="";
		
	     /**
	      * ユーザー名とパスワードで管理者の認証処理。
	      * データベースから取得したパスワードハッシュと照合し、認証結果を返す。
	      * 
	      * @param username 認証対象の管理者名
	      * @param password 入力されたパスワード
	      * @return 認証成功時→true 失敗→false
	      */
		public boolean authenticate(String username, String password) {
			String sql = "SELECT password_hash FROM admins WHERE username = ?";
			try {
				Class.forName("org.h2.Driver");
				try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
					 PreparedStatement stmt = conn.prepareStatement(sql)){
					stmt.setString(1, username);
					ResultSet rs = stmt.executeQuery();
					//ユーザー名のデータが存在するかをチェック
					if (rs.next()) {
						String storeHash = rs.getString("password_hash");
						//入力されたpasswordをハッシュ化してDBのハッシュと比較
						return SecurityUtil.checkPassword(password, storeHash);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		
		/**
		 *  管理者新規登録
		 *  パスワードはハッシュ化して保存し、登録結果を返す。
		 *  
		 * @param admin 新規作成する管理者アカウント情報
		 * @return 登録成功時→true 失敗時→false
		 */
		public boolean create(AdminModel admin) {
			String sql = "INSERT INTO admins(username, password_hash, role) VALUES(?,?,?)";
			try { 
				Class.forName("org.h2.Driver");
				try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
					PreparedStatement stmt = conn.prepareStatement(sql)) {
					stmt.setString(1, admin.getUsername());
					stmt.setString(2, SecurityUtil.hashPassword(admin.getPasswordHash()));
					stmt.setString(3, "normal"); //追加ユーザーはnormal（権限処理のため）
					int result = stmt.executeUpdate();
					return result == 1; //1件処理したらtrue
				}
			} catch (Exception e) {
				 e.printStackTrace(); 
				return false;
			}
		}
		
		/**
		 * 管理者の一覧取得
		 * 
		 * @return 管理者情報（id,username）のリスト
		 */
		public List<AdminModel> findAll() {
			List<AdminModel> list = new ArrayList<>();
			String sql = "SELECT id, username FROM admins ORDER BY id";
			try {
				Class.forName("org.h2.Driver");
				try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
						PreparedStatement stmt = conn.prepareStatement(sql);
						ResultSet rs = stmt.executeQuery()) {
					//取得結果の各行に対し繰り返し処理。id,usernameをセットしてリストに追加
					while (rs.next()) {
						AdminModel admin = new AdminModel();
						admin.setAdminId(rs.getInt("id"));
						admin.setUsername(rs.getString("username"));
						list.add(admin);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}
		
		/**
		 * 管理者の削除
		 * 
		 * @param username 対象の管理者名
		 * @return 1件削除成功→true 失敗→false
		 */
		public boolean delete(String username) {
			String sql = "DELETE FROM admins WHERE username = ?";
			try {
				Class.forName("org.h2.Driver");
				try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
						PreparedStatement stmt = conn.prepareStatement(sql)){
					stmt.setString(1, username);
					int result = stmt.executeUpdate();
					return result == 1; //１件処理したらtrue
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		/**
		 * パスワードの更新
		 * 
		 * @param admin パスワード更新対象の管理者情報
		 * @return 更新成功→true 失敗→false
		 */
		public boolean update(AdminModel admin) {
			String sql = "UPDATE admins SET password_hash=? WHERE username=?";
			try {
				Class.forName("org.h2.Driver");
				try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
					PreparedStatement stmt = conn.prepareStatement(sql)) {
					//ハッシュ化されたパスワードと名前をセット
					stmt.setString(1, SecurityUtil.hashPassword(admin.getPasswordHash()));
					stmt.setString(2, admin.getUsername());
					int result = stmt.executeUpdate();
					return result == 1; //1件処理したらtrue
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		
		/**
		 * 指定されたユーザー名から一致する管理者情報を取得。
		 * 
		 * @param username 検索対象の管理者名
		 * @return DBからの管理者情報が一致すればadminModel。存在がなければnull。
		 */
		public AdminModel findByUsername(String username) {
		    String sql = "SELECT id, username, password_hash, role FROM admins WHERE username = ?";
		    try {
		        Class.forName("org.h2.Driver");
		        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
		             PreparedStatement stmt = conn.prepareStatement(sql)) {
		            stmt.setString(1, username);
		            ResultSet rs = stmt.executeQuery();
		            if (rs.next()) { //入力した名前からid,username,password_hash,roleがあるなら、、
		                AdminModel admin = new AdminModel();
		                admin.setAdminId(rs.getInt("id"));
		                admin.setUsername(rs.getString("username"));
		                admin.setPasswordHash(rs.getString("password_hash"));
		                admin.setRole(rs.getString("role"));  // ✅ super / normal
		                return admin; 
		            }
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    return null;
		}
}
