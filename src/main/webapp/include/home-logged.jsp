<style>
    .card {
        padding: 10px;
        border: 30px;
        margin: 20px;
        width: fit-content;
        box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
        transition: 0.3s;
        border-radius: 5px; /* 5px rounded corners */
        display: none;
    }

    .card:hover {
        box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);
    }

    .container {
        padding: 2px 16px;
        max-width: min-content;
    }

    .flex-container {
        display: flex;
    }

    .flex-child {
        flex: 1;

    }

    .flex-child:first-child {
        margin-right: 20px;
    }

</style>
<link type = "text/css" href = "kustyle.css" media = "all" rel="stylesheet"/>
<section class="cover">
    <div class="cover__filter"></div>
    <div class="cover__caption">
        <div class="cover__caption__copy">
            <h1>Welcome back <%=loggedUser.getFirstname()%> <%=loggedUser.getSurname()%>!</h1>
            <h2>Start creating strong ties with your customers</h2>

        </div>
    </div>
</section>

<div  class="flex-container">

    <div class="card clearfix flex-child" id="card2" >
        <div class="container" style="margin-left: auto; margin-right: auto;">
            <h2>Tie Customers</h2>
            <img src="<%= request.getContextPath() %>/images/meeting.png"
                 width="200px" height="200px" style="border-radius: 10px; opacity: 95%; " />
            <p>  </p>
                <a href="Dispatcher?controllerAction=CustomerManagement.create"><button class="w3-button w3-block w3-dark-grey" >+ Add new customer</button></a>
        </div>
    </div>

    <div class="card clearfix flex-child" id="card" >
    <div class="container" style="margin-left: auto; margin-right: auto;">
        <h2>Check Schedule</h2>
        <img src="<%= request.getContextPath() %>/images/schedule.png"
             width="200px" height="200px" style="border-radius: 10px; opacity: 95%; " />
        <p> </p>
                <a href="Dispatcher?controllerAction=ScheduleManagement.scheduleView">
        <button class="btn btn-primary btn-sm">Check Schedule</button></a>
        </div>
    </div>

    <div class="card clearfix flex-child" id="card3" >
        <div class="container" style="margin-left: auto; margin-right: auto;">
            <h2>Personal Profile</h2>
            <img src="<%= request.getContextPath() %>/images/student.png"
                 width="200px" height="200px" style="border-radius: 10px; opacity: 95%" />
            <p>

                <a href="Dispatcher?controllerAction=ProfileManagement.profileView"><button class="w3-button w3-block w3-dark-grey" >Go to Profile</button></a>

            </p>

        </div>
    </div>
</div>



<div hidden>Icons made by <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>