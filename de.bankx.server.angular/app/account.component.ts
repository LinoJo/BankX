import {Component} from '@angular/core';
import {DataService} from "./dataservice";
import {AccountData} from "./account-data.model"

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
    this.dataService.getAccounts().subscribe(
      (data: AccountData[]) => this.accountList=data,
      (error: Response) => console.log("Error: " + error.statusText),
      () => console.log("Anzahl geladener Accounts: "+ this.accountList.length)
    )
  }
  onSelect(acc: AccountData){
    console.log(JSON.stringify(acc));
  }
}
