import { Component, OnInit } from '@angular/core';
import { ReclamationService } from '../reclamation-management/reclamation.service';
import { Reclamation } from '../reclamation-management/reclamation.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-reclamation-management',
  templateUrl: './reclamation-management.component.html',
  styleUrls: ['./reclamation-management.component.scss'],
})
export class ReclamationManagementComponent implements OnInit {
  reclamations: Reclamation[] = [];
  selectedReclamation: Reclamation | null = null;
  responseMessage: string = '';

  constructor(private reclamationService: ReclamationService) {}

  ngOnInit(): void {
    this.loadReclamations();
  }

  loadReclamations(): void {
    this.reclamationService.getAllReclamations().subscribe(
      (data) => {
        this.reclamations = data;
      },
      (error) => {
        console.error('Error loading reclamations:', error);
      }
    );
  }

  selectReclamation(reclamation: Reclamation): void {
    this.selectedReclamation = reclamation;
  }

  sendReply(): void {
    if (this.selectedReclamation) {
      this.reclamationService.replyToReclamation(this.selectedReclamation.id, this.responseMessage).subscribe({
        next: (response) => {
          console.log('Response:', response);
          // Show success alert
          Swal.fire({
            title: 'Success',
            text: 'Response sent and reclamation will be deleted.',
            icon: 'success',
            confirmButtonText: 'OK'
          }).then(() => {
            // Call deleteReclamation after the user acknowledges the success alert
            this.deleteReclamation(this.selectedReclamation.id);
          });
        },
        error: (error) => {
          console.error('Error:', error);
          // Show error alert
          Swal.fire({
            title: 'Error',
            text: 'Failed to send response. Please try again.',
            icon: 'error',
            confirmButtonText: 'OK'
          });
        }
      });
    }
  }

  deleteReclamation(id: number): void {
    this.reclamationService.deleteReclamation(id).subscribe(
      () => {
        console.log('Reclamation deleted successfully');
        // Show success alert
        Swal.fire({
          title: 'Success',
          text: 'Reclamation deleted successfully.',
          icon: 'success',
          confirmButtonText: 'OK'
        }).then(() => {
          // Refresh the list of reclamations and clear the selected reclamation
          this.loadReclamations();
          this.selectedReclamation = null;
        });
      },
      (error) => {
        console.error('Error deleting reclamation:', error);
        // Show error alert
        Swal.fire({
          title: 'Error',
          text: 'Failed to delete reclamation. Please try again.',
          icon: 'error',
          confirmButtonText: 'OK'
        });
      }
    );
  }
}