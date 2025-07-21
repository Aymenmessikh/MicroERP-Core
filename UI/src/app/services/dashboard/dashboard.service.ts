import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
    private apiUrl = 'http://localhost:8089/api/v1/admin';
  constructor(private http: HttpClient) { }
    getAuthorityTypeCount(): Observable<number> {
        return this.http.get<number>(`${this.apiUrl}/authorityType/count`);
    }
    getAuthorityCount(): Observable<number> {
        return this.http.get<number>(`${this.apiUrl}/authority/count`);
    }

    // Get count of authorities by module ID
    getAuthorityCountByModule(moduleId: number): Observable<number> {
        return this.http.get<number>(`${this.apiUrl}/authority/countByModule`, {
            params: { id: moduleId }
        });
    }
    getRoleCount(): Observable<number> {
        return this.http.get<number>(`${this.apiUrl}/role/count`);
    }

    // Get count of role by module ID
    getRoleCountByModule(moduleId: number): Observable<number> {
        return this.http.get<number>(`${this.apiUrl}/role/countByModule`, {
            params: { id: moduleId }
        });
    }
    getModuleCount(): Observable<number> {
        return this.http.get<number>(`${this.apiUrl}/module/count`);
    }
    getGroupeCount(): Observable<number> {
        return this.http.get<number>(`${this.apiUrl}/groupe/count`);
    }
    getUserCount(): Observable<number> {
        return this.http.get<number>(`${this.apiUrl}/user/count`);
    }
    getUserActifCount(): Observable<number> {
        return this.http.get<number>(`${this.apiUrl}/user/countAcifUser`);
    }
    getProfileCount(): Observable<number> {
        return this.http.get<number>(`${this.apiUrl}/profile/count`);
    }
}
