import {Component} from '@angular/core';
import {DataService} from "./dataservice";

@Component({
    templateUrl: 'views/account.html',
    selector: 'account-list',
    providers: [DataService]
})

export class AccountComponent {
  accountList: AccountData[] = [];

  constructor(private dataService : DataService){
    this.getData()
  }

  getData() {
    //this.dataList.push(this.dataService.getData())
    this.dataService.getAccounts().subscribe(
      (data: AccountData[]) => this.accountList=data,
      (error: Response) => console.log("Error: " + error.statusText),
      () => console.log("Anzahl Accounts: "+ this.accountList.length)
    )
  }
}