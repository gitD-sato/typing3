<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
 <head>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="main.css">
  <title>結果画面</title>
 </head>
 <body>
  <h1>タイピングの結果</h1>
  <p>課題文文字数: ${result.targetLength}</p>
  <p>入力文字数: ${result.inputLength}</p>
  <p>正解文字数: ${result.correctLength}</p>
  <p>正解率: ${result.accuracy}%</p>
  <p>経過時間: ${result.elapsedTimeFormatted}</p>
  <!-- #,##0は３桁ごとにカンマ区切り .0で小数点以下1桁を表示 -->
  <p>入力速度: <fmt:formatNumber value="${result.speedCpm}" pattern="#,##0.0"/> 文字/分</p>
  <h3>課題文</h3>
  <div class="result-width">${exerciseText}</div>  
  <h3>入力内容(間違いは赤表示)</h3>
  <div class="input-text">
    ${result.highlightedResult}
  </div>
  <br>
  <br>
  <a href="${pageContext.request.contextPath}/Start">開始画面へ</a>
  <br>
 </body>
</html>