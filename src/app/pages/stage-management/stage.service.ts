import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OffreStage } from './offre-stage.model';

@Injectable({
  providedIn: 'root'
})
export class StageService {

  private baseUrl = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) { }

  getAllOffresStage(): Observable<OffreStage[]> {
    return this.http.get<OffreStage[]>(`${this.baseUrl}/offres-stage`);
  }

  uploadCV(file: File, offreStageId: number): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);

    return this.http.post(`${this.baseUrl}/upload-cv/${offreStageId}`, formData, {
      headers: new HttpHeaders({
        'enctype': 'multipart/form-data'
      })
    });
  }
}
