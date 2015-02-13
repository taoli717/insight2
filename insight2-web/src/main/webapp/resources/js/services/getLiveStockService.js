/**
 * Created by Ken on 2/12/2015.
 */
angular.module( 'app' )
.factory ( 'GetLiveStockService', ['$http', '$q', function ( $http, $q ) {
    var service = {
        // test the service
        strTest : function ( strTest ) {

            var strRetCode = strTest + " get live stock service success.";

            return (strRetCode);
        },
        // get stock data
        getStockByNameSeq : function ( name, seq ) {

            var myDeferred = $q.defer();

            var url = '/api/rawPattern/' + name + '/' + seq;

            $http
            .get ( url )
            .then ( function ( returned ) {
                // success
                return myDeferred.resolve ( returned );
            },
            function ( error ) {
                // error
                return myDeferred.reject( error );
            });

            return myDeferred.promise;
        }
    };
    return (service);
}]);
