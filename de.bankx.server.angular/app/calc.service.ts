import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import 'rxjs/add/operator/map';

@Injectable()
export class CalcService {
    constructor(private http: Http) { }

    calcNumbers(num1: string, num2: string) {
        return this.http.get(`http://localhost:9998/rest/add/${num1}/${num2}`).map((response: Response) => response.text());
    }
}
