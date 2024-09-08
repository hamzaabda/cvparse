import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OffreStage } from './offre-stage.model';

@Injectable({
  providedIn: 'root'
})
export class StageService {

  private baseUrl = 'http://localhost:8080/auth'; // Assurez-vous que l'URL est correcte

  constructor(private http: HttpClient) { }

  getAllOffresStage(): Observable<OffreStage[]> {
    return this.http.get<OffreStage[]>(`${this.baseUrl}/offres-stage`);
  }

  uploadCV(file: File, offreStageId: number): Observable<string> {
    const formData: FormData = new FormData();
    formData.append('file', file);
  
    return this.http.post<string>(`${this.baseUrl}/upload-cv/${offreStageId}`, formData, {
      responseType: 'text' as 'json' // Spécifiez que la réponse est du texte
    });
  }
  
}
