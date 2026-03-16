<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
 <head>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="main.css">
  <title>管理者登録</title>
 </head>
 <body>
  <h2>管理者登録フォーム</h2>
  <form action="${pageContext.request.contextPath}/Register" method="post" class="register-form">
   ユーザー名:<input type="text" name="username" value="${param.username}" class="form-input"><br>
   パスワード:<input type="password" name="password" class="form-input"><br>
   <input type="submit" value="登録" class="form-button">
  </form>
  <!-- エラーメッセージ表示 -->
  <p style="color:red">${errorMsg}</p>
  <a href="${pageContext.request.contextPath}/Admin">管理者画面に戻る</a>
 </body>
</html>