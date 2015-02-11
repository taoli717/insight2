angular.module('routerApp', ['ui.router'])
    .config(function($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise('/home');

        $stateProvider
            // Home state
            .state('home', {
                url: '/home',
                templateUrl: '/resources/views/home.html',
                controller: function($scope, $http) {

                    $scope.tableFlag = false;

                    $scope.scopeSelectName = 'AA';  // stock name

                    $scope.scopeSelectStockSeq = 0; // stock seq

                    $scope.scopeGetDataURL = "";

                    $scope.hideTable = function() {
                        $scope.tableFlag = false;
                    };

                    $scope.getSampleData = function() {

                        $scope.scopeGetDataURL = '/api/rawPattern/' + $scope.scopeSelectName + '/' + $scope.scopeSelectStockSeq;

                        //AA stock name, 5 pattern index - remove me
                        //$http.get('/api/rawPattern/AA/5')
                        $http.get( $scope.scopeGetDataURL )
                            .then(function(res) {
                                $scope.tempSample = res.data;
                                $scope.tableFlag = true;
/*                                $scope.dailyStockArray = [];
                                angular.forEach($scope.tempSample, function(item) {
                                    $scope.dailyStockArray.push(item);
                                });
                                console.log($scope.dailyStockArray);*/
                            });
                    };
                // end of home controller
                }
            })
            // Stock state
            .state('stock', {
                url: '/stock',
                templateUrl: '/resources/views/stock.html',
                controller: function($scope, $http) {

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
                }
            })
            // dev page start
            .state( 'devPage', {
                url: '/devPage',
                templateUrl: '/resources/views/devPage.html',
                controller: function($scope) {
                    $scope.chartData = [[329641200000, 3.906, 3.953, 3.875, 3.906],
                        [329727600000, 3.867, 3.938, 3.836, 3.867],
                        [329986800000, 3.813, 3.836, 3.805, 3.813],
                        [330073200000, 3.852, 3.875, 3.828, 3.852],
                        [330159600000, 3.774, 3.844, 3.734, 3.774],
                        [330246000000, 3.649, 3.781, 3.649, 3.649],
                        [330332400000, 3.703, 3.742, 3.672, 3.703],
                        [330591600000, 3.719, 3.742, 3.695, 3.719],
                        [330678000000, 3.734, 3.734, 3.695, 3.734],
                        [330764400000, 3.75, 3.758, 3.734, 3.75],
                        [330850800000, 3.703, 3.766, 3.703, 3.703],
                        [330937200000, 3.695, 3.727, 3.656, 3.695],
                        [331196400000, 3.672, 3.688, 3.633, 3.672],
                        [331282800000, 3.711, 3.727, 3.664, 3.711],
                        [331369200000, 3.727, 3.734, 3.703, 3.727],
                        [331455600000, 3.711, 3.719, 3.695, 3.711],
                        [331801200000, 3.711, 3.711, 3.695, 3.711],
                        [331887600000, 3.734, 3.734, 3.711, 3.734],
                        [331974000000, 3.734, 3.774, 3.734, 3.734],
                        [332060400000, 3.672, 3.734, 3.672, 3.672],
                        [332146800000, 3.703, 3.703, 3.672, 3.703],
                        [332406000000, 3.766, 3.766, 3.703, 3.766],
                        [332492400000, 3.797, 3.828, 3.789, 3.797],
                        [332578800000, 3.813, 3.828, 3.789, 3.813],
                        [332665200000, 3.906, 3.906, 3.789, 3.906],
                        [332751600000, 4.094, 4.094, 4, 4.094],
                        [333010800000, 4.172, 4.172, 4.063, 4.172],
                        [333097200000, 4.18, 4.234, 4.172, 4.18],
                        [333183600000, 4.195, 4.266, 4.172, 4.195],
                        [333270000000, 4.188, 4.195, 4.149, 4.188],
                        [333356400000, 4.164, 4.188, 4.109, 4.164],
                        [333615600000, 4.219, 4.227, 4.109, 4.219],
                        [333702000000, 4.266, 4.266, 4.219, 4.266],
                        [333788400000, 4.281, 4.297, 4.266, 4.281],
                        [333874800000, 4.313, 4.313, 4.195, 4.313],
                        [333961200000, 4.289, 4.32, 4.281, 4.289],
                        [334220400000, 4.25, 4.305, 4.234, 4.25],
                        [334306800000, 4.219, 4.25, 4.195, 4.219],
                        [334393200000, 4.289, 4.289, 4.195, 4.289],
                        [334479600000, 4.297, 4.305, 4.25, 4.297],
                        [334566000000, 4.344, 4.391, 4.328, 4.344],
                        [334825200000, 4.375, 4.375, 4.352, 4.375],
                        [334911600000, 4.297, 4.359, 4.266, 4.297],
                        [334998000000, 4.258, 4.305, 4.234, 4.258],
                        [335084400000, 4.328, 4.328, 4.258, 4.328],
                        [335170800000, 4.313, 4.328, 4.274, 4.313],
                        [335430000000, 4.289, 4.32, 4.266, 4.289],
                        [335516400000, 4.234, 4.266, 4.234, 4.234],
                        [335602800000, 4.234, 4.242, 4.219, 4.234],
                        [335689200000, 4.266, 4.274, 4.242, 4.266],
                        [335775600000, 4.297, 4.328, 4.266, 4.297],
                        [336034800000, 4.313, 4.313, 4.266, 4.313],
                        [336121200000, 4.344, 4.367, 4.344, 4.344],
                        [336207600000, 4.281, 4.32, 4.281, 4.281],
                        [336294000000, 4.234, 4.274, 4.234, 4.234],
                        [336380400000, 4.234, 4.25, 4.227, 4.234],
                        [336726000000, 4.32, 4.32, 4.227, 4.32],
                        [336812400000, 4.336, 4.336, 4.313, 4.336],
                        [336898800000, 4.32, 4.375, 4.313, 4.32],
                        [336985200000, 4.25, 4.274, 4.234, 4.25],
                        [337244400000, 4.203, 4.289, 4.203, 4.203],
                        [337330800000, 4.188, 4.188, 4.094, 4.188],
                        [337417200000, 4.188, 4.219, 4.164, 4.188],
                        [337503600000, 4.203, 4.211, 4.188, 4.203],
                        [337590000000, 4.25, 4.25, 4.211, 4.25],
                        [337849200000, 4.274, 4.274, 4.242, 4.274],
                        [337935600000, 4.469, 4.469, 4.375, 4.469],
                        [338022000000, 4.524, 4.578, 4.461, 4.524],
                        [338108400000, 4.57, 4.578, 4.469, 4.57],
                        [338194800000, 4.641, 4.664, 4.547, 4.641],
                        [338454000000, 4.734, 4.734, 4.594, 4.734],
                        [338540400000, 4.672, 4.734, 4.656, 4.672],
                        [338626800000, 4.641, 4.711, 4.625, 4.641],
                        [338713200000, 4.649, 4.656, 4.609, 4.649],
                        [338799600000, 4.539, 4.625, 4.516, 4.539],
                        [339058800000, 4.484, 4.539, 4.406, 4.484],
                        [339145200000, 4.531, 4.531, 4.484, 4.531],
                        [339231600000, 4.469, 4.547, 4.406, 4.469],
                        [339318000000, 4.43, 4.578, 4.414, 4.43],
                        [339404400000, 4.445, 4.484, 4.414, 4.445],
                        [339663600000, 4.547, 4.547, 4.453, 4.547],
                        [339750000000, 4.531, 4.641, 4.524, 4.531],
                        [339836400000, 4.445, 4.531, 4.438, 4.445],
                        [339922800000, 4.516, 4.531, 4.453, 4.516],
                        [340009200000, 4.484, 4.563, 4.484, 4.484],
                        [340268400000, 4.555, 4.555, 4.469, 4.555],
                        [340354800000, 4.609, 4.641, 4.578, 4.609],
                        [340441200000, 4.625, 4.641, 4.609, 4.625],
                        [340527600000, 4.555, 4.774, 4.555, 4.555],
                        [340614000000, 4.375, 4.539, 4.328, 4.375]];
                    // Draw High charts
                    $('#chartContainerDevPage').highcharts('StockChart', {
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
                            name : 'stockFFFFF',
                            data : $scope.chartData,
                            dataGrouping: {
                                enabled: false
                            }
                        },
                        {
                            name : 'Buy point',
                            data: [[330246000000, 3.649, 3.781, 3.649, 3.649]],
                            marker: {
                                lineColor: '#00FF00',
                                lineWidth: 5,
                                color: '#00FF00',
                                radius: 4
                            },
                            dataGrouping: {
                                enabled: false
                            }
                        },
                        {
                            name : 'Sell point',
                            data: [[340268400000, 4.555, 4.555, 4.469, 4.555]],
                            marker: {
                                lineColor: '#00FF00',
                                lineWidth: 5,
                                color: '#FF0000',
                                radius: 4
                            },
                            dataGrouping: {
                                enabled: false
                            }
                        }]
                    });
                }
            })
            // dev page end
            ;
    });