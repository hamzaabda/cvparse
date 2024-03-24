import { Component, OnInit, ElementRef } from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import Swal from 'sweetalert2'; // Import de Sweet Alert
import { DeconnexionService } from './deconnexion.service';

// Définition des routes
const ROUTES = [
  { path: '/dashboard', title: 'Dashboard' },
  { path: '/user-profile', title: 'User Profile' },
  // Ajoutez d'autres routes ici si nécessaire
];

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  public focus;
  public listTitles: any[];
  public location: Location;
  public userToken: string; // Ajoutez une propriété pour stocker le token de l'utilisateur

  constructor(
    location: Location,
    private element: ElementRef,
    private router: Router,
    private deconnexionService: DeconnexionService
  ) {
    this.location = location;
  }

  ngOnInit() {
    this.listTitles = ROUTES; // Utilisez ROUTES ici
    this.userToken = localStorage.getItem('token'); // Récupérez le token de l'utilisateur depuis le stockage local
  }

  getTitle() {
    var titlee = this.location.prepareExternalUrl(this.location.path());
    if (titlee.charAt(0) === '#') {
      titlee = titlee.slice(1);
    }

    for (var item = 0; item < this.listTitles.length; item++) {
      if (this.listTitles[item].path === titlee) {
        return this.listTitles[item].title;
      }
    }
    return 'Dashboard';
  }

  logout(): void {
    Swal.fire({
      title: 'Confirmation',
      text: 'Voulez-vous vraiment vous déconnecter ?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Oui',
      cancelButtonText: 'Non'
    }).then((result) => {
      if (result.isConfirmed) {
        // Déconnexion de l'utilisateur
        this.deconnexionService.deconnectAndRedirect();
      }
    });
  }
}
