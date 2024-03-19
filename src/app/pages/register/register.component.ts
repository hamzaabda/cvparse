import { Component, OnInit } from '@angular/core';
import { RegisterService } from './register.service'; // Assurez-vous que le chemin vers votre service est correct

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  userData: any = {}; // Créez un objet pour stocker les données utilisateur

  constructor(private registerService: RegisterService) { }

  ngOnInit() {
  }

  registerUser() {
    // Appelez la méthode register du service et passez les données utilisateur
    this.registerService.register(this.userData).subscribe(
      (response) => {
        console.log('Inscription réussie :', response);
        // Traitez la réponse ou redirigez l'utilisateur vers une autre page.
      },
      (error) => {
        console.error('Erreur lors de l\'inscription :', error);
        // Gérez l'erreur (affichage d'un message d'erreur, etc.).
      }
    );
  }
}
