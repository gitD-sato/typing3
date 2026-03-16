package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Task;

public class TaskDAO {
	//H2データベースの接続先のURLとユーザー名とパスワード
	private final String JDBC_URL ="jdbc:h2:~/typing2";		
	private final String DB_USER ="sa";
	private final String DB_PASS ="";
	
	/**
	 * 課題文取得
	 * 
	 * @return　取得したTaskのリスト。レコードが存在しない場合は空リスト
	 */
	public List<Task> findAll(){
		//取得したタスクを格納するArrayListを作成する
		List<Task> list = new ArrayList<Task>();
		//taskテーブルの全レコードをid順に取得
		String sql = "select * from tasks order by id";
		//Ｈ２のドライバをロードする。
		try {
			Class.forName("org.h2.Driver");
			//接続と実行
			try (Connection conn = DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS);
				//SQLを実行する準備	
				PreparedStatement stmt = conn.prepareStatement(sql);
					//検索を実行し、結果をResultSetとして受け取ります。
					ResultSet rs = stmt.executeQuery()){
				    //取得結果の各行に対して繰り返し処理。
					while (rs.next()) {
						//１行分のデータを格納するTaskオブジェクトを作成
						Task model = new Task();
						//id、課題文、登録日時、更新日時をモデルに設定。
						model.setId(rs.getInt("id"));
						model.setText(rs.getString("text"));
						model.setCreateAt(rs.getTimestamp("create_at"));
						model.setUpdateAt(rs.getTimestamp("updated_at"));
						//作成したTaskを結果リストに追加。
						list.add(model);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		    return list;
		}	

	/**
	 * 課題文追加
	 * 
	 * @param model 登録するタスク情報（textを含む）
	 * @return １件のタスクが正常に登録された場合はtrue、それ以外はfalse
	 */
	public boolean create(Task model) {
		//タスクテーブルに課題文、登録日、更新日を挿入し値は？で後から設定。
		String sql = "insert into tasks(text, create_at, updated_at) values(?, ?, ?)";
		//H2ドライバをロード
		try {
			Class.forName("org.h2.Driver");
			//DBへ接続の準備。
			try (Connection conn = DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS);
				PreparedStatement stmt = conn.prepareStatement(sql)) {
				//現在時点のTimestampを生成。
				Timestamp now = new Timestamp(System.currentTimeMillis());
				//1の?に課題文を設定、2は作成時刻(now)の登録日を設定、3は更新時刻(now)の更新日を設定。
				stmt.setString(1, model.getText());
				stmt.setTimestamp(2, now);
				stmt.setTimestamp(3, now);
				//insert実行し、結果を受け取り。
				int result = stmt.executeUpdate();
				return result == 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 指定されたタスクテーブルのレコードを更新
	 * 課題文と更新日時を変更。
	 * 
	 * @param model 更新対象のタスク情報（id,textなど）
	 * @return １件のタスクが正常に更新された場合はtrue、それ以外はfalse
	 */
	public boolean update(Task model) {
		//指定したidのレコードに対して、課題文と、更新日を更新する。？に後から値を入れる。
		String sql = "update tasks set text=?, updated_at=? where id=?";
		//H2ドライバをロード
		try {
			Class.forName("org.h2.Driver");
			//接続の準備
			try(Connection conn = DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS);
					PreparedStatement stmt = conn.prepareStatement(sql)) {
				//更新日時として使うTimestampを生成する。
				Timestamp now = new Timestamp(System.currentTimeMillis());
				//1に課題文、２に更新日時、３に更新対象のIDを設定。
				stmt.setString(1, model.getText());
				stmt.setTimestamp(2, now);
				stmt.setInt(3, model.getId());
				//updateを実行し、更新できた行数を取得。
				int result = stmt.executeUpdate();
				return result == 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 指定されたタスクIDの削除
	 * 
	 * @param id 対象課題文のID
	 * @return 1件削除成功→true 失敗→false
	 */
	public boolean delete(int id) {
		//idのタスクを削除。削除するidの?は後で入れる。
		String sql = "delete from tasks where id=?";
		//H2ドライバをロード
		try {
			Class.forName("org.h2.Driver");
			//接続の準備
			try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
					PreparedStatement stmt = conn.prepareStatement(sql)) {
				//?に入る削除対象のidを設定。
				stmt.setInt(1, id);
				//DELETEを実行し、削除された行数を取得。
				int result = stmt.executeUpdate();
				return result == 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * tasksテーブルからランダムに１件取得する
	 * 
	 * @return ランダムに選ばれたタスクをモデルに返す。レコードが存在しないならnull。
	 */
	public Task findRandom() {
		//RAND()で並び順をランダム化し、limit1で先頭1件だけ取得。
		String sql = "select * from tasks order by RAND() limit 1";
		//H2ドライバをロード。
		try {
			Class.forName("org.h2.Driver");
			//接続の準備
			try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
					PreparedStatement stmt = conn.prepareStatement(sql);
					ResultSet rs = stmt.executeQuery()) {
				//rs.next()は次の行に進むメソッドで、進んで行が存在すればtrue、存在しなければfalse。
				if (rs.next()) {
					//検索結果の１行を入れるタスクを作成
					Task model = new Task();
					model.setId(rs.getInt("id"));
					model.setText(rs.getString("text"));
					model.setCreateAt(rs.getTimestamp("create_at"));
					model.setUpdateAt(rs.getTimestamp("updated_at"));
					return model;
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
