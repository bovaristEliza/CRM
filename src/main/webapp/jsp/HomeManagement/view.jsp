<%@page session="false"%>
<%@page import="model.mo.User"%>

<%@include file="/include/preProcessing.jsp"%>

<!DOCTYPE html>

<html>

<%@include file="/include/scripts.jsp"%>

<head>
    <%@include file="/include/header.jsp"%>
</head>

<body>
   <%@include file="/include/menu.jsp"%>
    <main>
        <% if (loggedOn) {%>
            <%@include file="/include/home-logged.jsp"%>
        <%} else {%>
            <%@include file="/include/home-guest.jsp"%>
        <%}%>

        <%@include file="/include/footer.jsp"%>

    </main>

</body>

</html>
