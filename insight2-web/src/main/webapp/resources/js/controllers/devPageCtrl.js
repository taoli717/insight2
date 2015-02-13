/**
 * Created by Ken on 2/11/2015.
 */
angular.module( 'app' )
    .controller( 'devPageCtrl', [ '$scope','GetLiveStockService', function( $scope, GetLiveStockService ) {
        $scope.testService = function () {
            var testString = GetLiveStockService.strTest( 'in devpage');
            alert(testString);
        };

        var name = "AA";
        var seq = 0;
        $scope.gotData = null;

        GetLiveStockService
        .getStockByNameSeq( name, seq )
        .then ( function ( returned ) {
            // success
            $scope.gotData = returned.data;
        }, function ( error ) {
            // error
        })
    }]);