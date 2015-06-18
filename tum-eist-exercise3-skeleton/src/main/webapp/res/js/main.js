(function() {
  var mobility_services = angular.module('Mobility Services', [
      'mob-serv-directives', 'mob-serv-initialize', 'mob-serv-utils', 
      'mob-serv-travel-controller', 'mob-serv-example-controller' ]);
  mobility_services.controller('accountController', function($rootScope, $scope, $http, sharedVariables) {
	$scope.status = "";
    this.addUser = function() {
      var dataObj = {
        email : $scope.user.email,
        firstName : $scope.user.firstname,
        lastName : $scope.user.lastname,
        street : $scope.user.street,
        pinCode : $scope.user.pincode,
        city : $scope.user.city,
        country : $scope.user.country,
        userClass : $scope.user.account_type
      };
      $http({
        url : '/user',
        method : "POST",
        data : dataObj,
        headers : {
          'Content-Type' : 'application/json',
          'Accept' : 'application/json'
        }
      }).success(function(data, status, headers, config) {
        location.reload(true);
      }).error(function(data, status, headers, config) {
        $scope.status = "Error occured. Please try again.";
      });

    };

  });
  mobility_services.controller('logoutURLController',
      function($scope, $http) {
        $scope.logoutURL = "";
        var APIcallURI = "/user/userLogout";
        $http.get(APIcallURI).success(
            function(data, status, headers, config) {
              $scope.logoutURL = data
            }).error(function(data, status, headers, config) {
          $scope.logoutURL = "#"
        });
      });
  mobility_services.controller('NavController', function() {
    this.tab = 1;
    this.selectTab = function(tabNumber) {
      this.tab = tabNumber;
    };
    this.isSelected = function(checkTab) {
      return this.tab === checkTab;
    };
  });
})();