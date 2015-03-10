/**
 * Created by Ken on 2/11/2015.
 */
angular.module('app')
    .controller('homeCtrl', ['$scope', '$http', 'GetLiveStockService', function ( $scope, $http, GetLiveStockService ) {

        var testService = GetLiveStockService.strTest( 'homepage' );
        console.log ( testService );
        // init variable
        $scope.tempSample = [];
        $scope.tableFlag = false;
        $scope.scopeSelectName;  // stock name
        $scope.scopeSelectStockSeq; // stock seq

        $scope.hideTable = function() {
            $scope.tableFlag = false;
        };

        $scope.getSampleData = function() {
            GetLiveStockService.getStockByNameSeq( $scope.scopeSelectName, $scope.scopeSelectStockSeq )
                .then ( function ( returned ) {
                    // success
                    if(returned.data.length == 0) {
                        alert("returned empty data");
                    }
                    $scope.tempSample = [];
                    $scope.tempSample = returned.data;
                    $scope.tableFlag = true;
                }, function ( error ) {
                    // error
                    alert('no data come back');
            });
        };
        // end of home controller
    }]);