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

        var testService = GetLiveStockService.strTest( 'stock page' );
        console.log ( testService );

        $scope.hideTable = function() {
            $scope.tableFlag = false;
        };

        $scope.getSampleData = function() {

            $scope.chartData = [];

            GetLiveStockService.getStockByNameSeq( $scope.scopeSelectName, $scope.scopeSelectStockSeq )
                .then( function ( res ) {
                    $scope.tempSample = res.data;
                    $scope.tableFlag = true;
                    $scope.tempBuyingDate = $scope.tempSample.buyingDate;
                    $scope.tempSellingDate = $scope.tempSample.sellingDate;

                    angular.forEach($scope.tempSample.dailyStocks, function(item) {
                        if( item.date == $scope.tempBuyingDate ) {
                            // buy point, give it blue color
                            $scope.temp = {
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
                        } else if ( item.date == $scope.tempSellingDate ) {
                            // sell point, give it orange color
                            $scope.temp = {
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
                            $scope.temp = {
                                x: item.date,
                                open: parseFloat( item.open ),
                                high: parseFloat( item.high ),
                                low: parseFloat( item.low ),
                                close: parseFloat( item.close )
                            };
                        }
                        $scope.chartData.push($scope.temp);
                    });

                    // Draw High charts
                    $('#chartContainer').highcharts('StockChart', {
                        chart : {
                            type: 'candlestick',
                            zoomType: 'x'
                        },
                        navigator : {
                            adaptToUpdatedData: false,
                            series : {
                                data : $scope.chartData
                            }
                        },
                        scrollbar: {
                            liveRedraw: false
                        },
                        title: {
                            text: $scope.scopeSelectName + ' stock history'
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
                            /*                                    events : {
                             afterSetExtremes : afterSetExtremes
                             },
                             minRange: 3600 * 1000 // one hour*/
                        },
                        yAxis: {
                            floor: 0
                        },
                        series : [{
                            data : $scope.chartData,
                            dataGrouping: {
                                enabled: false
                            }
                        }]
                    });
                }, function ( error ) {
                    // error
                    alert ( 'no data from back end: ' + error);
            });
        };
        // end of stock controller
    }]);
