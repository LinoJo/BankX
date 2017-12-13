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
var AccountDataDetailComponent = (function () {
    function AccountDataDetailComponent(dataService) {
        this.dataService = dataService;
        this.transactionList = [];
        this.submitted = false;
    }
    AccountDataDetailComponent.prototype.onSubmit = function () {
        this.submitted = true;
        console.log(JSON.stringify(this.acc));
        this.dataService.postEditAccount(this.acc).subscribe(function () { return console.log("edited"); });
        window.location.reload();
    };
    __decorate([
        core_1.Input(),
        __metadata("design:type", account_data_model_1.AccountData)
    ], AccountDataDetailComponent.prototype, "acc", void 0);
    AccountDataDetailComponent = __decorate([
        core_1.Component({
            selector: 'account-detail',
            templateUrl: 'views/accountdetail.html'
        }),
        __metadata("design:paramtypes", [dataservice_1.DataService])
    ], AccountDataDetailComponent);
    return AccountDataDetailComponent;
}());
exports.AccountDataDetailComponent = AccountDataDetailComponent;
//# sourceMappingURL=account-data-detail.component.js.map