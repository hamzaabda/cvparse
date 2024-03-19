import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {
  email: string = '';
  password: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    // Initialisations si nécessaire
  }

  ngOnDestroy() {
    // Nettoyage des abonnements ou d'autres tâches de nettoyage
  }

  loginUser() {
    const userData = {
      email: this.email,
      password: this.password
    };

    this.authService.loginUser(userData).subscribe(
      (response) => {
        console.log('Connexion réussie :', response);
        // Redirection vers le tableau de bord après une connexion réussie
        this.router.navigate(['/dashboard']);
      },
      (error) => {
        console.error('Erreur lors de la connexion :', error);
        // Gérer les erreurs de connexion ici, par exemple, afficher un message d'erreur à l'utilisateur
        Swal.fire({
          icon: 'error',
          title: 'Erreur de connexion',
          text: 'Mot de passe incorrect.',
          confirmButtonText: 'Réinitialiser le mot de passe',
          showCancelButton: true,
          cancelButtonText: 'Fermer'
        }).then((result) => {
          if (result.isConfirmed) {
            this.resetPassword();
          }
        });
      }
    );
  }

  resetPassword() {
    this.authService.resetPassword(this.email).subscribe(
      (response) => {
        console.log('Réinitialisation de mot de passe réussie :', response);
        // Afficher un message à l'utilisateur indiquant que la demande de réinitialisation de mot de passe a été envoyée
        Swal.fire({
          icon: 'success',
          title: 'Demande de réinitialisation envoyée',
          text: response // Afficher le message de réinitialisation tel quel
        });
      },
      (error) => {
        console.error('Erreur lors de la réinitialisation du mot de passe :', error);
        // Gérer les erreurs de réinitialisation du mot de passe ici
        Swal.fire({
          icon: 'error',
          title: 'Erreur lors de la réinitialisation du mot de passe',
          text: 'Une erreur est survenue lors de la réinitialisation de votre mot de passe. Veuillez réessayer plus tard.'
        });
      }
    );
  }
}
