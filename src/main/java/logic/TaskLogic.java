package logic;

import java.util.List;

import dao.TaskDAO;
import model.Task;

//TASKのDB操作のロジック
public class TaskLogic {
	private TaskDAO dao;
	
	//コンストラクタ
	public TaskLogic() {
		dao = new TaskDAO();
	}
	
	/**
	 * Taskリスト全件取得
	 * 
	 * @return 課題文(tasks)リスト
	 */
	public List<Task> getTaskList() {
		return dao.findAll();
	}
	
	/**
	 * Taskを追加
	 * 
	 * @param model 登録するタスク情報
	 * @return 登録が成功→true 失敗→false
	 */
	public boolean create(Task model) {
		return dao.create(model);
	}
	
	/**
	 * Task更新
	 * 
	 * @param model 登録するタスク情報
	 * @return 更新成功→true 失敗→false
	 */
	public boolean update(Task model) {
		return dao.update(model);
	}
	
	/**
	 * Task削除
	 * 
	 * @param id 指定されたタスクのID
	 * @return 削除成功→true 失敗→false
	 */
	public boolean delete(int id) {
	    return dao.delete(id);
	}

}
