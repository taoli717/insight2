/**
 * Created by Ken on 2/11/2015.
 */
angular.module( 'app' )
    .controller( 'devPageCtrl', [ '$scope', function( $scope ) {
        $scope.chartData = [
            {
                x: 330332400000,
                open: 3.703,
                high: 3.742,
                low: 3.672,
                close: 3.703
            },
            {
                x: 330591600000,
                open: 3.719,
                high: 3.742,
                low: 3.695,
                close: 3.719,
                color: 'blue',
                marker: {
                    fillColor: 'blue',
                    lineWidth: 3,
                    lineColor: "blue" // inherit from series
                }
            },
            {
                x: 331196400000,
                open: 3.672,
                high: 3.688,
                low: 3.633,
                close: 3.672
            },
            {
                x: 331282800000,
                open: 3.711,
                high: 3.727,
                low: 3.664,
                close: 3.711,
                color: 'orange',
                marker: {
                    fillColor: 'orange',
                    lineWidth: 3,
                    lineColor: "orange" // inherit from series
                }
            },
            {
                x: 331369200000,
                open: 3.727,
                high: 3.734,
                low: 3.703,
                close: 3.727
            }
        ];
/*        $scope.chartData = [
            [330332400000, 3.703, 3.742, 3.672, 3.703],
            {
                x: 330591600000,
                open: 3.719,
                high: 3.742,
                low: 3.695,
                close: 3.719,
                color: 'blue',
                marker: {
                    fillColor: 'blue',
                    lineWidth: 3,
                    lineColor: "blue" // inherit from series
                }
            },
            [331196400000, 3.672, 3.688, 3.633, 3.672],
            {
                x: 331282800000,
                open: 3.711,
                high: 3.727,
                low: 3.664,
                close: 3.711,
                color: 'orange',
                marker: {
                    fillColor: 'orange',
                    lineWidth: 3,
                    lineColor: "orange" // inherit from series
                }
            },
            [331369200000, 3.727, 3.734, 3.703, 3.727],
            [331455600000, 3.711, 3.719, 3.695, 3.711]];*/
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
            }]
        });
        // dev page controller end
    }]);