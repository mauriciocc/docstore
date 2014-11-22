angular.module("docstore.notifications", [])
    .factory("Notifications", ["$http", function ($http) {
        return {
            findAll: function () {
                return $http.get("/api/notifications");
            },
            markAsRead: function(id) {
                return $http.post("/api/notifications/"+id+"/mark-as-read");
            }
        };
    }])
    .controller("NotificationsCtrl", function ($scope, Notifications) {
        $scope.notifications = [];
        $scope.newCount = 0;

        $scope.refresh = function () {
            Notifications.findAll().success(function (data) {
                $scope.newCount = 0;
                data.forEach(function (item) {
                    if(!item.readAt) {
                        $scope.newCount++;
                    }
                });
                $scope.notifications = data;
            });
        };
        $scope.markAsRead = function (val) {
            Notifications.markAsRead(val.id).success(function () {
                $scope.refresh();
            });
        };

        $scope.refresh();
    })
;