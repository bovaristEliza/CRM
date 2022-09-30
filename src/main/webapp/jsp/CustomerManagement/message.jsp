<%--
  Created by IntelliJ IDEA.
  User: vespa
  Date: 10/10/2021
  Time: 18:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="model.mo.User"%>
<%String message = (String)request.getAttribute("applicationText");%>
<html>
<%@include file="/include/preProcessing.jsp"%>
<head>
    <title>Title</title>
    <%@include file="/include/header.jsp"%>
</head>
<body>
<%@include file="/include/menu.jsp"%>
<h1>
    <% if(message != null && message != "") {
        out.println(message);
    }
    %>
</h1>

<%@include file="/include/footer.jsp"%>
</body>
</html>
