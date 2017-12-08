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
var account_data_model_1 = require("./account-data.model");
var NewAccountComponent = (function () {
    function NewAccountComponent(dataService) {
        this.dataService = dataService;
        //account: any = {};
        this.account = new account_data_model_1.AccountData("", "");
        this.submitted = false;
    }
    NewAccountComponent.prototype.onSubmit = function () {
        this.submitted = true;
        console.log(JSON.stringify(this.account));
        this.dataService.postAccount(this.account, this.startguthaben).subscribe(function () { return console.log("finished"); });
        this.newAccount();
    };
    NewAccountComponent.prototype.newAccount = function () {
        this.account = new account_data_model_1.AccountData("", "");
    };
    Object.defineProperty(NewAccountComponent.prototype, "diagnostic", {
        get: function () { return JSON.stringify(this.account); },
        enumerable: true,
        configurable: true
    });
    NewAccountComponent = __decorate([
        core_1.Component({
            templateUrl: 'views/newaccount.html',
            selector: 'account-new',
            providers: [dataservice_1.DataService]
        }),
        __metadata("design:paramtypes", [dataservice_1.DataService])
    ], NewAccountComponent);
    return NewAccountComponent;
}());
exports.NewAccountComponent = NewAccountComponent;
//# sourceMappingURL=newaccount.component.js.map