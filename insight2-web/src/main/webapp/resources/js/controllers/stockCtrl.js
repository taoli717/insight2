/**
 * Created by Ken on 2/11/2015.
 */
angular.module( 'app' )
    .controller( 'stockCtrl', [ '$scope', '$http', function ( $scope, $http ) {

        $scope.chartData = [];

        $scope.tableFlag = false;

        $scope.scopeSelectName = 'AA';  // stock name

        $scope.scopeSelectStockSeq = 0; // stock seq

        $scope.scopeGetDataURL = "";

        $scope.hideTable = function() {
            $scope.tableFlag = false;
        };

        $scope.getSampleData = function() {

            $scope.chartData = [];

            $scope.scopeGetDataURL = '/api/rawPattern/' + $scope.scopeSelectName + '/' + $scope.scopeSelectStockSeq;

            $http
                //AA stock name, 5 pattern index - remove me
                .get( $scope.scopeGetDataURL )
                .then(function (res) {
                    $scope.tempSample = res.data;
                    $scope.tableFlag = true;
                    console.log($scope.tempSample.dailyStocks);
                    // TODO add highcharts here
                    angular.forEach($scope.tempSample.dailyStocks, function(item) {
                        $scope.temp = [];
                        $scope.temp.push(item.date);
                        $scope.temp.push(parseFloat(item.open));
                        $scope.temp.push(parseFloat(item.high));
                        $scope.temp.push(parseFloat(item.low));
                        $scope.temp.push(parseFloat(item.close));
                        console.log($scope.temp);
                        $scope.chartData.push($scope.temp);
                    });
                    console.log($scope.chartData);

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
                });
        };
        // end of stock controller
    }]);
