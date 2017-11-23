import {Component, Input} from "@angular/core";
import {DataService} from "./data.service";

@Component({
  selector: 'my-data-detail',
  templateUrl: 'app/data-detail.template.html'
})
export class DataDetailComponent{
  @Input() data: RestData;

  constructor(private dataService:DataService){}

  sendData(){
    console.log("Data: " + this.data.info);
  }
}
