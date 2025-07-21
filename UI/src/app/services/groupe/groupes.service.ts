import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../../models/Page';
import { RoleResponse } from '../../models/role/RoleResponse';
import { GroupeResponse } from '../../models/groupe/GroupeResponse';
import { GroupeRequest } from '../../models/groupe/GroupeRequest';
import { ModuleResponse } from '../../models/module/ModuleResponse';

@Injectable({
    providedIn: 'root'
})
export class GroupesService {
    private apiUrl = 'http://localhost:8089/api/v1/admin/groupe';

    constructor(private http: HttpClient) {}

    getGroupes(
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
    ): Observable<Page<GroupeResponse>> {
        let params = new HttpParams().set('skip', page.toString()).set('take', size.toString());

        if (searchExpr && searchOperation && searchValue) {
            params = params.set('searchExpr', searchExpr).set('searchOperation', searchOperation).set('searchValue', searchValue);
        }

        // Ajouter les filtres directement sans les indexer
        if (filters && filters.length > 0) {
            const filterQuery = filters.map((filter) => `${filter.field},${filter.matchMode},${filter.value}`).join(','); // On utilise ',' pour séparer chaque filtre

            params = params.set('filter', filterQuery);
        }

        return this.http.get<Page<GroupeResponse>>(this.apiUrl, { params });
    }
    getAllGroupes(): Observable<GroupeResponse[]> {
        return this.http.get<GroupeResponse[]>(`${this.apiUrl}/get`);
    }

    createGroupe(group: GroupeRequest): Observable<GroupeResponse> {
        return this.http.post<GroupeResponse>(this.apiUrl, group);
    }

    deleteGroupe(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    getGroupeById(id: number): Observable<GroupeResponse> {
        return this.http.get<GroupeResponse>(`${this.apiUrl}/${id}`);
    }

    updateGroupe(id: number, group: GroupeRequest): Observable<GroupeResponse> {
        return this.http.put<GroupeResponse>(`${this.apiUrl}/${id}`, group);
    }

    enableGroupe(id: number): Observable<RoleResponse> {
        return this.http.put<RoleResponse>(`${this.apiUrl}/enable/${id}`, null);
    }

    disableGroupe(id: number): Observable<RoleResponse> {
        return this.http.put<RoleResponse>(`${this.apiUrl}/disable/${id}`, null);
    }
}
