<%--
  Created by IntelliJ IDEA.
  User: vespa
  Date: 23/09/2021
  Time: 11:42
  To change this template use File | Settings | File Templates.
--%>

<%String emailError = (String)request.getAttribute("emailError");%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page session="false"%>
<%@page import="model.mo.User"%>
<%@include file="/include/preProcessing.jsp"%>

<!DOCTYPE html>

<html>

<%@include file="/include/scripts.jsp"%>

<head>
    <%@include file="/include/header.jsp"%>
    <title> Registration </title>
</head>

<style>
    .register{
        background-color: #eee;
        max-width: max-content;
        margin: 80px auto;
        border-radius: 10px;
        padding: 20px;
        border-left: 7px solid #009688;
        box-shadow: #1dad9a 0px 0px 0px 0px ;
    }

    td {
        padding: 5px;
    }


</style>

<body>

<%@include file="/include/menu.jsp"%>
    <section class="register clearfix" id="register">
        <h1>Registration</h1>
        <form  name="UserForm" action="Dispatcher?controllerAction=AdministratorManagement.registerUser" method="post">
            <table>
                <tr>
                    <td>First Name</td>
                    <td><input type="text" name="firstname" /></td>
                </tr>
                <tr>
                    <td>Last Name</td>
                    <td><input type="text" name="surname" /></td>
                </tr>
                <tr>
                    <td>Password</td>
                    <td><input type="password" name="password" /></td>
                </tr>
                <tr>
                    <td>Email</td>
                    <td><input type="text" name="email" /></td>
                    <p class="error" style="color: red">
                        <% if(emailError != null && emailError != "") {
                            out.println(emailError);
                        }
                        %>
                    </p>
                </tr>
                <tr>
                    <td> Tel </td>
                    <td><input type="text" name="tel" /></td>
                </tr>

                <tr>
                    <td>Employment</td>
                    <td><input type="text" name="employment" /></td>
                </tr>


                <tr>
                    <td>Gender</td>
                    <td>
                        <input type="radio" name="gender" value="male"> Male<br>
                        <input type="radio" name="gender" value="female"> Female<br>
                        <input type="radio" name="gender" value="other"> Other <br>
                    </td>
                </tr>

            </table>
            <input type="submit" value="Submit" />

            <% if(request.getAttribute("appText") != null) {
                out.println();
                out.println(request.getAttribute("appText"));
            } else if(request.getAttribute("registeredUserFirstname") != null) {
                out.println();
                out.println("User " + request.getAttribute("registeredUserFirstname") +" "+ request.getAttribute("registeredUserSurname")+" " +"created.");
            }
            %>
        </form>
    </section>
<%@include file="/include/footer.jsp"%>
</body>
</html>
