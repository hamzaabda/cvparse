import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Feedback } from '../feedback-management/feedback.model';

@Injectable({
  providedIn: 'root',
})
export class FeedbackService {
  private apiUrl = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) {}

  // Get all feedbacks
  getAllFeedbacks(): Observable<Feedback[]> {
    return this.http.get<Feedback[]>(`${this.apiUrl}/feedbacks`);
  }

  // Get feedback by ID
  getFeedbackById(id: number): Observable<Feedback> {
    return this.http.get<Feedback>(`${this.apiUrl}/feedback/${id}`);
  }

  // Create new feedback
  createFeedback(feedback: Feedback): Observable<Feedback> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<Feedback>(`${this.apiUrl}/feedback`, feedback, { headers });
  }
}
