import './polyfills';

import {HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatNativeDateModule} from '@angular/material/core';
import {BrowserModule} from '@angular/platform-browser';
import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {DemoMaterialModule} from './material-module';
import {RouterModule, Routes} from '@angular/router';

import {UserComponent} from './user/user.component';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {AppComponent} from "./app.component";
import {AddFolderDialog, FiletreeComponent} from './filetree/filetree.component';

const appRoutes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'user', component: UserComponent}
];

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    DemoMaterialModule,
    MatNativeDateModule,
    ReactiveFormsModule,
    RouterModule.forRoot(appRoutes)
  ],
  entryComponents: [AppComponent, AddFolderDialog],
  declarations: [AppComponent, UserComponent, LoginComponent, RegisterComponent, FiletreeComponent, AddFolderDialog],
  bootstrap: [AppComponent],
  providers: []
})
export class AppModule {}

platformBrowserDynamic().bootstrapModule(AppModule);
