<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
 <head>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="main.css">
  <title>演習画面</title>
 </head>
 <body>
  <h2>次のテキストを入力してください</h2>
  <p class="exercise-text">${exerciseText}</p>
  <%--
   fomeタグのautocomplete="off"は履歴から候補を出さない設定。だがクロームは無視される。
  --%>
  <br>
  <br>
  <form action="Typing" method="post" autocomplete="off">
     <input type="hidden" name="userInput" id="hiddenInput">
      <%--
      自動保管と自動修正なし、自動大文字化なし、スペルチェック無効
       --%>
      <div id="displayArea"
          class="typing-textarea" 
          contenteditable="true"
          autocomplete="off"     
          autocorrect="off"     
          autocapitalize="off"    
          spellcheck="false" >     
      </div>
     <input type="submit" name="action" value="終了して結果へ">
  </form>
  <script>
     const editor = document.getElementById("displayArea");
     const hidden = document.getElementById("hiddenInput");
     //フォーム送信前にhiddenに値を入れる
     document.querySelector("form").addEventListener("submit", ()=> {
         let text = editor.innerText; //入力内容を送信
         text = text.replace(/\u200B/g, ""); //0幅スペースを削除
         hidden.value = text;
          });
  </script>
 </body>
</html>