
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page session="false"%>
<%@page import="model.mo.User"%>
<%@ page import="java.util.ArrayList" %>
<%@include file="/include/preProcessing.jsp"%>

<!DOCTYPE html>

<html>

<%ArrayList<User> userList = (ArrayList<User>)request.getAttribute("userList");%>

<%@include file="/include/scripts.jsp"%>

<head>
    <%@include file="/include/header.jsp"%>
    <title> Registration </title>
</head>

<style>
    table {
        border-spacing: 0.5rem;
    }
    td {
        padding: 0.5rem;
    }

    td:nth-child(1) { background: hsl(150, 50%, 50%); }
    td:nth-child(2) { background: hsl(160, 60%, 50%); }
    td:nth-child(3) { background: hsl(170, 70%, 50%); }
    td:nth-child(4) { background: hsl(180, 80%, 50%); }
    td:nth-child(5) { background: hsl(190, 90%, 50%); }
    td:nth-child(6) { background: hsl(200, 99%, 50%); }
    td:nth-child(7) { background: hsl(200, 99%, 50%); }
    td:nth-child(8) { background: hsl(200, 99%, 50%); }
    td:nth-child(9) { background: hsl(200, 99%, 50%); }

    a, a:hover, a:focus, a:active {
        text-decoration: none;
        color: inherit;
    }

</style>

<body>
<%@include file="/include/menu.jsp"%>
<table style="margin: 50px 0px 50px 80px; ">
    <tr >

        <td class="td"><b>ID</b></td>
        <td class="td"><b>First name</b></td>
        <td class="td"><b>Surname</b></td>
        <td class="td"><b>Email</b></td>
        <td class="td"><b>Admin</b></td>
        <td class="td"><b>Deleted</b></td>
        <td class="td"><b>Make Admin</b></td>
        <td class="td"><b>Revoke Admin</b></td>
        <td class="td"><b>Delete</b></td>

    </tr>
<% for(int i = 0; i < userList.size(); i+=1) { %>

        <tr >
            <td class="td"><%=userList.get(i).getIduser()%></td>
            <td class="td"><%=userList.get(i).getFirstname()%></td>
            <td class="td"><%=userList.get(i).getSurname()%></td>
            <td class="td"><%=userList.get(i).getEmail()%></td>
            <td class="td"><% if(userList.get(i).getIsAdmin() == 1) {out.println("Yes");} else out.println("No");%></td>
            <td class="td"><% if(userList.get(i).getDeleted().equals("Y")) {out.println("Yes");} else if(userList.get(i).getDeleted().equals("N")) out.println("No"); else out.println("To be deleted");%></td>
            <td class="td"><a href="Dispatcher?controllerAction=AdministratorManagement.addAdmin&iduser=<%=userList.get(i).getIduser()%>"><button  type="submit">Make Admin</button></a></td>
            <td class="td"><a href="Dispatcher?controllerAction=AdministratorManagement.revokeAdmin&iduser=<%=userList.get(i).getIduser()%>"><button  type="submit">Revoke Admin</button></a></td>
            <td class="td"><a href="Dispatcher?controllerAction=AdministratorManagement.deleteUser&iduser=<%=userList.get(i).getIduser()%>"><button  type="submit">Delete</button></a></td>
        </tr>

        <%}%>
</table>

        <%@include file="/include/footer.jsp"%>
</body>


</html>
