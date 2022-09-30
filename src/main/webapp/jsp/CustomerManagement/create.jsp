<%@page session="false"%>
<%@page import="model.mo.User"%>
<%@page import="model.mo.Customer"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.*" %>
<%@ page import="model.mo.AppServices" %>


<%@include file="/include/preProcessing.jsp"%>
<%%>
<%Object list = request.getAttribute("CustomerList");%>
<%ArrayList<Customer> newlist = (ArrayList<Customer>)list;%>
<%ArrayList<AppServices> appServicesList = (ArrayList<AppServices>)request.getAttribute("appServicesList");%>
<%String emailError = (String)request.getAttribute("emailError");%>
<!DOCTYPE html>

<html>

<%@include file="/include/scripts.jsp"%>

<head>
    <%@include file="/include/header.jsp"%>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <a id="top"></a>
</head>

<style>
    .customer{
        background-color: #eee;
        max-width: fit-content;
        margin: 80px auto;
        border-radius: 10px;
        padding: 20px;
        border-left: 7px solid #009688;
        box-shadow: #1dad9a 0px 0px 0px 0px ;
    }

    .container {
        padding: 2px 16px;
    }


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


</style>

<body>
<%@include file="/include/menu.jsp"%>
<main>

        <section id="customer" class="customer clearfix">

            <form  name="CustomerForm" action="Dispatcher?controllerAction=CustomerManagement.newCustomer" method="post">
             <h1 style="text-align: justify; padding-bottom: 40px;">Tie a new customer to your profile</h1>

                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label for="businessName">Customer Name</label>
                        <input type="text" class="form-control" name="businessName" id="businessName" placeholder="ex. Meta" required>
                        <!--<input type="hidden" value="123" class="form-control" name="businessName" id="businessName" placeholder="ex. pinterest">-->
                    </div>
                    <div class="form-group col-md-6">
                        <label for="spokesperson">Business Spokesperson E-mail or Phone</label>
                        <input type="text" class="form-control" name="spokesperson" id="spokesperson" placeholder="JhonDoe@outlook.com" required>
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

                <div class="form-group col-md-6">
                    <label for="services">Selling Product</label>
                    <input type="text" class="form-control" name="product" id="product" placeholder="technology" required>
                </div>
                </div>

                <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="contract">Signed Contract</label>
                    <input type="text" class="form-control" name="contract" id="contract" placeholder="type of contract" required>
                </div>
                <div class="form-group col-md-6">
                    <label for="customerType">Customer Status</label>
                    <input type="text" class="form-control" name="customerType" id="customerType" placeholder="status of customer" required>
                </div>
                </div>
                <div style="display:none">
                    <label for="customerType">userid</label>
                    <input type="text" value="userid" name="userid" id="userid">

                </div>

                <button type="submit" class="btn btn-primary">Create customer</button><br>
                <div style="padding:10px;">
                    <a href="javascript:ViewList.submit()">View your customers</a><br>
                </div>
                <p class="error" style="color:red">
                    <% if(emailError != null && emailError != "") {
                        out.println(emailError);
                    }
                    %>
                </p>
            </form>

            <% if (loggedOn) {%>

            <% if(request.getAttribute("createdCustomerBusinessName") != null)
            {
                out.println();
                out.println("Customer " + request.getAttribute("createdCustomerBusinessName") + " succesfully added!");
            }

            %>
            <%} else {%>
            <%out.println("errore creazione contatto");%>
            <%}%>

            <% if (loggedOn) {%>
            <% if(request.getAttribute("appText") != null)
            {
                out.println();
                out.println(request.getAttribute("appText"));
            }
            %>
            <%} else {%>
            <%out.println("errore creazione contatto");%>
            <%}%>

        </section>


    <form name="ViewList" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="CustomerManagement.showCustomers"/>
    </form>

    <div style="padding: 20px; " >
    <label>
        <input type="radio" name="colorCheckbox" value="view" checked> Full view </label>
    <label>
        <input type="radio" name="colorCheckbox" value="Name" > Name </label>
    <label>
        <input type="radio" name="colorCheckbox" value="Product"> Product </label>
    <label>
        <input type="radio" name="colorCheckbox" value="Search"> Free text search </label>


        <form class="myform-a" style="display: none" name="ViewListNames" action="Dispatcher?controllerAction=CustomerManagement.showCustomers" method="post">
            <label for="stringName">Find a Customer</label>
            <input type="text" placeholder="Enter Business name" name="stringName" id="stringName">
            <a href="javascript:ViewListNames.submit()">Search name </a><br>
        </form>

        <form class="myform-b" style="display: none" class="myform"  name="ViewListProducts" action="Dispatcher?controllerAction=CustomerManagement.showCustomers" method="post">
            <label for="stringProduct">Find a Customer</label>
            <input type="text" placeholder="Enter Business name" name="stringProduct" id="stringProduct">
            <a href="javascript:ViewListProducts.submit()">Search product</a><br>
        </form>

    <form class="myform-c" style="display: none" class="myform"  name="ViewListSearch" action="Dispatcher?controllerAction=CustomerManagement.showCustomers" method="post">
        <label for="search">Find a Customer</label>
        <input type="text" placeholder="Type any word" name="search" id="search">
        <a href="javascript:ViewListSearch.submit()">Search </a><br>
    </form>

            <%if(newlist!=null){ %>

                <% if(newlist.isEmpty()){ %>
                <p>No matching customers. Please check your spelling or <a href="#top"> insert a new customer</a>
                </p>
                <%} else { %>
                <% for(int i = 0; i < newlist.size(); i+=1) { %>
                <div class="table">
                    <table>
                    <tr >
                        <td class="td"><%=newlist.get(i).getBusinessName()%></td>
                        <td class="td"><%=newlist.get(i).getSpokeperson()%></td>
                        <td class="td"><%=newlist.get(i).getAppServices().getServiceName()%></td>
                        <td class="td"><%=newlist.get(i).getProduct()%></td>
                        <td><a href="Dispatcher?controllerAction=CustomerManagement.customerView&idCustomer=<%=newlist.get(i).getIdcustomer()%>">view full profile</a></td>

                    </tr>

                    </table>
                </div>

                    <% } %>
                            <a href="#top">Back to top</a>
                    <%}%>
                    <%}  else {%>

                            <%} %>

    </div>

    <%@include file="/include/footer.jsp"%>

</main>

<script>
    $(document).ready(function() {
        $('input[name=colorCheckbox]:radio').change(function(e) {
            let value = e.target.value.trim()

            $('[class^="myform"]').css('display', 'none');

            switch (value) {
                case 'Name':
                    $('.myform-a').show()
                    break;
                case 'Product':
                    $('.myform-b').show()
                    break;
                case 'Search':
                    $('.myform-c').show()
                    break;
                default:
                    break;
            }
        })
    })

</script>

</body>

</html>



