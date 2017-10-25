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
var calc_service_1 = require("./calc.service");
var core_1 = require("@angular/core");
var AppComponent = (function () {
    function AppComponent(calcService) {
        this.calcService = calcService;
    }
    AppComponent.prototype.calcLocal = function () {
        this.result = String(Number(this.num1) + Number(this.num2));
        // this.result = "" + (+this.num1 + +this.num2);
    };
    AppComponent.prototype.calcRest = function () {
        var _this = this;
        this.calcService.calcNumbers(this.num1, this.num2).subscribe(function (result) { return _this.result = result; }, function (error) { return console.log(error); });
    };
    return AppComponent;
}());
AppComponent = __decorate([
    core_1.Component({
        selector: 'my-app',
        templateUrl: 'app/app.template.html',
        providers: [calc_service_1.CalcService]
    }),
    __metadata("design:paramtypes", [calc_service_1.CalcService])
], AppComponent);
exports.AppComponent = AppComponent;
//# sourceMappingURL=app.component.js.map