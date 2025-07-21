import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RoleResponse } from '../../models/role/RoleResponse';
import { RoleRequest } from '../../models/role/RoleRequest';
import { Page } from '../../models/Page';

@Injectable({
    providedIn: 'root'
})
export class RolesService {
    private apiUrl = 'http://localhost:8089/api/v1/admin/role';

    constructor(private http: HttpClient) {}

    getRoles(
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
    ): Observable<Page<RoleResponse>> {
        let params = new HttpParams().set('skip', page.toString()).set('take', size.toString());

        if (searchExpr && searchOperation && searchValue) {
            params = params.set('searchExpr', searchExpr).set('searchOperation', searchOperation).set('searchValue', searchValue);
        }

        // Ajouter les filtres directement sans les indexer
        if (filters && filters.length > 0) {
            const filterQuery = filters.map((filter) => `${filter.field},${filter.matchMode},${filter.value}`).join(','); // On utilise ',' pour séparer chaque filtre

            params = params.set('filter', filterQuery);
        }

        return this.http.get<Page<RoleResponse>>(this.apiUrl, { params });
    }

    createRole(role: RoleRequest): Observable<RoleResponse> {
        return this.http.post<RoleResponse>(this.apiUrl, role);
    }

    deleteRole(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    getRoleById(id: number): Observable<RoleResponse> {
        return this.http.get<RoleResponse>(`${this.apiUrl}/${id}`);
    }

    updateRole(id: number, role: RoleRequest): Observable<RoleResponse> {
        return this.http.put<RoleResponse>(`${this.apiUrl}/${id}`, role);
    }

    enableRole(id: number): Observable<RoleResponse> {
        return this.http.put<RoleResponse>(`${this.apiUrl}/enable/${id}`, null);
    }

    disableRole(id: number): Observable<RoleResponse> {
        return this.http.put<RoleResponse>(`${this.apiUrl}/disable/${id}`, null);
    }
    grantAuthoritiesToRole(roleId: number, authorityIds: number[]): Observable<RoleResponse> {
        return this.http.post<RoleResponse>(`${this.apiUrl}/grantAuth/${roleId}`, authorityIds);
    }

    revokeAuthoritiesFromRole(roleId: number, authorityIds: number[]): Observable<RoleResponse> {
        return this.http.request<RoleResponse>('delete', `${this.apiUrl}/revokeAuth/${roleId}`, { body: authorityIds });
    }
    getModulesRolesExcludingProfileRole(profileId:number): Observable<RoleResponse[]>{
        return this.http.get<RoleResponse[]>(`${this.apiUrl}/mdulesRolesExcludingProfileRole/${profileId}`)};
}
