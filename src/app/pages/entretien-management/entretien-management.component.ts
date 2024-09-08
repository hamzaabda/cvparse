import { Component, OnInit } from '@angular/core';
import { EntretienManagementService } from './entretien-management.service';

@Component({
  selector: 'app-entretien-management',
  templateUrl: './entretien-management.component.html',
  styleUrls: ['./entretien-management.component.scss']
})
export class EntretienManagementComponent implements OnInit {

  acceptedEmails: string[] = [];
  message: string = '';

  constructor(private entretienService: EntretienManagementService) {}

  ngOnInit(): void {
    this.loadAcceptedEmails();
  }

  loadAcceptedEmails(): void {
    this.entretienService.getAcceptedCandidateEmails().subscribe(
      emails => this.acceptedEmails = emails,
      error => console.error('Erreur lors du chargement des emails acceptés', error)
    );
  }

  saveAcceptedEmails(): void {
    this.entretienService.saveAcceptedEmails().subscribe(
      response => this.message = response,
      error => console.error('Erreur lors de la sauvegarde des emails', error)
    );
  }

  acceptCandidate(email: string): void {
    this.entretienService.acceptCandidate(email).subscribe(
      response => this.message = response,
      error => console.error('Erreur lors de l\'acceptation du candidat', error)
    );
  }

  rejectCandidate(email: string): void {
    this.entretienService.rejectCandidate(email).subscribe(
      response => this.message = response,
      error => console.error('Erreur lors du refus du candidat', error)
    );
  }
}
