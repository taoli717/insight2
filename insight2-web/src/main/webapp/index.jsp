<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<html>
    <head>
        <title>Tao's Project</title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

        <link rel="stylesheet" type="text/css" href="/resources/openSource/bootstrap-3.1.1/bootstrap.css" />
        <script src="/resources/openSource/jquery-1.11.1/jquery.js"></script>
        <script src="/resources/openSource/bootstrap-3.1.1/bootstrap.js"></script>
        <script src="/resources/openSource/angular-1.2.28/angular.js"></script>
        <script src="/resources/openSource/angular-ui-router-0.2.13/angular-ui-router.js"></script>
        <script src="http://code.highcharts.com/stock/highstock.js"></script>
        <script src="http://code.highcharts.com/stock/modules/exporting.js"></script>
        <script src="/resources/js/app.js"></script>
    </head>

    <body ng-app="routerApp">
    <!-- NAVIGATION -->
    <nav class="navbar navbar-inverse" role="navigation">
        <div class="navbar-header">
            <a class="navbar-brand" ui-sref="#">Tao's Project</a>
        </div>
        <ul class="nav navbar-nav">
            <li><a ui-sref="home">Home</a></li>
            <li><a ui-sref="stock">Stock</a></li>
        </ul>
    </nav>

    <!-- Main Content -->
    <div class="container-fluid">
        <!-- views -->
        <div ui-view></div>
    </div>
    </body>
</html>
