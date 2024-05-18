import { Component, OnInit } from '@angular/core';
import { RegisterService } from './register.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  userData: any = {};

  constructor(private registerService: RegisterService, private router: Router) { }

  ngOnInit() {
  }

  registerUser() {
    this.registerService.register(this.userData).subscribe(
      (response) => {
        console.log('Inscription réussie :', response);
        Swal.fire({
          icon: 'success',
          title: 'Inscription réussie',
          text: 'Vous allez être redirigé vers la page de connexion',
          timer: 1,
          timerProgressBar: true
        }).then((result) => {
          if (result.dismiss === Swal.DismissReason.timer) {
            console.log('La notification a été fermée, redirection vers la page de connexion.');
            this.redirectToLogin(); // Redirection vers la page de connexion
          }
        });
      },
      (error) => {
        console.error('Erreur lors de l\'inscription :', error);
        // Gérer l'erreur ici (affichage d'un message d'erreur, etc.).
      }
    );
  }

  redirectToLogin() {
    console.log('Redirection vers la page de connexion...');
    this.router.navigate(['/login']);
  }
}
