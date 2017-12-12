import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }   from '@angular/forms';
import { HttpModule }    from '@angular/http';

import { AccountComponent }  from './account.component';
import { TransactionComponent }  from './transaction.component';
import { NewAccountComponent } from './newaccount.component';
import { AccountDataDetailComponent } from './account-data-detail.component';

@NgModule({
  imports: [ BrowserModule, FormsModule, HttpModule ],
  declarations: [ AccountComponent, TransactionComponent, NewAccountComponent, AccountDataDetailComponent],
  bootstrap: [ AccountComponent, TransactionComponent, NewAccountComponent, AccountDataDetailComponent ]
})
export class AppModule { }
