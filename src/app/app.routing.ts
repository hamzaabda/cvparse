import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { Routes, RouterModule } from '@angular/router';

import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';
import { StageManagementComponent } from './pages/stage-management/stage-management.component';
import { ReclamationComponent } from './pages/reclamation/reclamation.component';
import { ReclamationManagementComponent } from './pages/reclamation-management/reclamation-management.component';
import { BlogComponent } from './pages/blog/blog.component';
import { EmploisManagementComponent } from './pages/emplois-management/emplois-management.component'; 
import { FeedbackManagementComponent } from './pages/feedback-management/feedback-management.component';


const routes: Routes = [
  {
    path: '',
    redirectTo: 'stage-management',
    pathMatch: 'full',
  },
  {
    path: 'stage-management',
    component: StageManagementComponent,
  },
  {
    path: 'reclamation',
    component: ReclamationComponent,
  },
  {
    path: 'reclamation-management',
    component: ReclamationManagementComponent,
  },
  {
    path: 'blog',
    component: BlogComponent,
  },
  {
    path: 'emplois-management',
    component: EmploisManagementComponent,
  },
  {
    path: 'feedback-management',
    component: FeedbackManagementComponent, // Nouvelle route pour FeedbackManagementComponent
  },
  
  {
    path: '',
    component: AdminLayoutComponent,
    children: [
      {
        path: '',
        loadChildren: () => import('src/app/layouts/admin-layout/admin-layout.module').then(m => m.AdminLayoutModule)
      }
    ]
  },
  {
    path: '',
    component: AuthLayoutComponent,
    children: [
      {
        path: '',
        loadChildren: () => import('src/app/layouts/auth-layout/auth-layout.module').then(m => m.AuthLayoutModule)
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'stage-management'
  }
  
];

@NgModule({
  imports: [
    CommonModule,
    BrowserModule,
    RouterModule.forRoot(routes, {
      useHash: true
    })
  ],
  exports: [],
})
export class AppRoutingModule { }
