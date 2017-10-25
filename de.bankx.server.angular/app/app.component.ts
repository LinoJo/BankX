import {CalcService} from "./calc.service";
import {Component} from '@angular/core';
import {Response} from '@angular/http';

@Component({
    selector: 'my-app',
    templateUrl: 'app/app.template.html',
    providers: [CalcService]
})

export class AppComponent {
    num1: string;
    num2: string;
    result: string;

    constructor(private calcService: CalcService){}

    calcLocal() {
        this.result = String(Number(this.num1) + Number(this.num2));
        // this.result = "" + (+this.num1 + +this.num2);
    }

    calcRest() {
        this.calcService.calcNumbers(this.num1, this.num2).subscribe(
            result => this.result = result,
            error => console.log(error)
        );
    }

}
