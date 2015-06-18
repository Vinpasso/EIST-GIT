(function() {
	var mobility_services = angular.module('mob-serv-travel-controller', []);
	mobility_services.controller('travelForm', function($rootScope, $scope, $http,
			sharedVariables, Utils) {
		$scope.nonStop=false;
		$scope.refundable=false;
		var recommendDefaults = {
			carBGColor : "#FFFFFF",
			trainBGColor : "#FFFFFF",
			flightBGColor : "#FFFFFF",
			carTrip : "",
			trainTrip : "",
			flightTrip : ""
		};
		seatOptions = [];
		this.findTravelOptions = function() {
			$rootScope.showRentalCarTripPlan = false;
			$rootScope.showTrainTripPlan = false;
			$rootScope.showFlightTripPlan = false;
			$scope.showResult = false;
			$rootScope.showError = false;
			$rootScope.recommend.trainTrip ="";
			$rootScope.recommend.carTrip ="";
			$rootScope.recommend.flightTrip ="";
			$rootScope.recommend.carBGColor="#FFFFFF";
			$rootScope.recommend.trainBGColor="#FFFFFF";
			$rootScope.recommend.flightBGColor="#FFFFFF";
			$rootScope.carTrip = {};
			$rootScope.trainTrip = {};
			$rootScope.flightTrip = {};
			$rootScope.error = "";
			$http(
					{
						url : '/findMyOptions/'
								+ sharedVariables.getUserClass() + '/'
								+ $scope.sourceStr + ' ' + $scope.sourceCity
								+ '/' + $scope.destStr + ' ' + $scope.destCity
								+ '/' + $scope.selectedOption.value + '/'
								// getting flightDate by "getElementById"
								// coz it gives back date in YYYY-MM-DD format.
								// Using $scope would complicate things: By
								// default AngularJS doesn't give date in
								// YYYY-MM-DD format
								+ document.getElementById('flightDate').value
								+ '/' + $scope.nonStop + '/'
								+ $scope.refundable,
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
					if(typeof data.flightTrip !== 'undefined')
					  Utils.checkFlightOption(data.flightTrip);
					Utils.recommendOption(data.RecommendedOption);
					$scope.showResult = true;
				} else {
					if(typeof data.Error !== 'undefined')
						$rootScope.error = data.Error + ". ";
					if(typeof data.flightTrip !== 'undefined')
						$rootScope.error = $rootScope.error+data.flightTrip.Error + ". ";
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