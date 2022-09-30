<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/include/preProcessing.jsp"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.mo.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.text.SimpleDateFormat" %>

<!DOCTYPE html>

<html>

<%@include file="/include/scripts.jsp"%>
<%Customer customer = (Customer)request.getAttribute("yourCustomer");%>
<%@include file="/include/header.jsp"%>
<%Object list = request.getAttribute("customerNotes");%>

<%if(list==null){
    System.out.println("e nullo");
} else {
    System.out.println("non nullo");
}
%>
<%ArrayList<AppServices> appServicesList = (ArrayList<AppServices>)request.getAttribute("appServicesList");%>
<%ArrayList<Notes> notesList = (ArrayList<Notes>)list;%>

<%ArrayList<Opportunity> opportunityList = (ArrayList<Opportunity>)request.getAttribute("opportunityList");%>
<%String emailError = (String)request.getAttribute("emailError");%>
<!--TODO: cancellazione profilo, rispedisco alla create, mando customer corrente e tolgo dalla visibilita -->
<head>

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

    <!-- Popper JS -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>


    <link href="src/main/webapp/WEB-INF/profile.css" rel="stylesheet" type="text/css">

</head>

<body>
<%@include file="/include/menu.jsp"%>
<h1> </h1>
    <div class="container " id="customerView" style="margin-top:40px;">
        <div class="main-body" id="p" class="hideP">
            <h1><%out.println(customer.getBusinessName());%>'s Profile</h1>
                <div class="col-md-8">
                    <div class=" mb-3">
                        <div class="useless">
                            <div class="row">
                                    <div class="col-sm-4">
                                        <h6 class="mb-0">Business Name</h6>
                                    </div>
                                    <div class="col-sm-9 text-secondary">
                                        <%out.println(customer.getBusinessName());%>
                                    </div>
                            </div>
                            <hr>
                            <div class="row">
                                    <div class="col-sm-4">
                                        <h6 class="mb-0">Business Spokesperson</h6>
                                    </div>
                                    <div class="col-sm-9 text-secondary">
                                        <% out.println(customer.getSpokeperson()); %>
                                        <p class="error" style="color:red">
                                            <% if(emailError != null && emailError != "") {
                                                out.println(emailError);
                                            }
                                            %>
                                        </p>
                                    </div>
                            </div>
                            <hr>
                            <div class="row">
                                <div class="col-sm-4">
                                    <h6 class="mb-0">Customer status</h6>
                                </div>
                                <div class="col-sm-9 text-secondary">
                                    <% out.println(customer.getCustomerType()); %>
                                </div>
                            </div>
                            <hr>
                            <div class="row">
                                <div class="col-sm-4">
                                    <h6 class="mb-0">Service subscribed</h6>
                                </div>
                                <div class="col-sm-9 text-secondary">
                                    <% out.println(customer.getAppServices().getServiceName()); %>
                                    <br>
                                    Monthly cost: <%
                                    BigDecimal bd = new BigDecimal(String.valueOf(customer.getAppServices().getServiceCost()));
                                    bd.setScale(2, BigDecimal.ROUND_HALF_UP); // this does change bd
                                    bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                    out.println(bd);
                                    %> €
                                    <br>
                                    Service description: <% out.println(customer.getAppServices().getDescription()); %>
                                </div>
                            </div>

                            <hr>
                            <div class="row">
                                <div class="col-sm-4">
                                    <h6 class="mb-0">Product</h6>
                                </div>
                                <div class="col-sm-9 text-secondary">
                                    <% out.println(customer.getProduct()); %>
                                </div>
                            </div>

                            <hr>
                            <div class="row">
                                <div class="col-sm-4">
                                    <h6 class="mb-0">Contract</h6>
                                </div>
                                <div class="col-sm-9 text-secondary">
                                    <% out.println(customer.getContract()); %>
                                </div>
                            </div>

                            <hr>
                            <div class="row">
                                <div class="col-sm-12">
                                    <a id="reach" class="btn btn-warning" style="margin-right: 20px;"
                                       href="Dispatcher?controllerAction=NotesManagement.displayNotesForm&idCustomer=<%=request.getAttribute("yourid")%>">Reach <%=customer.getBusinessName()%></a>

                                    <a id="toggle-edit"class="btn btn-info"  style="margin-right: 20px;"
                                     >Edit</a>

                                    <a id="opportunity" class="btn btn-outline-dark"  style="margin-right: 20px;" href="Dispatcher?controllerAction=OpportunityManagement.displayOpp&customerid=<%=request.getAttribute("yourid")%>"
                                    >Add Opportunity</a>

                                    <a id="delete"class="btn btn-danger"  href="Dispatcher?controllerAction=CustomerManagement.deleteCustomer&custid=<%=request.getAttribute("yourid")%>"
                                    >Delete</a>


                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    <form id="edit" action ="Dispatcher?controllerAction=CustomerManagement.customerEdit&idCustomer=<%=request.getAttribute("yourid")%>" method="post">
        <div class="container">
                        <div class="card">
                            <div class="row mb-3">
                                        <div class="col-sm-4">
                                            <h6 class="mb-3">Business Name</h6>
                                        </div>
                                    <div class="col-sm-9 text-secondary">
                                        <input type="text" class="form-control" value="<%out.println(customer.getBusinessName()); %>" name="businessName" id="businessname">
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-sm-8">
                                        <h6 class="mb-4">Business Spokesperson</h6>
                                    </div>
                                    <div class="col-sm-9 text-secondary">
                                        <input type="text" class="form-control" value="<% out.println(customer.getSpokeperson()); %>" name="spokeperson" id="spokeperson">

                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-sm-8">
                                        <h6 class="mb-3">Customer Status</h6>
                                    </div>
                                    <div class="col-sm-9 text-secondary">
                                        <input type="text" class="form-control" value="<% out.println(customer.getCustomerType()); %>" name="customerType" id="customerType">
                                    </div>
                                </div>


                            <div class="row mb-3">
                                <div class="col-sm-8">
                                    <h6 class="mb-3">Product</h6>
                                </div>
                                <div class="col-sm-9 text-secondary">
                                    <input type="text" class="form-control" value="<% out.println(customer.getProduct()); %>" name="product" id="product">
                                </div>
                            </div>

                            <div class="row mb-3">
                                <div class="col-sm-8">
                                    <h6 class="mb-3">Contract</h6>
                                </div>
                                <div class="col-sm-9 text-secondary">
                                    <input type="text" class="form-control" value="<% out.println(customer.getContract()); %>" name="contract" id="contract">
                                </div>
                            </div>
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


                            <div class="row mb-3">
                                <div class="col-sm-8">
                                    <a id="toggle-customerView" class="btn btn-info">Customer profile</a>
                                    <input  style="margin-left: 20px;" class="btn btn-success" type="submit" value="Save Changes" >
                                </div>
                            </div>
                        </div>
        </div>
    </form>


<section class="clearfix" style="margin:50px;">
    <%if(notesList!=null){ %>

    <% if(notesList.isEmpty()){ %>
    <p>You have no notes</p>
    <%} else { %>
    <% for(int i = 0; i < notesList.size(); i+=1) { %>
    <article class="card" style="margin: 5px; width: 18rem; float: left; background-color: #adffb4">
        <div class="card-body">

            <%
                Timestamp selectedDateTime = notesList.get(i).getNoteDate();
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String selectedDateTimeConverted= outputFormat.format(selectedDateTime);
            %>
            <h5 class="card-title"><%=customer.getBusinessName()%> Memo</h5>
            <h6 class="card-subtitle mb-2 text-muted">Created on <%=selectedDateTimeConverted%></h6>
            <p class="card-text"><%=notesList.get(i).getContent()%></p>
            <a href="Dispatcher?controllerAction=NotesManagement.deleteNote&idnote=<%=notesList.get(i).getNoteid()%>&idCustomer=<%=customer.getIdcustomer()%>"
               class="icon" ><img align="right" width="20" height="20"src="<%= request.getContextPath() %>/images/trash-can.png"></a>

        </div>
    </article>
    <% } %>
    <%}%>
    <%}  else {%>

    <%} %>
</section>

<section class="clearfix" style="margin:50px;">
    <%if(opportunityList!=null){ %>

    <% if(opportunityList.isEmpty()){ %>
    <p>You have no opportunities</p>
    <%} else { %>
    <% for(int i = 0; i < opportunityList.size(); i+=1) { %>
    <article class="card" style="margin: 5px; width: 18rem; float: left; background-color: #c69bff">
        <div class="card-body">
            <h5 class="card-title text-muted">Active opportunity</h5>
            <h6 class="card-subtitle mb-2 "><%=opportunityList.get(i).getOppName()%></h6>
            <a href="Dispatcher?controllerAction=OpportunityManagement.deleteOpp&idopp=<%=opportunityList.get(i).getIdopp()%>&idCustomer=<%=customer.getIdcustomer()%>"
               class="icon" ><img align="right" width="20" height="20"src="<%= request.getContextPath() %>/images/cross.png"></a>
        </div>
    </article>
    <% } %>
    <%}%>
    <%}  else {%>

    <%} %>
</section>



<div hidden>Icons made by <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
<%@include file="/include/footer.jsp"%>

</body>




</body>

<script >

    $("#toggle-customerView").click(function() {
        $("#edit").hide().attr("formnovalidate");
        $("#customerView").show();
    });

    $("#toggle-edit").click(function() {
        $("#customerView").hide().attr("formnovalidate");
        $("#edit").show();
    });

    $("document").ready(function() {
        $("#edit").hide();
    });

</script>

</html>