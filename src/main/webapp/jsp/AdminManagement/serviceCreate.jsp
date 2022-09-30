<%@ page import="model.mo.AppServices" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.mo.Opportunity" %>
<%@ page import="model.mo.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%boolean loggedOn = (Boolean) request.getAttribute("loggedOn");%>
<%User loggedUser = (User) request.getAttribute("loggedUser");%>

<%ArrayList<AppServices> appServicesList = (ArrayList<AppServices>)request.getAttribute("appServicesList");%>
<%ArrayList<Opportunity> opportunityList = (ArrayList<Opportunity>)request.getAttribute("opportunities");%>

<html>
<head>
    <%@include file="/include/header.jsp"%>
</head>

<body>
<%@include file="/include/menu.jsp"%>
<section id="customer" class="customer clearfix">

    <form  name="CustomerForm" action="Dispatcher?controllerAction=AdministratorManagement.addService" method="post">
        <h1 style="text-align: justify; padding-bottom: 40px;">Add a new service on Tie Me</h1>

        <div class="form-row">
            <div class="form-group col-md-6">
                <label for="serviceName">Service Name</label>
                <input type="text" class="form-control" name="serviceName" id="serviceName" placeholder="ex. consultation">
                <!--<input type="hidden" value="123" class="form-control" name="businessName" id="businessName" placeholder="ex. pinterest">-->
            </div>
            <div class="form-group col-md-6">
                <label for="serviceCost">Service Cost</label>
                <input type="text" class="form-control" name="serviceCost" id="serviceCost" placeholder="ex. 800 €">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group col-md-6">
                <label for="description">Service description</label>
                <input type="text" class="form-control" name="description" id="description">
            </div>
        </div>
        <button type="submit" class="btn btn-primary">+ add service</button><br>
        <%if(request.getAttribute("applicationText1")!=null) {out.println(request.getAttribute("applicationText1"));}%>
    </form>

    <form  name="oppForm" action="Dispatcher?controllerAction=OpportunityManagement.createOpportunity" method="post">
        <h1 style="text-align: justify; padding-bottom: 40px;">Add a new opportunity for customers</h1>

        <div class="form-row">
            <div class="form-group col-md-6">
                <label for="opportunityName">Name your opportunity</label>
                <input type="text" class="form-control" name="opportunityName" id="opportunityName" placeholder="ex. discount">
            </div>
        </div>

            <button type="submit" class="btn btn-primary">+ add service</button><br>
        <%if(request.getAttribute("applicationText2")!=null) {out.println(request.getAttribute("applicationText2"));}%>
    </form>

    <form  name="linkForm" action="Dispatcher?controllerAction=AdministratorManagement.linkServiceToOpp" method="post">
       <h1> Link a service to an opportunity </h1>
        <div class="form-row">
            <div class="form-group col-md-6">
                <label for="services">Services</label>
                <select name="services" id="services">
                    <% for(int i = 0; i < appServicesList.size(); i+=1) { %>
                    <option value="<%=appServicesList.get(i).getIdservice()%>"><%=appServicesList.get(i).getServiceName()%></option>
                    <% } %>
                </select>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group col-md-6">
                <label for="opportunity">Select Opportunity</label>
                <select name="opportunity" id="opportunity">
                    <% for(int i = 0; i < opportunityList.size(); i+=1) { %>
                    <option value="<%=opportunityList.get(i).getIdopp()%>"><%=opportunityList.get(i).getOppName()%></option>
                    <% } %>
                </select>
            </div>
        </div>

        <button type="submit" class="btn btn-primary">link</button><br>
        <% if(request.getAttribute("applicationText3") != null) {
            out.println();
            out.println(request.getAttribute("applicationText3"));
        } else {
        }
        %>

    </form>

</section>

<%@include file="/include/footer.jsp"%>
</body>
</html>
