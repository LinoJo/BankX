import {Component} from '@angular/core';
import {DataService} from "./dataservice";
import {AccountData} from "./account-data.model"

@Component({
    templateUrl: 'views/newaccount.html',
    selector: 'account-new',
    providers: [DataService]
})

export class NewAccountComponent {
  //account: any = {};

  account = new AccountData("","","","")
  submitted = false;
  startguthaben: number


  onSubmit() {
    this.submitted = true;
    console.log(JSON.stringify(this.account))
    this.dataService.postAccount(this.account, this.startguthaben).subscribe(() => console.log("finished"));
    this.newAccount();
  }

  newAccount() {
  this.account = new AccountData("","","","");
}

  constructor(private dataService:DataService){}

  get diagnostic() { return JSON.stringify(this.account); }
}
