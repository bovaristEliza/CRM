
<%@page import="model.mo.User"%>
<%@ page import="model.mo.Customer" %>
<%@ page import="model.mo.Schedule" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<%ArrayList<Schedule> scheduleList = (ArrayList<Schedule>)request.getAttribute("scheduleList");%>

<%@include file="/include/preProcessing.jsp"%>

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
<h1 style="padding: 20px;">Personal Agenda</h1>
<form style="margin-left: 80px;" action="Dispatcher?controllerAction=ScheduleManagement.scheduleFutureView" method="post">
    <button  type="submit">View future appointments</button>
</form>


<%if(scheduleList!=null){ %>

    <% if(scheduleList.isEmpty()){ %>

    <%} else { %>

    <br>
    <% for(int i = 0; i < scheduleList.size(); i+=1) { %>

        <table style="margin: auto; margin-left: 80px;">
            <tr >
                <% Timestamp selectedDateTime = scheduleList.get(i).getDateApp();
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String selectedDateTimeConverted= outputFormat.format(selectedDateTime);
                    %>
                <td class="td"><%=selectedDateTimeConverted %></td>
                <td class="td"><%=scheduleList.get(i).getCustomer().getBusinessName()%></td>
                <td class="td"><%=scheduleList.get(i).getNote().getContent()%></td>

            </tr>
        </table>
        <br>
    <%}%>

        <form style="margin-left: 80px;" class="form" action="Dispatcher?controllerAction=ScheduleManagement.scheduleDateView" method="post">
            <label for="date">Date</label>
            <input type="date" placeholder="YYYY-MM-DD" name="date" id="date" required>
            <button  type="submit">Search Appointment</button>
            <% if(request.getAttribute("appText") != null) {
                out.println();
                out.println(request.getAttribute("appText"));
            } else {
            }%>
        </form>


<% } %>
<%} else {%>
<% if(request.getAttribute("applicationText") != null) {
    out.println();
    out.println(request.getAttribute("applicationText"));
} else {
}
%>

<%} %>

<%@include file="/include/footer.jsp"%>
</body>
</html>
