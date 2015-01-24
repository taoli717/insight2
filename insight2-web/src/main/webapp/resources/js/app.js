angular.module('routerApp', ['ui.router'])
    .config(function($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise('/home');

        $stateProvider
            // Home state
            .state('home', {
                url: '/home',
                templateUrl: '/resources/views/home.html',
                controller: function($scope, $http) {
                    //$scope.tempSample = [{firstName: "John", lastName: "Ty"},{firstName: "Bob", lastName: "Thrs"}];
                    $scope.getSampleData = function() {
                        $http.get('api/sample')
                            .then(function(res) {
                                $scope.tempSample = res.data;
                            });
                    };
                }
            })
            // Stock state
            .state('stock', {
                url: '/stock',
                templateUrl: '/resources/views/stock.html'
            });
    });