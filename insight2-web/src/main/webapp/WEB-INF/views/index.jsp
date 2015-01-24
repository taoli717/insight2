<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div ng-app>
    <div id="container1" style="height: 400px; min-width: 310px"></div>
    <script>
        $(function () {

            //sample connection, feel free to remove it
            $.ajax({
                url: "/api/sample/1" ,
                dataType: 'json',
                type: "GET"
            })
                    .done(function( data ) {
                        console.log(data);
                    });

            var seriesOptions = [],
                    seriesCounter = 0,
                    names = ['MSFT', 'AAPL', 'GOOG'],
            // create the chart when all data is loaded
                    createChart = function () {

                        $('#container1').highcharts('StockChart', {

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
    <div id="container2" style="height: 400px; min-width: 310px"></div>
    <script>
        $(function () {
            /**
             * Load new data depending on the selected min and max
             */
            function afterSetExtremes(e) {

                var chart = $('#container2').highcharts();

                chart.showLoading('Loading data from server...');
                $.getJSON('http://www.highcharts.com/samples/data/from-sql.php?start=' + Math.round(e.min) +
                '&end=' + Math.round(e.max) + '&callback=?', function (data) {

                    chart.series[0].setData(data);
                    chart.hideLoading();
                });
            }

            // See source code from the JSONP handler at https://github.com/highslide-software/highcharts.com/blob/master/samples/data/from-sql.php
            $.getJSON('http://www.highcharts.com/samples/data/from-sql.php?callback=?', function (data) {

                // Add a null value for the end date
                data = [].concat(data, [[Date.UTC(2011, 9, 14, 19, 59), null, null, null, null]]);

                // create the chart
                $('#container2').highcharts('StockChart', {
                    chart : {
                        type: 'candlestick',
                        zoomType: 'x'
                    },

                    navigator : {
                        adaptToUpdatedData: false,
                        series : {
                            data : data
                        }
                    },

                    scrollbar: {
                        liveRedraw: false
                    },

                    title: {
                        text: 'AAPL history by the minute from 1998 to 2011'
                    },

                    subtitle: {
                        text: 'Displaying 1.7 million data points in Highcharts Stock by async server loading'
                    },

                    rangeSelector : {
                        buttons: [{
                            type: 'hour',
                            count: 1,
                            text: '1h'
                        }, {
                            type: 'day',
                            count: 1,
                            text: '1d'
                        }, {
                            type: 'month',
                            count: 1,
                            text: '1m'
                        }, {
                            type: 'year',
                            count: 1,
                            text: '1y'
                        }, {
                            type: 'all',
                            text: 'All'
                        }],
                        inputEnabled: false, // it supports only days
                        selected : 4 // all
                    },

                    xAxis : {
                        events : {
                            afterSetExtremes : afterSetExtremes
                        },
                        minRange: 3600 * 1000 // one hour
                    },

                    yAxis: {
                        floor: 0
                    },

                    series : [{
                        data : data,
                        dataGrouping: {
                            enabled: false
                        }
                    }]
                });
            });
        });
    </script>
    <br/>
    <hr class="row"/>
    <p>${stockSample}</p>
    <script>
       console.log(${stockSample});
       console.log(${stockSample}.buyingDate);
       console.log(${stockSample}.dailyStocks);
       var temp = ${stockSample}.dailyStocks;
       console.log(${stockSample}.sellingDate);
       console.log(${stockSample}.seq);
       console.log(${stockSample}.stockCode);
       console.log(${stockSample}.stockName);
    </script>
    <p ng-init="tempStocks = temp"></p>
    <div ng-repeat="tempStock in tempStocks">
        Test<p>{{tempStock.close}}</p><br/>
    </div>
</div>