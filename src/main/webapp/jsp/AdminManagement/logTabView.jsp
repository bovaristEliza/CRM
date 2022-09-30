
<%@ page import="model.mo.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.mo.LoggerTab" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%boolean loggedOn = (Boolean) request.getAttribute("loggedOn");%>
<%User loggedUser = (User) request.getAttribute("loggedUser");%>

<%ArrayList<LoggerTab> logList = (ArrayList<LoggerTab>)request.getAttribute("logList");%>

<html>
<head>
    <%@include file="/include/header.jsp"%>
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

        <td class="td"><b>ID log</b></td>
        <td class="td"><b>User</b></td>
        <td class="td"><b>Log time</b></td>
        <td class="td"><b>Action Type</b></td>
        <td class="td"><b>Action Table</b></td>
        <td class="td"><b>Action Record ID</b></td>

    </tr>
    <% for(int i = 0; i < logList.size(); i+=1) { %>

    <tr >
        <td class="td"><%=logList.get(i).getIdLogger()%></td>
        <td class="td"><%=logList.get(i).getUser().getFirstname()%> <%=logList.get(i).getUser().getSurname()%></td>
        <td class="td"><%=logList.get(i).getLogTime()%></td>
        <td class="td"><%=logList.get(i).getActionType()%></td>
        <td class="td"><%=logList.get(i).getActionTable()%></td>
        <td class="td"><%=logList.get(i).getActionRecordId()%></td>
    </tr>
    <%}%>
</table>

<%@include file="/include/footer.jsp"%>
</body>
</html>