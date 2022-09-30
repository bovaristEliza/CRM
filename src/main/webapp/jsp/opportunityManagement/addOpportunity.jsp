<%@page session="false"%>
<%@page import="model.mo.User"%>
<%@ page import="model.mo.Customer" %>
<%@ page import="model.mo.Opportunity" %>
<%@ page import="java.util.ArrayList" %>

<%@include file="/include/preProcessing.jsp"%>

<%Customer customerRecipient = (Customer) request.getAttribute("yourCustomer");%>
<%ArrayList<Opportunity> opportunityList = (ArrayList<Opportunity>)request.getAttribute("opportunityList");%>

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
<form name="CustomerForm" action="Dispatcher?controllerAction=OpportunityManagement.addOpportunity" method="post">
    <h1>Add a new opportunity for <%= customerRecipient.getBusinessName()%></h1>
    <input type="hidden" name="customerid" id="customerid" value="<%=customerRecipient.getIdcustomer()%>">
    <div class="form-row">
        <div class="form-group col-md-6">
            <label for="opportunity">Select Opportunity</label>
            <select name="opportunity" id="opportunity">
                <% for(int i = 0; i < opportunityList.size(); i+=1) { %>
                <option value="<%=opportunityList.get(i).getIdopp()%>"><%=opportunityList.get(i).getOppName()%></option>
                <% } %>
            </select>
        </div>
        <button  type="submit">SUBMIT</button>

        <br>
        <%if(request.getAttribute("applicationText")!=null) {out.println(request.getAttribute("applicationText"));}%>
        <br>
        <a href="Dispatcher?controllerAction=CustomerManagement.customerView&idCustomer=<%=customerRecipient.getIdcustomer()%>">Go back to <%= customerRecipient.getBusinessName()%>'s page</a>
    <%} else {%>

    <%}%>
</form>
</main>

<%@include file="/include/footer.jsp"%>
</body>

</html>
