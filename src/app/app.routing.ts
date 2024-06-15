import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { Routes, RouterModule } from '@angular/router';

import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';
import { StageManagementComponent } from './pages/stage-management/stage-management.component'; // Chemin d'importation ajusté
import { ReclamationComponent } from './pages/reclamation/reclamation.component'; // Importation de ReclamationComponent
import { ReclamationManagementComponent } from './pages/reclamation-management/reclamation-management.component'; // Importation de ReclamationManagementComponent
import { BlogComponent } from './pages/blog/blog.component'; // Importation de BlogComponent

const routes: Routes = [
  {
    path: '',
    redirectTo: 'stage-management', // Rediriger vers le composant StageManagementComponent par défaut
    pathMatch: 'full',
  },
  {
    path: 'stage-management',
    component: StageManagementComponent, // Composant StageManagementComponent
  },
  {
    path: 'reclamation',
    component: ReclamationComponent, // Composant ReclamationComponent
  },
  {
    path: 'reclamation-management',
    component: ReclamationManagementComponent, // Composant ReclamationManagementComponent
  },
  {
    path: 'blog',
    component: BlogComponent, // Composant BlogComponent
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
    redirectTo: 'stage-management' // Redirection par défaut vers le composant StageManagementComponent pour les routes inconnues
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
  exports: [
  ],
})
export class AppRoutingModule { }
