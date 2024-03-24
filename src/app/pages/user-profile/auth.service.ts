import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators'; // Import de l'opérateur tap

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) { }

  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { email, password });
  }

  addAdmin(token: string, adminData: any): Observable<any> {
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.post<any>(`${this.apiUrl}/admin/add`, adminData, { headers });
  }

  getAdmins(token: string): Observable<any> {
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.get<any>(`${this.apiUrl}/admin`, { headers });
  }

  updateAdmin(token: string, adminId: number, adminData: any): Observable<any> {
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.put<any>(`${this.apiUrl}/admin/update/${adminId}`, adminData, { headers });
  }

  deleteAdmin(token: string, adminId: number): Observable<any> {
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.delete<any>(`${this.apiUrl}/admin/delete/${adminId}`, { headers });
  }

  getUsersCount(token: string): Observable<number> {
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.get<number>(`${this.apiUrl}/users/count`, { headers })
      .pipe(
        tap(usersCount => console.log('Total users count:', usersCount))
      );
  }
}
