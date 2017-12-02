import {Injectable} from "@angular/core";
import {Http, Headers, Response} from "@angular/http";
import "rxjs/add/operator/map";

@Injectable()
export class DataService {
  constructor(private http: Http){}

  getAccounts() {
    return this.http.get('http://localhost:9998/rest/admin/getAllAccounts').map(
      (response: Response) => response.json().items
    )
  }

  postAccount(data: AccountData) {
      let headers = new Headers();
      headers.append('Content-Type', 'application/x-www-form-urlencoded');
      this.http.post('http://localhost:9998/rest/admin/addAccount', `info=$(data.info)`,
      {headers : headers});
  }

  getTransactions(){
    return this.http.get('http://localhost:9998/rest/admin/getAllTransactions').map(
      (response: Response) => response.json().items
    )
  }
}
