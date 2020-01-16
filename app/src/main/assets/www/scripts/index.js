// For an introduction to the Blank template, see the following documentation:
// http://go.microsoft.com/fwlink/?LinkID=397704
// To debug code on page load in Ripple or on Android devices/emulators: launch your app, set breakpoints, 
// and then run "window.location.reload()" in the JavaScript Console.
(function () {
    "use strict";

    document.addEventListener('deviceready', onDeviceReady.bind(this), false);

    function onDeviceReady() {
        // Handle the Cordova pause and resume events
        document.addEventListener('pause', onPause.bind(this), false);
        document.addEventListener('resume', onResume.bind(this), false);

        // TODO: Cordova has been loaded. Perform any initialization that requires Cordova here.
    };

    function onPause() {
        // TODO: This application has been suspended. Save application state here.
    };

    function onResume() {
        // TODO: This application has been reactivated. Restore application state here.
    };

    var app = angular.module('finPinApp', ['ngMaterial', 'ngMessages'])

        .config(function ($compileProvider, $mdThemingProvider, $mdDateLocaleProvider) {
            $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|chrome-extension|ms-appx|file):/);

            //finpin theme
            $mdThemingProvider.theme('default').primaryPalette('amber').accentPalette('light-blue').warnPalette('red');

            //aditya theme
            //$mdThemingProvider.theme('default').primaryPalette('red').accentPalette('orange').warnPalette('red');

            $mdDateLocaleProvider.formatDate = function (date) {
                return moment(date).format('DD-MM-YYYY');
            };

            $mdDateLocaleProvider.parseDate = function (dateString) {
                var m = moment(dateString, 'DD-MM-YYYY', true);

                return m.isValid() ? m.toDate() : new Date(NaN);
            };

        })

        .run(['$anchorScroll', function ($anchorScroll) {
            $anchorScroll.yOffset = 50;   // always scroll by 50 extra pixels
        }])


    //==================================================================================
    //                                main controller
    //==================================================================================
    .controller('mainController', function ($rootScope, $scope, $mdDialog, $location, $timeout, $mdSidenav, $log, $http, $window, $filter, $anchorScroll) {
        //block direct access url for without login
        $rootScope.$on('$routeChangeStart', function () {
            $rootScope.loading = true;
        });

        $rootScope.$on('$routeChangeSuccess', function () {
            $rootScope.loading = false;
        });

        $scope.refreshPage = function(){
            $scope.canShowRunningCheckFlowCard = false;
            $scope.canShowRunStep1_15 = false;
            $scope.isLoadingRunStep1 = false;
            $scope.isLoadingRunStep1 = false;
            $scope.canShowRunCheckedStep1 = false;

            //step 2
            $scope.canShowRunStep2_15 = false;
            $scope.isLoadingRunStep2 = false;
            $scope.isLoadingRunStep2 = false;
            $scope.canShowRunCheckedStep2 = false;

            //step 3
            $scope.canShowRunStep3_15 = false;
            $scope.isLoadingRunStep3 = false;
            $scope.isLoadingRunStep3 = false;
            $scope.canShowRunCheckedStep3 = false;

            //step 4
            $scope.canShowRunStep4_15 = false;
            $scope.isLoadingRunStep4 = false;
            $scope.isLoadingRunStep4 = false;
            $scope.canShowRunCheckedStep4 = false;

            //step 5
            $scope.canShowRunStep5_15 = false;
            $scope.isLoadingRunStep5 = false;
            $scope.isLoadingRunStep5 = false;
            $scope.canShowRunCheckedStep5 = false;

            //step 6
            $scope.canShowRunStep6_15 = false;
            $scope.isLoadingRunStep6 = false;
            $scope.isLoadingRunStep6 = false;
            $scope.canShowRunCheckedStep6 = false;

            //step 7
            $scope.canShowRunStep7_15 = false;
            $scope.isLoadingRunStep7 = false;
            $scope.isLoadingRunStep7 = false;
            $scope.canShowRunCheckedStep7 = false;

            //step 8
            $scope.canShowRunStep8_15 = false;
            $scope.isLoadingRunStep8 = false;
            $scope.isLoadingRunStep8 = false;
            $scope.canShowRunCheckedStep8 = false;

            //step 9
            $scope.canShowRunStep9_15 = false;
            $scope.isLoadingRunStep9 = false;
            $scope.isLoadingRunStep9 = false;
            $scope.canShowRunCheckedStep9 = false;

            //step 10
            $scope.canShowRunStep10_15 = false;
            $scope.isLoadingRunStep10 = false;
            $scope.isLoadingRunStep10 = false;
            $scope.canShowRunCheckedStep10 = false;

            //step 11
            $scope.canShowRunStep11_15 = false;
            $scope.isLoadingRunStep11 = false;
            $scope.isLoadingRunStep11 = false;
            $scope.canShowRunCheckedStep11 = false;

            //step 12
            $scope.canShowRunStep12_15 = false;
            $scope.isLoadingRunStep12 = false;
            $scope.isLoadingRunStep12 = false;
            $scope.canShowRunCheckedStep12 = false;

            //step 13
            $scope.canShowRunStep13_15 = false;
            $scope.isLoadingRunStep13 = false;
            $scope.isLoadingRunStep13 = false;
            $scope.canShowRunAlertStep13 = false;

            //step 14
            $scope.canShowRunStep14_15 = false;
            $scope.isLoadingRunStep14 = false;
            $scope.isLoadingRunStep14 = false;
            $scope.canShowRunAlertStep14 = false;

            //step 15
            $scope.canShowRunStep15_15 = false;
            $scope.isLoadingRunStep15 = false;
            $scope.isLoadingRunStep15 = false;
            $scope.canShowRunAlertStep15 = false;

            //final button
            $scope.canShowFinishedRunCheck = false;

        };

        $scope.refreshPage();



         $scope.runCheckFlow = function () {

            $scope.canShowRunningCheckFlowCard = true;
            $timeout(function () {
                $anchorScroll('runningCheckFlow');
                $scope.canShowRunStep1_15 = true;
                $scope.isLoadingRunStep1 = true;
                $timeout(function () {
                    $anchorScroll('RunStep1_15');
                    $timeout(function () {
                        $scope.isLoadingRunStep1 = false;
                        $scope.canShowRunCheckedStep1 = true;

                        //step 2
                        $scope.canShowRunStep2_15 = true;
                        $scope.isLoadingRunStep2 = true;
                        $timeout(function () {
                            $anchorScroll('RunStep2_15');
                            $timeout(function () {
                                $scope.isLoadingRunStep2 = false;
                                $scope.canShowRunCheckedStep2 = true;

                                //step 3
                                $scope.canShowRunStep3_15 = true;
                                $scope.isLoadingRunStep3 = true;
                                $timeout(function () {
                                    $anchorScroll('RunStep3_15');
                                    $timeout(function () {
                                        $scope.isLoadingRunStep3 = false;
                                        $scope.canShowRunCheckedStep3 = true;

                                        //step 4
                                        $scope.canShowRunStep4_15 = true;
                                        $scope.isLoadingRunStep4 = true;
                                        $timeout(function () {
                                            $anchorScroll('RunStep4_15');
                                            $timeout(function () {
                                                $scope.isLoadingRunStep4 = false;
                                                $scope.canShowRunCheckedStep4 = true;

                                                //step 5
                                                $scope.canShowRunStep5_15 = true;
                                                $scope.isLoadingRunStep5 = true;
                                                $timeout(function () {
                                                    $anchorScroll('RunStep5_15');
                                                    $timeout(function () {
                                                        $scope.isLoadingRunStep5 = false;
                                                        $scope.canShowRunCheckedStep5 = true;

                                                        //step 6
                                                        $scope.canShowRunStep6_15 = true;
                                                        $scope.isLoadingRunStep6 = true;
                                                        $timeout(function () {
                                                            $anchorScroll('RunStep6_15');
                                                            $timeout(function () {
                                                                $scope.isLoadingRunStep6 = false;
                                                                $scope.canShowRunCheckedStep6 = true;

                                                                //step 7
                                                                $scope.canShowRunStep7_15 = true;
                                                                $scope.isLoadingRunStep7 = true;
                                                                $timeout(function () {
                                                                    $anchorScroll('RunStep7_15');
                                                                    $timeout(function () {
                                                                        $scope.isLoadingRunStep7 = false;
                                                                        $scope.canShowRunCheckedStep7 = true;

                                                                        //step 8
                                                                        $scope.canShowRunStep8_15 = true;
                                                                        $scope.isLoadingRunStep8 = true;
                                                                        $timeout(function () {
                                                                            $anchorScroll('RunStep8_15');
                                                                            $timeout(function () {
                                                                                $scope.isLoadingRunStep8 = false;
                                                                                $scope.canShowRunCheckedStep8 = true;

                                                                                //step 9
                                                                                $scope.canShowRunStep9_15 = true;
                                                                                $scope.isLoadingRunStep9 = true;
                                                                                $timeout(function () {
                                                                                    $anchorScroll('RunStep9_15');
                                                                                    $timeout(function () {
                                                                                        $scope.isLoadingRunStep9 = false;
                                                                                        $scope.canShowRunCheckedStep9 = true;

                                                                                        //step 10
                                                                                        $scope.canShowRunStep10_15 = true;
                                                                                        $scope.isLoadingRunStep10 = true;
                                                                                        $timeout(function () {
                                                                                            $anchorScroll('RunStep10_15');
                                                                                            $timeout(function () {
                                                                                                $scope.isLoadingRunStep10 = false;
                                                                                                $scope.canShowRunCheckedStep10 = true;

                                                                                                //step 11
                                                                                                $scope.canShowRunStep11_15 = true;
                                                                                                $scope.isLoadingRunStep11 = true;
                                                                                                $timeout(function () {
                                                                                                    $anchorScroll('RunStep11_15');
                                                                                                    $timeout(function () {
                                                                                                        $scope.isLoadingRunStep11 = false;
                                                                                                        $scope.canShowRunCheckedStep11 = true;

                                                                                                        //step 12
                                                                                                        $scope.canShowRunStep12_15 = true;
                                                                                                        $scope.isLoadingRunStep12 = true;
                                                                                                        $timeout(function () {
                                                                                                            $anchorScroll('RunStep12_15');
                                                                                                            $timeout(function () {
                                                                                                                $scope.isLoadingRunStep12 = false;
                                                                                                                $scope.canShowRunCheckedStep12 = true;

                                                                                                                //step 13
                                                                                                                $scope.canShowRunStep13_15 = true;
                                                                                                                $scope.isLoadingRunStep13 = true;
                                                                                                                $timeout(function () {
                                                                                                                    $anchorScroll('RunStep13_15');
                                                                                                                    $timeout(function () {
                                                                                                                        $scope.isLoadingRunStep13 = false;
                                                                                                                        $scope.canShowRunAlertStep13 = true;

                                                                                                                        //step 14
                                                                                                                        $scope.canShowRunStep14_15 = true;
                                                                                                                        $scope.isLoadingRunStep14 = true;
                                                                                                                        $timeout(function () {
                                                                                                                            $anchorScroll('RunStep14_15');
                                                                                                                            $timeout(function () {
                                                                                                                                $scope.isLoadingRunStep14 = false;
                                                                                                                                $scope.canShowRunAlertStep14 = true;

                                                                                                                                //step 15
                                                                                                                                $scope.canShowRunStep15_15 = true;
                                                                                                                                $scope.isLoadingRunStep15 = true;
                                                                                                                                $timeout(function () {
                                                                                                                                    $anchorScroll('RunStep15_15');
                                                                                                                                    $timeout(function () {
                                                                                                                                        $scope.isLoadingRunStep15 = false;
                                                                                                                                        $scope.canShowRunAlertStep15 = true;

                                                                                                                                        //final button
                                                                                                                                        $scope.canShowFinishedRunCheck = true;

                                                                                                                                        $timeout(function () {
                                                                                                                                            $anchorScroll('FinalRunCheckedButton');
                                                                                                                                        }, 200);

                                                                                                                                    }, 500);
                                                                                                                                }, 200);

                                                                                                                            }, 500);
                                                                                                                        }, 200);

                                                                                                                    }, 500);
                                                                                                                }, 200);

                                                                                                            }, 500);
                                                                                                        }, 200);

                                                                                                    }, 500);
                                                                                                }, 200);

                                                                                            }, 500);
                                                                                        }, 200);

                                                                                    }, 500);
                                                                                }, 200);

                                                                            }, 500);
                                                                        }, 200);

                                                                    }, 500);
                                                                }, 200);

                                                            }, 500);
                                                        }, 200);

                                                    }, 500);
                                                }, 200);

                                            }, 500);
                                        }, 200);

                                    }, 500);
                                }, 200);

                            }, 500);
                        }, 200);

                    }, 500);
                }, 200);

            }, 200);
        };

        var action = Android.getAction();

        if(action == 'Start'){
            $timeout(function(){
                $scope.runCheckFlow();
            },500);
        }

//        $scope.doProceedRunChecked = function(){
//            var action = 'Completed';
//
//            return action;
//        };


    })


    //end of main 
    ;

})();