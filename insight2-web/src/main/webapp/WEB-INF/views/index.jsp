<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<%--<html>--%>
<%--<head>--%>
<%--<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">--%>
    <%--<script src="/resources/js/sample.js"></script>--%>
    <%--<link rel="stylesheet" type="text/css" href="/resources/openSource/bootstrap-3.1.1/bootstrap.css" />--%>
    <%--<script src="/resources/openSource/bootstrap-3.1.1/bootstrap.js"></script>--%>
<%--<title>Insight2</title>--%>
<%--</head>--%>
<%--<body>--%>
<div id="container" style="height: 400px; min-width: 310px"></div>
<script>
    $(function () {
        var seriesOptions = [],
                seriesCounter = 0,
                names = ['MSFT', 'AAPL', 'GOOG'],
        // create the chart when all data is loaded
                createChart = function () {

                    $('#container').highcharts('StockChart', {

                        rangeSelector: {
                            selected: 4
                        },

                        yAxis: {
                            labels: {
                                formatter: function () {
                                    return (this.value > 0 ? ' + ' : '') + this.value + '%';
                                }
                            },
                            plotLines: [{
                                value: 0,
                                width: 2,
                                color: 'silver'
                            }]
                        },

                        plotOptions: {
                            series: {
                                compare: 'percent'
                            }
                        },

                        tooltip: {
                            pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>',
                            valueDecimals: 2
                        },

                        series: seriesOptions
                    });
                };

        $.each(names, function (i, name) {

            $.getJSON('http://www.highcharts.com/samples/data/jsonp.php?filename=' + name.toLowerCase() + '-c.json&callback=?',    function (data) {

                seriesOptions[i] = {
                    name: name,
                    data: data
                };

                // As we're loading the data asynchronously, we don't know what order it will arrive. So
                // we keep a counter and create the chart when all the data is loaded.
                seriesCounter += 1;

                if (seriesCounter === names.length) {
                    createChart();
                }
            });
        });
    });
</script>
<br/>
<hr class="row"/>
<p ng-init="stockTest = '${stockSample}';" ng-hide="false">${stockSample}</p>
<%--<p>{{stockTest}}</p>--%>
<script>
   console.log(${stockSample});
   console.log(${stockSample}.buyingDate);
   console.log(${stockSample}.dailyStocks);
   console.log(${stockSample}.sellingDate);
   console.log(${stockSample}.seq);
   console.log(${stockSample}.stockCode);
   console.log(${stockSample}.stockName);
</script>
<%--</body>--%>
<%--</html>--%>