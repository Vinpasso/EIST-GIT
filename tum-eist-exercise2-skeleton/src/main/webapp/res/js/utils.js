(function() {
  var mobility_services = angular.module('mob-serv-utils', []);
  mobility_services.service('Utils', function($rootScope) {
	  return {
	  checkRentalCarOption : function(carTrip) {
			  if (carTrip.Success) {
					$rootScope.carTrip = carTrip;
					$rootScope.showRentalCarTripPlan = true;
				} else {
					$rootScope.error = $rootScope.error+carTrip.Error + ". ";
					$rootScope.showError = true;
				}
	  },
	  checkTrainOption: function(trainTrip){
		  if (trainTrip.Success) {
				$rootScope.trainTrip = trainTrip;
				$rootScope.showTrainTripPlan = true;
			} else {
				$rootScope.error = $rootScope.error+trainTrip.Error + ". ";
				$rootScope.showError = true;
			}
	  },
	  recommendOption: function(recommendedOption){
		    if (recommendedOption == "Train") {
				$rootScope.recommend.trainTrip = "(Recommended)";
				$rootScope.recommend.trainBGColor = "#E7FFEB";
			} else if (recommendedOption == "Car") {
				$rootScope.recommend.carTrip = "(Recommended)";
				$rootScope.recommend.carBGColor = "#E7FFEB";
			}
	  }
	};
  });
})();