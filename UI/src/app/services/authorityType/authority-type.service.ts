import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthorityTypeResponse } from '../../models/authorityType/AuthorityTypeResponse';
import { AuthorityTypeRequest } from '../../models/authorityType/AuthorityTypeRequest';
import { RoleResponse } from '../../models/role/RoleResponse';

@Injectable({
    providedIn: 'root'
})
export class AuthorityTypeService {
    private apiUrl = 'http://localhost:8089/api/v1/admin/authorityType';

    constructor(private http: HttpClient) {}

    getAuthorityTypes(): Observable<AuthorityTypeResponse[]> {
        return this.http.get<AuthorityTypeResponse[]>(this.apiUrl);
    }

    createAuthorityType(authorityType: AuthorityTypeRequest): Observable<AuthorityTypeResponse> {
        return this.http.post<AuthorityTypeResponse>(this.apiUrl, authorityType);
    }

    deleteAuthorityType(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    getAuthorityTypeById(id: number): Observable<AuthorityTypeResponse> {
        return this.http.get<AuthorityTypeResponse>(`${this.apiUrl}/${id}`);
    }

    updateAuthorityType(id: number, authorityType: AuthorityTypeRequest): Observable<AuthorityTypeResponse> {
        return this.http.put<AuthorityTypeResponse>(`${this.apiUrl}/${id}`, authorityType);
    }
    enableAuthorityType(id: number): Observable<AuthorityTypeResponse> {
        return this.http.put<AuthorityTypeResponse>(`${this.apiUrl}/enable/${id}`, null);
    }

    disableAuthorityType(id: number): Observable<AuthorityTypeResponse> {
        return this.http.put<AuthorityTypeResponse>(`${this.apiUrl}/disable/${id}`, null);
    }
}
