import { Component, OnInit } from '@angular/core';
import { AuthService } from './auth.service';
import Swal from 'sweetalert2';
import { Chart } from 'chart.js';

@Component({
  selector: 'app-tables',
  templateUrl: './tables.component.html',
  styleUrls: ['./tables.component.scss']
})
export class TablesComponent implements OnInit {
  newRecruiter: any = {};
  recruiters: any[] = [];
  filteredRecruiters: any;
  searchEmail: string = '';

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.getAllRecruiters(); 
    this.generateRecruitersChart(); 
    // Appel de la méthode pour récupérer les recruteurs lors de l'initialisation
  }

  onCreateRecruiter() {
    this.authService.createRecruiter(this.newRecruiter).subscribe(
      response => {
        console.log('Recruiter created:', response);
        this.recruiters.push(response); // Ajouter le nouveau recruteur à la liste
        this.newRecruiter = {}; // Réinitialiser les données du formulaire
        Swal.fire('Success', 'Recruiter created successfully!', 'success');
      },
      error => {
        console.error('Error creating recruiter:', error);
        Swal.fire('Error', 'Failed to create recruiter', 'error');
      }
    );
  }

  onUpdateRecruiter(recruiterId: number) {
    const updatedRecruiterData = {
      username: this.newRecruiter.username,
      password: this.newRecruiter.password,
      email: this.newRecruiter.email
    };

    this.authService.updateRecruiter(recruiterId, updatedRecruiterData).subscribe(
      response => {
        console.log('Recruiter updated successfully:', response);
        this.getAllRecruiters(); // Refresh the list of recruiters after the update
      },
      error => {
        console.error('Error updating recruiter:', error);
        Swal.fire('Error', 'Failed to update recruiter', 'error');
      }
    );
  }

  onDeleteRecruiter(recruiterId: number) {
    this.authService.deleteRecruiter(recruiterId).subscribe(
      response => {
        console.log('Recruiter deleted:', response);
        this.getAllRecruiters(); // Rafraîchir la liste des recruteurs après la suppression
        Swal.fire('Success', 'Recruiter deleted successfully!', 'success');
      },
      error => {
        console.error('Error deleting recruiter:', error);
        Swal.fire('Error', 'Failed to delete recruiter', 'error');
      }
    );
  }

  getAllRecruiters() {
    this.authService.getAllRecruiters().subscribe(
      response => {
        this.recruiters = response;
      },
      error => {
        console.error('Error fetching recruiters:', error);
        Swal.fire('Error', 'Failed to fetch recruiters', 'error');
      }
    );
  }

  // Méthode pour récupérer les données d'un recruteur spécifique pour la mise à jour
  getRecruiter(recruiterId: number) {
    // Utiliser le service pour récupérer les données du recruteur spécifique
    this.authService.getRecruiterById(recruiterId).subscribe(
      response => {
        this.newRecruiter = response; // Assigner les données récupérées au nouveau recruteur
        // Appeler la méthode onUpdateRecruiter avec les données récupérées
        this.onUpdateRecruiter(recruiterId);
      },
      error => {
        console.error('Error fetching recruiter details:', error);
        Swal.fire('Error', 'Failed to fetch recruiter details', 'error');
      }
    );
  }

  searchRecruiterByEmail(email: string) {
    this.authService.searchRecruiterByEmail(email).subscribe(
      (recruiters) => {
        console.log(recruiters); // Faites quelque chose avec les données des recruteurs retournées
        // Afficher une alerte si les recruteurs sont trouvés avec succès
        if (recruiters.length > 0) {
          Swal.fire('Success', 'Recruiters found!', 'success');
        } else {
          Swal.fire('Info', 'No recruiters found!', 'info');
        }
      },
      (error) => {
        console.error(error); // Gérez les erreurs ici
        Swal.fire('Error', 'Failed to search for recruiters', 'error');
      }
    );
  }

  onSearch() {
    const searchEmailLower = this.searchEmail.toLowerCase();
    const searchEmailUpper = this.searchEmail.toUpperCase();
  
    this.filteredRecruiters = this.recruiters.filter(recruiter => {
      const email = recruiter.email?.toLowerCase();
      return email?.includes(searchEmailLower) || email?.includes(searchEmailUpper);
    });
  }
  generateRecruitersChart() {
    const recruitersCount = this.recruiters.length;
  
    const ctx = document.getElementById('recruitersChart');
    if (!ctx) return; // Vérifiez si l'élément canvas est présent
  
    new Chart(ctx, {
      type: 'bar',
      data: {
        labels: ['Recruiters'],
        datasets: [{
          label: 'Number of Recruiters',
          data: [recruitersCount],
          backgroundColor: 'rgba(75, 192, 192, 0.2)',
          borderColor: 'rgba(75, 192, 192, 1)',
          borderWidth: 1
        }]
      },
      options: {
        scales: {
          yAxes: [{
            ticks: {
              beginAtZero: true
            }
          }]
        }
      }
    });
  }
  
}
