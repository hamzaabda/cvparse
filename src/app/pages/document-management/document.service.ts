import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DocumentService {
  private apiUrl = 'http://localhost:8080/auth/documents';

  constructor(private http: HttpClient) {}

  // Upload a document
  uploadDocument(formData: FormData): Observable<any> {
    return this.http.post(this.apiUrl, formData);
  }

  // Get a document by ID
  getDocument(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`, { responseType: 'blob' });
  }

  // Get all documents
  getAllDocuments(): Observable<any> {
    return this.http.get(this.apiUrl);
  }

  // Delete a document by ID
  deleteDocument(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
