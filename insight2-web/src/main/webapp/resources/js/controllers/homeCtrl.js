/**
 * Created by Ken on 2/11/2015.
 */
angular.module('app')
    .controller('homeCtrl', ['$scope', '$http', 'GetLiveStockService', function ( $scope, $http, GetLiveStockService ) {

        var testService = GetLiveStockService.strTest( 'homepage' );
        console.log ( testService );
        // init variable
        $scope.tableFlag = false;
        $scope.scopeSelectName = 'AA';  // stock name
        $scope.scopeSelectStockSeq = 0; // stock seq

        $scope.hideTable = function() {
            $scope.tableFlag = false;
        };

        $scope.getSampleData = function() {
            GetLiveStockService.getStockByNameSeq( $scope.scopeSelectName, $scope.scopeSelectStockSeq )
                .then ( function ( returned ) {
                    // success
                    $scope.tempSample = returned.data;
                    $scope.tableFlag = true;
                }, function ( error ) {
                    // error
                    alert('no data come back');
            });
        };
        // end of home controller
    }]);