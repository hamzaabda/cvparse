import { Component } from '@angular/core';
import { FeedbackService } from '../feedback-management/feedback.service';
import { Feedback } from '../feedback-management/feedback.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-feedback-management',
  templateUrl: './feedback-management.component.html',
  styleUrls: ['./feedback-management.component.scss']
})
export class FeedbackManagementComponent {
  feedback: Feedback = { commentaire: '', rating: 0, date: new Date().toISOString().split('T')[0] };

  constructor(private feedbackService: FeedbackService) { }

  createFeedback(): void {
    this.feedbackService.createFeedback(this.feedback).subscribe({
      next: (result) => {
        Swal.fire({
          icon: 'success',
          title: 'Feedback créé',
          text: 'Votre feedback a été ajouté avec succès!',
          confirmButtonText: 'OK'
        }).then(() => {
          // Rafraîchir la page après la confirmation de SweetAlert
          window.location.reload();
        });
      },
      error: (err) => {
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'Une erreur est survenue lors de la création du feedback.',
          confirmButtonText: 'OK'
        });
      }
    });
  }
}
