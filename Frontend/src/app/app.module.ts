import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { HttpClientModule } from '@angular/common/http';
import { MatIconModule } from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoreModule } from './core/core.module';
import { LayoutModule } from './layout/layout.module';
import { SharedModule } from './shared/shared.module';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    MatIconModule,

    CoreModule, //  Singleton services, interceptors, auth guard, etc.
    SharedModule, //  Common components, directives, pipes used everywhere
    LayoutModule,
    BrowserAnimationsModule, //  Navbar, Sidebar, AppShell — visible throughout app
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
