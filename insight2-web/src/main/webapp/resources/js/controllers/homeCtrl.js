/**
 * Created by Ken on 2/11/2015.
 */
angular.module('app')
    .controller('homeCtrl', ['$scope', '$http', function ( $scope, $http ) {

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
    }]);