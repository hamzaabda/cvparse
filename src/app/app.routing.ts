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
import { ChatComponent } from './pages/chat/chat.component'; 
import { EntretienManagementComponent } from './pages/entretien-management/entretien-management.component'; // Importation du EntretienManagementComponent
import { FeedbackListComponent } from './pages/feedback-list/feedback-list.component';
import { DocumentManagementComponent } from './pages/document-management/document-management.component';


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
    component: FeedbackManagementComponent, // Route pour FeedbackManagementComponent
  },
  {
    path: 'chat',
    component: ChatComponent, // Route pour ChatComponent
  },

  { path: 'feedback-list', component: FeedbackListComponent },
  {
    path: 'entretien-management',
    component: EntretienManagementComponent, // Nouvelle route pour EntretienManagementComponent
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
    path: 'document-management',
    component: DocumentManagementComponent, // Nouvelle route pour DocumentManagementComponent
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
  exports: [RouterModule],
})
export class AppRoutingModule { }
