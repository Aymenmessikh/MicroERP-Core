import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthorityResponse } from '../../models/authority/AuthorityResponse';
import { AuthorityRequest } from '../../models/authority/AuthorityRequest';
import { Page } from '../../models/Page';

@Injectable({
    providedIn: 'root'
})
export class AuthorityService {
    private apiUrl = 'http://localhost:8089/api/v1/admin/authority';

    constructor(private http: HttpClient) {}

    getAuthorities(
        page: number,
        size: number,
        searchExpr?: string,
        searchOperation?: string,
        searchValue?: string,
        filters?: {
            field: string;
            matchMode: string;
            value: string;
        }[]
    ): Observable<Page<AuthorityResponse>> {
        let params = new HttpParams().set('skip', page.toString()).set('take', size.toString());

        if (searchExpr && searchOperation && searchValue) {
            params = params.set('searchExpr', searchExpr).set('searchOperation', searchOperation).set('searchValue', searchValue);
        }

        // Ajouter les filtres directement sans les indexer
        if (filters && filters.length > 0) {
            const filterQuery = filters.map((filter) => `${filter.field},${filter.matchMode},${filter.value}`).join(','); // On utilise ',' pour séparer chaque filtre

            params = params.set('filter', filterQuery);
        }

        return this.http.get<Page<AuthorityResponse>>(this.apiUrl, { params });
    }

    createAuthority(authority: AuthorityRequest): Observable<AuthorityResponse> {
        return this.http.post<AuthorityResponse>(this.apiUrl, authority);
    }

    deleteAuthority(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    getAuthorityById(id: number): Observable<AuthorityResponse> {
        return this.http.get<AuthorityResponse>(`${this.apiUrl}/${id}`);
    }

    updateAuthority(id: number, authority: AuthorityRequest): Observable<AuthorityResponse> {
        return this.http.put<AuthorityResponse>(`${this.apiUrl}/${id}`, authority);
    }

    enableAuthority(id: number): Observable<AuthorityResponse> {
        return this.http.put<AuthorityResponse>(`${this.apiUrl}/enable/${id}`, null);
    }

    disableAuthority(id: number): Observable<AuthorityResponse> {
        return this.http.put<AuthorityResponse>(`${this.apiUrl}/disable/${id}`, null);
    }

    getModuleAuthoritiesExcludingRoleAuthorities(idRole: number, idModule: number): Observable<AuthorityResponse[]> {
        return this.http.get<AuthorityResponse[]>(`${this.apiUrl}/ModuleAuthoritiesExcludingRoleAuthorities/${idRole}/${idModule}`);
    }

    getModulesAuthoritiesExcludingProfileAuthorities(id: number): Observable<AuthorityResponse[]> {
        return this.http.get<AuthorityResponse[]>(`${this.apiUrl}/ModulesAuthoritiesExcludingProfileAuthorities/${id}`);
    }
}
