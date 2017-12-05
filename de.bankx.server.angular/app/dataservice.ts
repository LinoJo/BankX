import {Injectable} from "@angular/core";
import {Http, Headers, Response} from "@angular/http";
import "rxjs/add/operator/map";
import {AccountData} from "./account-data.model";

@Injectable()
export class DataService {
  constructor(private http: Http){}

  getAccounts() {
    return this.http.get('http://localhost:9998/rest/admin/getAllAccounts').map(
      (response: Response) => response.json().items
    )
  }

  postAccount(data: AccountData, start: number) {

      let headers = new Headers();
      console.log("posting...");
      headers.append('Content-Type', 'application/x-www-form-urlencoded');
      return this.http.post('http://localhost:9998/rest/admin/addAccount', `post_owner=${data.owner}&post_amount=${start}`,
      {headers : headers}).map(response => response);
  }

  getTransactions(){
    return this.http.get('http://localhost:9998/rest/admin/getAllTransactions').map(
      (response: Response) => response.json().items
    )
  }
}
