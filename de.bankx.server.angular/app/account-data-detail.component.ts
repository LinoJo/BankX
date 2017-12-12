import {Component, Input} from "@angular/core";
import {DataService} from "./dataservice";
import {AccountData} from "./account-data.model"

@Component({
  selector: 'account-detail',
  templateUrl: 'views/accountdetail.html'
})
export class AccountDataDetailComponent{
  @Input() acc: AccountData;

  constructor(private dataService:DataService){}

  sendData(){
    console.log("Test");
  }
}
