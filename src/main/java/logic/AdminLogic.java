package logic;

import java.util.List;

import dao.AdminDAO;
import model.AdminModel;
import utilSecurity.SecurityUtil;

public class AdminLogic {
	private AdminDAO dao;
	
	//コンストラクタ
	public AdminLogic() {
		dao = new AdminDAO();
	}
	
	/**
	 * 管理者認証。ユーザー名から管理者情報を取得し、ハッシュ化されたパスワードと比較する
	 * 
	 * @param username ユーザー名
	 * @param password 入力されたパスワード
	 * @return 成功時→AdminModel　失敗時→null
	 */
    public AdminModel authenticate(String username, String password) {
	    AdminModel admin = dao.findByUsername(username);
	    if(admin != null && SecurityUtil.checkPassword (password, admin.getPasswordHash())) {
		     return admin;
	     }
	     return null;
    }
    
    /**
     * 管理者登録 
     * 
     * @param admin 管理者アカウント情報
     * @return 登録成功→true 失敗→false
     */
    public boolean create(AdminModel admin) {
    	return dao.create(admin);
    }
    
    /**
     * 管理者情報更新
     * 
     * @param admin 管理者アカウント情報
     * @return 更新成功→true 失敗→false
     */
    public boolean update(AdminModel admin) {	
    	return dao.update(admin);
    }
    
    /**
     * 管理者削除
     * 
     * @param username 対象の管理者の名前
     * @return 削除成功→true 失敗→false
     */
    public boolean delete(String username) {
    	return dao.delete(username);
    }
    
    /**
     * 管理者リスト取得
     * 
     * @return　管理者情報のリスト
     */
    public List<AdminModel> findAll() {
    	return dao.findAll();
    }
}
