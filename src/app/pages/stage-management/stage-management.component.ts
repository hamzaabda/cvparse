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
            // Hide the offer from the list
            this.offresStage = this.offresStage.filter(offre => offre.id !== offreStageId);
          });
        },
        error: (error) => {
          if (error.status === 400 && error.error.includes('already applied')) {
            Swal.fire({
              title: 'Info',
              text: 'You have already applied for this offer.',
              icon: 'info',
              confirmButtonText: 'OK'
            });
          } else {
            Swal.fire({
              title: 'Error',
              text: 'Failed to submit your application. Please try again.',
              icon: 'error',
              confirmButtonText: 'OK'
            });
          }
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
