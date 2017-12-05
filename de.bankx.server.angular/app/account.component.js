"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var dataservice_1 = require("./dataservice");
var AccountComponent = (function () {
    function AccountComponent(dataService) {
        this.dataService = dataService;
        this.accountList = [];
        this.getData();
    }
    AccountComponent.prototype.getData = function () {
        var _this = this;
        this.dataService.getAccounts().subscribe(function (data) { return _this.accountList = data; }, function (error) { return console.log("Error: " + error.statusText); }, function () { return console.log("Anzahl geladener Accounts: " + _this.accountList.length); });
    };
    AccountComponent.prototype.onSelect = function (acc) {
        console.log(JSON.stringify(acc));
    };
    AccountComponent = __decorate([
        core_1.Component({
            templateUrl: 'views/account.html',
            selector: 'account-list',
            providers: [dataservice_1.DataService]
        }),
        __metadata("design:paramtypes", [dataservice_1.DataService])
    ], AccountComponent);
    return AccountComponent;
}());
exports.AccountComponent = AccountComponent;
//# sourceMappingURL=account.component.js.map