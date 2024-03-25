import { Component, OnInit } from '@angular/core';
import { AuthService } from '../tables/auth.service'; // Assurez-vous de mettre le bon chemin vers votre service AuthService

@Component({
  selector: 'app-tables',
  templateUrl: './tables.component.html',
  styleUrls: ['./tables.component.scss']
})
export class TablesComponent implements OnInit {
  newRecruiter: any = {}; // Utilisation de 'any' pour représenter l'entité Recruiter
  
  recruiters: any[] = []; // Déclaration de la propriété recruiters pour stocker la liste des recruteurs

  constructor(private authService: AuthService) { }

  ngOnInit() {
    // Vous pouvez récupérer la liste des recruteurs existants ici si nécessaire
  }

  onCreateRecruiter() {
    this.authService.createRecruiter(this.newRecruiter).subscribe(
      response => {
        // Réussi, réinitialiser le formulaire si nécessaire
        console.log('Recruiter created:', response);
        this.newRecruiter = {}; // Réinitialiser les données du formulaire
      },
      error => {
        // Gérer les erreurs
        console.error('Error creating recruiter:', error);
      }
    );
  }
  onUpdateRecruiter(recruiterId: number) {
    // Vous devez fournir les données du recruteur à mettre à jour
    // Par exemple, vous pouvez stocker ces données dans une propriété de votre composant ou les récupérer à partir d'une source externe
    const updatedRecruiterData = {
      // Données du recruteur à mettre à jour
    };

    this.authService.updateRecruiter(recruiterId, updatedRecruiterData).subscribe(
      response => {
        console.log('Recruiter updated:', response);
        // Gérer la réponse si nécessaire
      },
      error => {
        console.error('Error updating recruiter:', error);
        // Gérer les erreurs
      }
    );
  }

  onDeleteRecruiter(recruiterId: number) {
    this.authService.deleteRecruiter(recruiterId).subscribe(
      response => {
        console.log('Recruiter deleted:', response);
        // Gérer la réponse si nécessaire
      },
      error => {
        console.error('Error deleting recruiter:', error);
        // Gérer les erreurs
      }
    );
  }

  
}
