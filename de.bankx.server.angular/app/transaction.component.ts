import {Component} from '@angular/core';
import {DataService} from "./dataservice";
import {TransactionData} from "./transaction-data.model"

@Component({
    templateUrl: 'views/transaction.html',
    selector: 'transact-list',
    providers: [DataService]
})

export class TransactionComponent {
  transactionList: TransactionData[] = [];

  constructor(private dataService : DataService){
    this.getData()
  }

  getData() {
    this.dataService.getTransactions().subscribe(
      (data: TransactionData[]) => this.transactionList=data,
      (error: Response) => console.log("Error: " + error.statusText),
      () => console.log("Anzahl geladener Transaktionen: " + this.transactionList.length)
    )
  }
}
