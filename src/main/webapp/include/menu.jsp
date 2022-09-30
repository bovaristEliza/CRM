

<%String applicationText = (String)request.getAttribute("applicationText");%>
<header class="header clearfix" >
    <div class="logo" id="logo" style="padding: 10px; margin-top: 5px; margin-left: 5px;" >
        <a href="Dispatcher?controllerAction=HomeManagement.view"> <img src="<%= request.getContextPath() %>/images/logo.png"/></a>


    </div>

    <form name="logoutForm" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="HomeManagement.logout"/>
    </form>

    <ul class="header__menu animate" style="padding:2px;">
        <li class="header__menu__item" >
            <a href="Dispatcher?controllerAction=HomeManagement.view">Home</a></li>

        <% if (loggedOn) {%>
            <li class="header__menu__item"><a href="Dispatcher?controllerAction=ProfileManagement.profileView">Profile</a></li>
            <li class="header__menu__item"><a href="Dispatcher?controllerAction=CustomerManagement.create">Customers</a></li>
        <li class="header__menu__item"><a href="Dispatcher?controllerAction=ScheduleManagement.scheduleView">Schedule</a></li>
        <%} %>
        <% if (loggedOn && loggedUser.getIsAdmin() == 1) {%>
            <li class="header__menu__item"><a href="Dispatcher?controllerAction=AdministratorManagement.adminView">Admin</a></li>
        <%} %>
        <% if (loggedOn) {%>
            <li class="header__menu__item"><a href="javascript:logoutForm.submit()">Logout</a></li>
        <%} %>
        <% if (!loggedOn) {%>

        <% } %>
    </ul>

        <% if (!loggedOn) {%>
                <form class="login" name="logonForm" action="Dispatcher" method="post" style="padding: 20px; float: right; margin-right: 40px">
                    <label for="email">Email</label>
                    <input type="text" id="email"  name="email" maxlength="40" required>
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" maxlength="40" required>
                    <input type="hidden" name="controllerAction" value="HomeManagement.logon"/>
                    <input type="submit" value="login">
                    <p class="error">
                        <% if(applicationText != null && applicationText != "") {
                            out.println(applicationText);
                        }
                        %>
                    </p>
                </form>

        <%} %>
</header>

