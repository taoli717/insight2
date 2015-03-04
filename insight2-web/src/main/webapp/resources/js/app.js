angular.module('app', ['ui.router'])
.config([ '$stateProvider', '$urlRouterProvider', function( $stateProvider, $urlRouterProvider ) {
    $urlRouterProvider.otherwise( '/home' );

    $stateProvider
        // Home state
        .state('home', {
            url: '/home',
            templateUrl: '/resources/views/home.html',
            controller: 'homeCtrl'
        })
        .state('stock', {
            url: '/stock',
            templateUrl: '/resources/views/stock.html',
            controller: 'stockCtrl'
        })
        .state( 'devPage', {
            url: '/devPage',
            templateUrl: '/resources/views/devPage.html',
            controller: 'devPageCtrl'
        })
        .state('3DPage', {
            url: '/3DPage',
            templateUrl: '/resources/views/3DPage.html',
            controller: '3DPageCtrl'
        });
}]);