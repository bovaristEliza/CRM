<%--
  Created by IntelliJ IDEA.
  User: vespa
  Date: 11/10/2021
  Time: 17:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@include file="/include/preProcessing.jsp"%>
<%@page import="model.mo.User"%>
<%User puser =(User)request.getAttribute("profileUser");%>

<html>
<%String message = (String)request.getAttribute("applicationText");%>
<head>
    <%@include file="/include/header.jsp"%>
</head>

<style>
    .parent {
        border: 1px solid black;
        margin: 1rem;
        padding: 2rem 2rem;
        text-align: center;
    }
    .child {
        display: inline-block;
        border: 1px;
        padding: 1rem 1rem;
        vertical-align: middle;
    }
</style>
<body>
<%@include file="/include/menu.jsp"%>


<% if (!loggedOn && message != null && message != "") {%>
            out.println(message);
<%} else {%>
<%}%>
    <link type = "text/css" href = "kustyle.css" media = "all" rel="stylesheet"/>
    <section class="cover">
        <div class="cover__filter"></div>
        <div class="cover__caption">
            <div class="cover__caption__copy">
                <h1>Welcome back administrator <%=loggedUser.getFirstname()%> <%=loggedUser.getSurname()%>!</h1>
                <a href="Dispatcher?controllerAction=AdministratorManagement.viewService" class="button">add services here</a>
            </div>
        </div>
    </section>

<div >
    <div class="child" style="margin: 10px; ">
        <h2> Create new user </h2>
            <a href="Dispatcher?controllerAction=AdministratorManagement.registerView" class="button">Create user</a>

    </div>

    <div class="child" style="margin: 10px; alignment: left;">
        <h2>Manage users</h2>
        <a href="Dispatcher?controllerAction=AdministratorManagement.addAdminView" class="button">Manage users</a>
    </div>

<div class="child" style="margin: 10px; alignment: left;">
    <h2>View application logs</h2>
    <a href="Dispatcher?controllerAction=AdministratorManagement.viewLogTab" class="button">View logs</a>
</div>

</div>

<%@include file="/include/footer.jsp"%>
</body>
</html>
