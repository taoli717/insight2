/**
 * Created by Ken on 2/11/2015.
 */
angular.module( 'app' )
    .controller( 'stockCtrl', [ '$scope', '$http', 'GetLiveStockService', function ( $scope, $http, GetLiveStockService ) {

        // init variable
        $scope.chartData_1 = [];
        $scope.tableFlag_1 = false;
        $scope.chartData_2 = [];
        $scope.tableFlag_2 = false;
        $scope.scopeSelectName_1 = 'AA';  // stock name
        $scope.scopeSelectStockSeq_1 = 0; // stock seq
        $scope.scopeSelectName_2 = 'AA';  // stock name
        $scope.scopeSelectStockSeq_2 = 0; // stock seq
        var tempStock;

        var testService = GetLiveStockService.strTest( 'stock page' );
        console.log ( testService );

        $scope.hideTable = function() {
            $scope.tableFlag_1 = false;
            $scope.tableFlag_2 = false;
        };

        // find the buy point and sell point
        var findBuySellPoint = function ( dailyStocks, buyPoint, sellPoint ) {
            tempStock = [];
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
        var drawCharts = function ( containerId, chartData, chartName ) {
            $( containerId ).highcharts('StockChart', {
                chart : {
                    type: 'candlestick',
                    zoomType: 'x'
                },
                navigator : {
                    adaptToUpdatedData: false,
                    series : {
                        data : chartData
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
                    data : chartData,
                    dataGrouping: {
                        enabled: false
                    }
                }]
            });
        };

        // click the get data button
        $scope.getSampleData = function() {

            $scope.chartData_1 = [];
            $scope.chartData_2 = [];

            // first chart
            GetLiveStockService.getStockByNameSeq( $scope.scopeSelectName_1, $scope.scopeSelectStockSeq_1 )
                .then( function ( res ) {
                    $scope.tempSample_1 = res.data;
                    $scope.tableFlag_1 = true;

                    $scope.chartData_1 = findBuySellPoint( $scope.tempSample_1.dailyStocks, $scope.tempSample_1.buyingDate, $scope.tempSample_1.sellingDate);

                    var containerId_1 = '#chartContainer_1';

                    drawCharts( containerId_1, $scope.chartData_1, $scope.scopeSelectName_1 );

                }, function ( error ) {
                    // error
                    alert ( 'no data from back end: ' + error);
            });

            // second chart
            GetLiveStockService.getStockByNameSeq( $scope.scopeSelectName_2, $scope.scopeSelectStockSeq_2 )
                .then( function ( res ) {
                    $scope.tempSample_2 = res.data;
                    $scope.tableFlag_2 = true;

                    $scope.chartData_2 = findBuySellPoint( $scope.tempSample_2.dailyStocks, $scope.tempSample_2.buyingDate, $scope.tempSample_2.sellingDate);

                    var containerId_2 = '#chartContainer_2';

                    drawCharts( containerId_2, $scope.chartData_2, $scope.scopeSelectName_2 );

                }, function ( error ) {
                    // error
                    alert ( 'no data from back end: ' + error);
                })
        };
        // end of stock controller
    }]);
