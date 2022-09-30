<%@page session="false"%>
<%@page import="model.mo.User"%>
<%@ page import="model.mo.Customer" %>

<%@include file="/include/preProcessing.jsp"%>

<%Customer customerRecipient = (Customer) request.getAttribute("yourCustomer");%>


<!DOCTYPE html>

<html>


<%@include file="/include/scripts.jsp"%>

<head>
    <%@include file="/include/header.jsp"%>
</head>

<style>

    .mybody{ background-color: rgb(187, 231, 187);}

    .textarea {
        display: block;
        width: 100%;
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
        border-radius: 10px;
        border-style: hidden;
    }
</style>

<body class="mybody">
<%@include file="/include/menu.jsp"%>
<main>
    <% if (loggedOn) {%>

    <div style="width: max-content; margin:0 auto;">
        <h2 style="font-size:30px">Create a Note</h2>
        <form action="Dispatcher?controllerAction=NotesManagement.tagNote"  method="POST" id="submitForm">
            <input type="hidden" name="appointment" value="false" id="setAppointment" />
            <div>
                <label>Customer addressee</label>
                <div>
                    <input style="border-style: hidden; padding: 10px;"  type="customerName" name="customerName" value="<%out.println(customerRecipient.getBusinessName());%>">
                    <input type="hidden" name="idCustomer" id="idCustomer" value="<%=customerRecipient.getIdcustomer()%>">
                </div>
            </div>

            <br>
            <div>
                <label>Your Memo</label>
                <div>
                    <textarea class="textarea" wrap="off" cols=80% rows=10% type="text" name="content" id="content" placeholder="Enter your text" required="required"></textarea>
                    <input type="hidden" name="idmanager" id="idmanager" value="<%= loggedUser.getIduser()%>">
                </div>
            </div>
            <br>
            <button  type="submit">Tag note</button>
            <br>
            <h2>Want to schedule an appointment with <%out.println(customerRecipient.getBusinessName());%>?</h2>
            <div>
                <label>Choose appointment date and time</label>
                <div>
                    <input type="date" name="date" id="date">
                    <input type="time" name="time" id="time">
                </div>
            </div>
            <br>
            <button  id="setAppointmentButton" type="submit">Set Appointment</button>
            <%if(request.getAttribute("applicationText")!=null) {out.println(request.getAttribute("applicationText"));}%>
        </form>
    </div>

    <%} else {%>
    <%@include file="/include/home-guest.jsp"%>
    <%}%>

</main>

<%@include file="/include/footer.jsp"%>
</body>

<script>
    $('#setAppointmentButton').click(function(){
        $('#setAppointment').val("true");
        $('#submitForm').submit();
    });
</script>

</html>
