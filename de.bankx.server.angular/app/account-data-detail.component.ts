import {Component, Input} from "@angular/core";
import {DataService} from "./dataservice";
import {AccountData} from "./account-data.model"

@Component({
  selector: 'account-detail',
  templateUrl: 'views/accountdetail.html'
})
export class AccountDataDetailComponent{
  @Input() acc: AccountData;
  submitted = false;

  constructor(private dataService:DataService){}

  onSubmit(){
    this.submitted = true;
    console.log(JSON.stringify(this.acc))
    this.dataService.postEditAccount(this.acc).subscribe(() => console.log("edited"));
    window.location.reload();
  }
}
