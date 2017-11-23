import {Component} from '@angular/core';
import {DataDetailComponent} from "./data-detail.component";
import {DataService} from "./data.service";

@Component({
    templateUrl: 'app/app.template.html',
    selector: 'my-app',
    providers: [DataService]
})

export class AppComponent {
  title: string = "RestDataViewer";
  selectedData: RestData;
  dataList: RestData[] = [];

  constructor(private dataService : DataService){}

  getData() {
    //this.dataList.push(this.dataService.getData())
    this.dataService.getData().subscribe(
      (data: RestData) => this.dataList.push(data),
      (error: Response) => console.log("Error: " + error.statusText),
      () => console.log("GetRequest sent")
    )
  }

  clearData() {
    this.dataList.length = 0;
  }

  onSelect(data: RestData){
    this.selectedData = data;
  }
}
