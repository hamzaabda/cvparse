import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppRoutingModule } from './app.routing';
import { ComponentsModule } from './components/components.module';
import { EmploisManagementComponent } from './pages/emplois-management/emplois-management.component';
import { StageManagementComponent } from './pages/stage-management/stage-management.component';
import { ReclamationManagementComponent } from './pages/reclamation-management/reclamation-management.component';
import { ReclamationComponent } from './pages/reclamation/reclamation.component';
import { BlogComponent } from './pages/blog/blog.component';
import { FeedbackManagementComponent } from './pages/feedback-management/feedback-management.component';


@NgModule({
  imports: [
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    ComponentsModule,
    NgbModule,
    RouterModule,
    AppRoutingModule
  ],
  declarations: [
    AppComponent,
    AdminLayoutComponent,
    AuthLayoutComponent,
    EmploisManagementComponent,
    StageManagementComponent,
    ReclamationManagementComponent,
    ReclamationComponent,
    BlogComponent,
    FeedbackManagementComponent,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
