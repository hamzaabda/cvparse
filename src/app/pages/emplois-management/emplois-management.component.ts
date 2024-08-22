import { Component, OnInit } from '@angular/core';
import { OffreEmploiService } from '../emplois-management/offre-emploi.service';
import { OffreEmploi } from '../emplois-management/offre-emploi.model';
import Swal from 'sweetalert2'; // SweetAlert for notification

@Component({
  selector: 'app-emplois-management',
  templateUrl: './emplois-management.component.html',
  styleUrls: ['./emplois-management.component.scss']
})
export class EmploisManagementComponent implements OnInit {
  offresEmploi: OffreEmploi[] = [];

  constructor(private offreEmploiService: OffreEmploiService) { }

  ngOnInit(): void {
    this.loadOffresEmploi();
  }

  // Load job offers
  loadOffresEmploi(): void {
    this.offreEmploiService.getAllOffresEmploi().subscribe((data: OffreEmploi[]) => {
      this.offresEmploi = data;
    });
  }

  apply(id: number, cvFile: File): void {
    if (!cvFile) {
      Swal.fire('Erreur', 'Veuillez sélectionner un fichier CV.', 'error');
      return;
    }

    this.offreEmploiService.applyToOffreEmploi(id, cvFile).subscribe(
      (response) => {
        Swal.fire('Succès', response, 'success');
        this.loadOffresEmploi(); // Refresh the list after a successful application
      },
      (error) => {
        Swal.fire('Erreur', error.error || 'Une erreur est survenue.', 'error');
      }
    );
  }

  onFileSelected(event: Event, offreId: number): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      // The file can be accessed via input.files[0]
    }
  }
}
