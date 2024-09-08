import { Component, OnInit } from '@angular/core';
import { StageService } from './stage.service';
import { OffreStage } from './offre-stage.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-stage-management',
  templateUrl: './stage-management.component.html',
  styleUrls: ['./stage-management.component.scss']
})
export class StageManagementComponent implements OnInit {

  offresStage: OffreStage[] = [];
  selectedFile: File | null = null;

  constructor(private stageService: StageService) { }

  ngOnInit(): void {
    this.loadOffresStage();
  }

  loadOffresStage(): void {
    this.stageService.getAllOffresStage().subscribe((data: OffreStage[]) => {
      this.offresStage = data;
    });
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
    console.log('Fichier sélectionné:', this.selectedFile);
  }

  onPostuler(offreStageId: number): void {
    if (this.selectedFile) {
      this.stageService.uploadCV(this.selectedFile, offreStageId).subscribe({
        next: (response) => {
          Swal.fire({
            title: 'Success',
            text: 'Your application has been submitted successfully.',
            icon: 'success',
            confirmButtonText: 'OK'
          }).then(() => {
            this.offresStage = this.offresStage.filter(offre => offre.id !== offreStageId);
          });
        },
        error: (error) => {
          console.error('Erreur:', error);
          Swal.fire({
            title: 'Error',
            text: error.error || 'Failed to submit your application. Please try again.',
            icon: 'error',
            confirmButtonText: 'OK'
          });
        }
      });
    } else {
      Swal.fire({
        title: 'Warning',
        text: 'Please select a file before submitting.',
        icon: 'warning',
        confirmButtonText: 'OK'
      });
    }
  }
}
