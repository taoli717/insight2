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
                    $scope.getSampleData = function() {
                        $http.get('api/sample')
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
                }
            })
            // Stock state
            .state('stock', {
                url: '/stock',
                templateUrl: '/resources/views/stock.html',
                controller: function($scope, $http) {
                    $scope.getSampleData = function() {
                        $http
                        .get('api/sample')
                        .then(function (res) {
                            $scope.tempSample = res.data;
                            console.log($scope.tempSample);
                            // TODO add highcharts here
                        });
                    };
                }
            });
    });