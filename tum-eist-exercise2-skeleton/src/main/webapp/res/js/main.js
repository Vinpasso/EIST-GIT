(function() {
  var mobility_services = angular.module('Mobility Services', [
   'mob-serv-directives', 'mob-serv-utils']);
  mobility_services.controller('travelController', function($rootScope, $scope, $http, Utils) {
	  $rootScope.seatOptions =[
	    { value: "1", id: 1 },   
	    { value: "2", id: 2 },
	    { value: "3", id: 3 },
	    { value: "4", id: 4 }
	  ];
	  $rootScope.selectedOption = $rootScope.seatOptions[0];
	  $rootScope.recommend = {};
		seatOptions = [];
		this.findTravelOptions = function() {
			$rootScope.showRentalCarTripPlan = false;
			$rootScope.showTrainTripPlan = false;
			$scope.showResult = false;
			$rootScope.showError = false;
			$rootScope.recommend.trainTrip ="";
			$rootScope.recommend.carTrip ="";
			$rootScope.recommend.carBGColor="#FFFFFF";
			$rootScope.recommend.trainBGColor="#FFFFFF";
			$rootScope.carTrip = {};
			$rootScope.trainTrip = {};
			$rootScope.error = "";
			$http(
					{
						url : '/findMyOptions/'
								+ $scope.userClass + '/'
								+ $scope.sourceLat + '/'
								+ $scope.sourceLong + '/'
								+ $scope.destLat + '/'
								+ $scope.destLong + '/'
								+ $scope.selectedOption.value,
						method : "GET",
						headers : {
							'Content-Type' : 'application/json',
							'Accept' : 'application/json'
						}
					}).success(function(data, status, headers, config) {
				if (data.Success) {
					$scope.distance = data.distance;
					if(typeof data.carTrip !== 'undefined')
					  Utils.checkRentalCarOption(data.carTrip);
					if(typeof data.trainTrip !== 'undefined')
					  Utils.checkTrainOption(data.trainTrip);
					Utils.recommendOption(data.RecommendedOption);
					$scope.showResult = true;
				} else {
					if(typeof data.Error !== 'undefined')
						$rootScope.error = data.Error + ". ";
					if(typeof data.trainTrip !== 'undefined')
						$rootScope.error = $rootScope.error+data.trainTrip.Error + ". ";
					if(typeof data.carTrip !== 'undefined')
						$rootScope.error = $rootScope.error+data.carTrip.Error + ". ";
					$rootScope.showError = true;
				}
			}).error(function(data, status, headers, config) {
				$rootScope.error = "Probably a time-out issue. Please try again.";
				$rootScope.showError = true;
			});
		}
	});
})();