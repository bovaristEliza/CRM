<%--
  Created by IntelliJ IDEA.
  User: vespa
  Date: 27/09/2021
  Time: 20:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/include/preProcessing.jsp"%>
<%@page import="model.mo.User"%>

<!DOCTYPE html>

<html>

<%@include file="/include/scripts.jsp"%>

<%if(loggedOn!=false || loggedUser!=null){

    User puser =(User)request.getAttribute("profileUser");
%>
<html>

<head>
    <%@include file="/include/header.jsp"%>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

    <!-- Popper JS -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

    <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700" rel="stylesheet">
    <link href="src/main/webapp/WEB-INF/profile.css" rel="stylesheet" type="text/css">
    <title>Title</title>
</head>

<%@include file="/include/menu.jsp"%>

<style>
    .flex-container {
        display: flex;
    }

    .flex-child {
        flex: 1;

    }

    .flex-child:first-child {
        margin-right: 20px;
    }
</style>

<body>


<div  class="flex-container">

        <div class="card flex-child">
            <div class="card-body">
                <div class="d-flex flex-column align-items-center text-center">
                    <img src="<%= request.getContextPath() %>/images/celtic-knot.png"  width="150">
                    <div class="mt-3">
                        <h4><%= loggedUser.getFirstname() %> </h4>
                        <p class="text-secondary mb-1"><%  out.println(puser.getEmployment());%></p>
                        <p class="text-muted font-size-sm"> <%  out.println(puser.getTel());%> </p>
                        <a href="Dispatcher?controllerAction=ProfileManagement.delete"><button class="btn btn-outline-primary">Delete profile</button></a>
                    </div>
                </div>
            </div>
        </div>

    <div class="container flex-child"  id="child1" style="padding:20px;">
                        <div class="row">
                            <div class="col-sm-3">
                                <h6 class="mb-0">Full Name</h6>
                            </div>
                            <div class="col-sm-9 text-secondary">
                                <% out.println(puser.getFirstname()); %> <% out.println(puser.getSurname()); %>
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-sm-3">
                                <h6 class="mb-0">Email</h6>

                            </div>
                            <div class="col-sm-9 text-secondary">
                                <% out.println(puser.getEmail()); %>
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-sm-3">
                                <h6 class="mb-0">Department</h6>
                            </div>
                            <div class="col-sm-9 text-secondary">
                                <% out.println(puser.getEmployment()); %>
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-sm-3">
                                <h6 class="mb-0">Gender</h6>
                            </div>
                            <div class="col-sm-9 text-secondary">
                                <% out.println(puser.getGender()); %>
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-sm-3">
                                <h6 class="mb-0">Tel</h6>
                            </div>
                            <div class="col-sm-9 text-secondary">
                                <% out.println(puser.getTel()); %>
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-sm-12">
                                <a id="button" class="btn btn-info"
                                   href="Dispatcher?controllerAction=ProfileManagement.updateView">Edit</a>
                            </div>
                        </div>

    </div>
</div>

<%} %>


<%@include file="/include/footer.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/jscript/profile.js"></script>
</body>

</html>