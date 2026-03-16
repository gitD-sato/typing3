<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
 <head>
  <meta charset="UTF-8">
  <title>管理者画面</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin.css">
 </head>
 <body>
   <h2>課題文登録</h2>
   <form action="${pageContext.request.contextPath}/Admin" method="post">
    <input type="hidden" name="action" value="create">
    <textarea name="text" rows="5" cols="50"></textarea>
    <input type="submit" value="登録">
   </form>
   <h2>課題文一覧</h2>
   <table class="task-table">
     <tr><th style="width:5%">ID</th>
     <th style="width:40%">課題文</th>
     <th style="width:10%">登録日</th>
     <th style="width:10%">更新日</th>
     <th style="width:25%">修正</th>
     <th style="width:5%">削除</th>
     </tr>
      <c:forEach var="task" items="${taskList}">
       <tr>
        <td>${task.id}</td>
        <td>${task.text}</td>
        <td>${task.createAt}</td>
        <td>${task.updateAt}</td>
        <td>
         <!-- 修正フォーム -->
         <form action="${pageContext.request.contextPath}/Admin" method="post">
          <input type="hidden" name="action" value="update">
          <input type="hidden" name="id" value="${task.id}">
          <textarea name="text" class="edit-text">${task.text}</textarea><br>
          <input type="submit" value="修正" class="btn">
         </form>
         </td>
        <td>
         <!-- 削除フォーム -->
         <form action="${pageContext.request.contextPath}/Admin" method="post">
          <input type="hidden" name="action" value="delete">
          <input type="hidden" name="id" value="${task.id}">
          <input type="submit" value="削除" class="btn">
         </form>
        </td>
       </tr>
      </c:forEach>
    </table>
    <br>
    <br>
    <!-- super管理者だけ管理者一覧を表示 -->
    <c:if test="${sessionScope.role == 'super'}">
     <!-- admin-sectionでアンカーをつけここにリダイレクトするように -->    
     <h2 id="admin-section">管理者一覧</h2>
      <c:if test="${not empty errorMsg}">
       <p style="color:red; font-weight:bold">${errorMsg}</p>
      </c:if>
      <c:if test="${not empty successMsg}">
       <p style="color:green; font-weight:bold">${successMsg}</p>
      </c:if>
     <table class="admin-table">
      <tr>
       <th>ID</th>
       <th>ユーザー名</th>
       <th>パスワード変更</th>
       <th>削除</th>
      </tr>
      <c:forEach var="admin" items="${adminList}">
       <tr>
        <td>${admin.adminId}</td>
        <td>${admin.username}</td>
        <!-- パスワード変更 -->
        <td>
         <form action="${pageContext.request.contextPath}/Admin" method="post">
          <input type="hidden" name="action" value="updateAdmin">
          <input type="hidden" name="username" value="${admin.username}">
          <input type="password" name="password" placeholder="新パスワード">
          <input type="submit" value="変更">
         </form>
        </td>
        <!-- 削除 -->
        <td>
         <form action="${pageContext.request.contextPath}/Admin" method="post">
          <input type="hidden" name="action" value="deleteAdmin">
          <input type="hidden" name="username" value="${admin.username}">
          <input type="submit" value="削除">
         </form>
         </td>
        </tr>
       </c:forEach>
      </table>
     </c:if>
    
    <br>
    <a href="${pageContext.request.contextPath}/Start">タイピング開始画面へ</a><br><br>
    <a href="${pageContext.request.contextPath}/Logout">ログアウト</a><br><br>
    <a href="${pageContext.request.contextPath}/Register">管理者を追加する</a>
 </body>
</html>