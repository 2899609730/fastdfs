<%--
  Created by IntelliJ IDEA.
  User: mr.fox
  Date: 2024/4/21
  Time: 9:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>上传图片</title>
</head>
<body>
<form action="upload" method="post" enctype="multipart/form-data">
    <input type="file" name="fname">
    <br>
    <button>提交</button>
</form>
</body>
</html>
