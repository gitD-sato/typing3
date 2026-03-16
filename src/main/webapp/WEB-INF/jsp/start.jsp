<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
 <head>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="main.css">
  <title>開始画面</title>
 </head>
 <body>
  <h1>タイピング演習へようこそ</h1>
  <p>演習開始をクリックすると、演習画面に進みます。</p>
  <p>開始したら、表示しているテキストを入力してください。</p>
  <form action="Start" method="post">
    <input type="submit" value="演習開始！">
  </form>
  <br>
  <a href="${pageContext.request.contextPath}/Login">管理者ログイン</a>
 </body>
</html>