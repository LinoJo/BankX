import {Injectable} from "@angular/core";
import {Http, Headers, Response} from "@angular/http";
import "rxjs/add/operator/map";

@Injectable()
export class DataService {
  constructor(private http: Http) {}

  getData() {
      return this.http.get('http://localhost:9998/rest/data').map(
        (response: Response) => response.json()
      );
  }

  postData(data: RestData) {
      let headers = new Headers();
      headers.append('Content-Type', 'application/x-www-form-urlencoded');
      this.http.post('http://localhost:9998/rest/data', `info=$(data.info)`,
      {headers : headers});
  }
}
