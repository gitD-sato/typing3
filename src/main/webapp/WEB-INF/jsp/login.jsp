<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
 <head>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="main.css">
  <title>管理者ログイン</title>
 </head>
 <body>
  <h2>管理者ログイン</h2>
  <form action="${pageContext.request.contextPath}/Login" method="post" class="login-form">
   ユーザー名:<input type="text" name="username" class="form-input"><br>
   パスワード:<input type="password" name="password" class="form-input"><br>
   <input type="submit" value="ログイン" class="form-button">
  </form>
  <!-- エラーメッセージ表示 -->
  <p style="color:red">${errorMsg}</p>
 </body>
</html>