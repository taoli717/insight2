/**
 * Created by Ken on 2/11/2015.
 */
angular.module( 'app' )
    .controller( 'stockCtrl', [ '$scope', '$http', 'GetLiveStockService', function ( $scope, $http, GetLiveStockService ) {

        // init variable
        $scope.chartData = [];
        $scope.tableFlag = false;
        $scope.scopeSelectName = 'AA';  // stock name
        $scope.scopeSelectStockSeq = 0; // stock seq

        var testChartData = [];

        var testService = GetLiveStockService.strTest( 'stock page' );
        console.log ( testService );

        $scope.hideTable = function() {
            $scope.tableFlag = false;
        };

        // find the buy point and sell point
        var findBuySellPoint = function ( dailyStocks, buyPoint, sellPoint ) {
            var tempStock = [];
            var tempData = "";
            angular.forEach( dailyStocks, function ( item ) {
                if( item.date == buyPoint ) {
                    // buy point, give it blue color
                    tempData = {
                        x: item.date,
                        open: parseFloat( item.open ),
                        high: parseFloat( item.high ),
                        low: parseFloat( item.low ),
                        close: parseFloat( item.close ),
                        marker: {
                            fillColor: 'blue',
                            lineWidth: 3,
                            lineColor: "blue" // inherit from series
                        }
                    };
                } else if ( item.date == sellPoint ) {
                    // sell point, give it orange color
                    tempData = {
                        x: item.date,
                        open: parseFloat( item.open ),
                        high: parseFloat( item.high ),
                        low: parseFloat( item.low ),
                        close: parseFloat( item.close ),
                        marker: {
                            fillColor: 'orange',
                            lineWidth: 3,
                            lineColor: "orange" // inherit from series
                        }
                    };
                } else {
                    // normal data
                    tempData = {
                        x: item.date,
                        open: parseFloat( item.open ),
                        high: parseFloat( item.high ),
                        low: parseFloat( item.low ),
                        close: parseFloat( item.close )
                    };
                }
                tempStock.push( tempData );
            });
            return tempStock;
        };

        // draw the chart
        var drawCharts = function ( containerId, chartData_1, chartName ) {
            $( containerId ).highcharts('StockChart', {
                chart : {
                    type: 'candlestick',
                    zoomType: 'x'
                },
                navigator : {
                    adaptToUpdatedData: false,
                    series : {
                        data : chartData_1
                    }
                },
                scrollbar: {
                    liveRedraw: false
                },
                title: {
                    text: chartName + ' stock history'
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
                },
                yAxis: {
                    floor: 0
                },
                series : [{
                    data : chartData_1,
                    dataGrouping: {
                        enabled: false
                    }
                }]
            });
        };

        // click the get data button
        $scope.getSampleData = function() {

            $scope.chartData = [];

            testChartData = [];

            GetLiveStockService.getStockByNameSeq( $scope.scopeSelectName, $scope.scopeSelectStockSeq )
                .then( function ( res ) {
                    $scope.tempSample = res.data;
                    $scope.tableFlag = true;

                    $scope.chartData = findBuySellPoint( $scope.tempSample.dailyStocks, $scope.tempSample.buyingDate, $scope.tempSample.sellingDate);

                    var containerId_1 = '#chartContainer_1';
                    var containerId_2 = '#chartContainer_2';
                    var stockName_1 = 'AA';

                    drawCharts( containerId_1, $scope.chartData, stockName_1 );
                    drawCharts( containerId_2, $scope.chartData, stockName_1 );

                }, function ( error ) {
                    // error
                    alert ( 'no data from back end: ' + error);
            });
        };
        // end of stock controller
    }]);
