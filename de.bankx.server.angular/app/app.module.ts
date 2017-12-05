import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }   from '@angular/forms';
import { HttpModule }    from '@angular/http';

import { AccountComponent }  from './account.component';
import { TransactionComponent }  from './transaction.component';
import { NewAccountComponent } from './newaccount.component';

@NgModule({
  imports: [ BrowserModule, FormsModule, HttpModule ],
  declarations: [ AccountComponent, TransactionComponent, NewAccountComponent],
  bootstrap: [ AccountComponent, TransactionComponent, NewAccountComponent ]
})
export class AppModule { }
