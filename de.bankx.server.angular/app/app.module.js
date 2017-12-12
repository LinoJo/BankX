"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var platform_browser_1 = require("@angular/platform-browser");
var forms_1 = require("@angular/forms");
var http_1 = require("@angular/http");
var account_component_1 = require("./account.component");
var transaction_component_1 = require("./transaction.component");
var newaccount_component_1 = require("./newaccount.component");
var account_data_detail_component_1 = require("./account-data-detail.component");
var AppModule = (function () {
    function AppModule() {
    }
    AppModule = __decorate([
        core_1.NgModule({
            imports: [platform_browser_1.BrowserModule, forms_1.FormsModule, http_1.HttpModule],
            declarations: [account_component_1.AccountComponent, transaction_component_1.TransactionComponent, newaccount_component_1.NewAccountComponent, account_data_detail_component_1.AccountDataDetailComponent],
            bootstrap: [account_component_1.AccountComponent, transaction_component_1.TransactionComponent, newaccount_component_1.NewAccountComponent, account_data_detail_component_1.AccountDataDetailComponent]
        })
    ], AppModule);
    return AppModule;
}());
exports.AppModule = AppModule;
//# sourceMappingURL=app.module.js.map