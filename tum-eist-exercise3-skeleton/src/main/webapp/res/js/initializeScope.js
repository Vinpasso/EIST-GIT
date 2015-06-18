(function() {
  var mobility_services = angular.module('mob-serv-initialize', []);
  mobility_services.service('sharedVariables', function($http,$rootScope) {
    var userClass;
    var APIcallURI = "/user/info";
    $rootScope.showProfileInfo = false;
    $rootScope.welcome = "Please wait... while we get your user info";
    $http.get(APIcallURI).success(function(data, status, headers, config) {
      $rootScope.welcome = "Welcome! ";
      $rootScope.userId = data.userId;
      $rootScope.firstname = data.firstName;
      $rootScope.lastname = data.lastName;
      $rootScope.street = data.street;
      $rootScope.pincode = data.pinCode;
      $rootScope.city = data.city;
      $rootScope.country = data.country;
      $rootScope.userClass = data.userClass;
      $rootScope.email = data.email;
      $rootScope.showProfileInfo = true;
      userClass = data.userClass;
      // Set variables here to pre-fill forms with userData
      $rootScope.user= {
        firstname: data.firstName,
    	lastname: data.lastName,
    	street: data.street,
    	pincode: data.pinCode,
    	city: data.city,
    	country: data.country,
    	email: data.email,
    	account_type: userClass
      }; 
      $rootScope.sourceStr = data.street;
      $rootScope.sourceCity = data.city;
      if (userClass == "Family") {
    	$rootScope.seatOptions =[
    	{ value: "2", id: 1 },
    	{ value: "3", id: 2 },
    	{ value: "4", id: 3 }
    	];
      }else{
    	$rootScope.seatOptions =[{ value: "1", id: 1 }];
      }
      $rootScope.selectedOption = $rootScope.seatOptions[0];
    }).error(function(data, status, headers, config) {
    	$rootScope.welcome = "You don't have an account on our app. Please go to Setting->Account Settings to create one."
    });
    return {
      getUserClass : function() {
        return userClass;
      }
    };
  });
})();