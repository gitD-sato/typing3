package model;

import java.sql.Timestamp;

public class Task {
	private int id; //主キー
	private String text; //課題文
	private java.sql.Timestamp createAt; //登録日時
	private java.sql.Timestamp updateAt; //更新日時
	
	public Task() { //引数なしの空のコンストラクタ。JavaBeansのルールで一応入れている。	
	}
	
	//主キーのゲッターとセッター
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	//課題文のゲッターとセッター
	public String getText() {
		return this.text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	//登録日時のゲッターとセッター
	public Timestamp getCreateAt() {
		return this.createAt;
	}
	public void setCreateAt(Timestamp createAt) {
		this.createAt = createAt;
	}
	
	//更新日時のゲッターとセッター
	public Timestamp getUpdateAt() {
		return this.updateAt;
	}
	public void setUpdateAt(Timestamp updateAt) {
		this.updateAt = updateAt;
	}
	

}
