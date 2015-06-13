(function() {
	var mobility_services = angular.module('mob-serv-directives', []);
	mobility_services.directive('travelForm', function() {
		return {
			restrict : 'E',
			templateUrl : './res/sections/travel-form.html'
		};
	});
	mobility_services.directive('rentalCarPlan', function() {
		return {
			restrict : 'E',
			templateUrl : './res/sections/rental-car-plan.html'
		};
	});
	mobility_services.directive('trainPlan', function() {
		return {
			restrict : 'E',
			templateUrl : './res/sections/train-plan.html'
		};
	});
})();