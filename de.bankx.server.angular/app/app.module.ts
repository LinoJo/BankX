import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }   from '@angular/forms';
import { HttpModule }    from '@angular/http';

import { AccountComponent }  from './account.component';
import { TransactionComponent }  from './transaction.component';

@NgModule({
  imports: [ BrowserModule, FormsModule, HttpModule ],
  declarations: [ AccountComponent, TransactionComponent],
  bootstrap: [ AccountComponent, TransactionComponent ]
})
export class AppModule { }
