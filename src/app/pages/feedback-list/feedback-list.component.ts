import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Feedback } from './feedback.model';

@Component({
  selector: 'app-feedback-list',
  templateUrl: './feedback-list.component.html',
  styleUrls: ['./feedback-list.component.scss']
})
export class FeedbackListComponent implements OnInit {

  feedbacks: Feedback[] = [];

  constructor(private http: HttpClient) {}

  // Cette méthode fait l'appel HTTP pour récupérer tous les feedbacks
  getAllFeedbacks(): Observable<Feedback[]> {
    const url = 'http://localhost:8080/auth/feedbacks';  // URL de votre contrôleur Spring Boot
    return this.http.get<Feedback[]>(url);
  }

  ngOnInit(): void {
    this.getAllFeedbacks().subscribe(
      (data: Feedback[]) => {
        this.feedbacks = data;
      },
      (error) => {
        console.error('Erreur lors de la récupération des feedbacks', error);
      }
    );
  }
}
