import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ModuleResponse } from '../../models/module/ModuleResponse';
import { ModuleRequest } from '../../models/module/ModuleRequest';

@Injectable({
    providedIn: 'root'
})
export class ModulesService {
    private apiUrl = 'http://localhost:8089/api/v1/admin/module';

    constructor(private http: HttpClient) {}

    // getModules(
    //     page: number,
    //     size: number,
    //     searchExpr?: string,
    //     searchOperation?: string,
    //     searchValue?: string,
    //     filters?: {
    //         field: string;
    //         matchMode: string;
    //         value: string;
    //     }[]
    // ): Observable<Page<ModuleResponse>> {
    //     let params = new HttpParams().set('skip', page.toString()).set('take', size.toString());
    //
    //     if (searchExpr && searchOperation && searchValue) {
    //         params = params.set('searchExpr', searchExpr).set('searchOperation', searchOperation).set('searchValue', searchValue);
    //     }
    //
    //     // Ajouter les filtres directement sans les indexer
    //     if (filters && filters.length > 0) {
    //         const filterQuery = filters.map((filter) => `${filter.field},${filter.matchMode},${filter.value}`).join(','); // On utilise ',' pour séparer chaque filtre
    //
    //         params = params.set('filter', filterQuery);
    //     }
    //
    //     return this.http.get<Page<ModuleResponse>>(this.apiUrl, { params });
    // }
    getModules(): Observable<ModuleResponse[]> {
        return this.http.get<ModuleResponse[]>(this.apiUrl);
    }
    getModulesExcludingModuleProfile(id:number):Observable<ModuleResponse[]>{
        return this.http.get<ModuleResponse[]>(`${this.apiUrl}/modulesExcludingModuleProfile/${id}`);
    }
    getModulesByProfile(username:string):Observable<ModuleResponse[]>{
        return this.http.get<ModuleResponse[]>(`${this.apiUrl}/modulesByProfile/${username}`);
    }

    createModule(module: ModuleRequest): Observable<ModuleResponse> {
        return this.http.post<ModuleResponse>(this.apiUrl, module);
    }
    createModuleWithIcon(formData: FormData): Observable<ModuleResponse> {
        return this.http.post<ModuleResponse>(`${this.apiUrl}/with-icon`, formData);
    }
    deleteModule(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    getModuleById(id: number): Observable<ModuleResponse> {
        return this.http.get<ModuleResponse>(`${this.apiUrl}/${id}`);
    }

    updateModule(id: number, module: ModuleRequest): Observable<ModuleResponse> {
        return this.http.put<ModuleResponse>(`${this.apiUrl}/${id}`, module);
    }

    enableModule(id: number): Observable<ModuleResponse> {
        return this.http.put<ModuleResponse>(`${this.apiUrl}/enable/${id}`, null);
    }

    disableModule(id: number): Observable<ModuleResponse> {
        return this.http.put<ModuleResponse>(`${this.apiUrl}/disable/${id}`, null);
    }

    // grantAuthoritiesToRole(roleId: number, authorityIds: number[]): Observable<RoleResponse> {
    //     return this.http.post<RoleResponse>(`${this.apiUrl}/grantAuth/${roleId}`, authorityIds);
    // }
    //
    // revokeAuthoritiesFromRole(roleId: number, authorityIds: number[]): Observable<RoleResponse> {
    //     return this.http.request<RoleResponse>('delete', `${this.apiUrl}/revokeAuth/${roleId}`, { body: authorityIds });
    // }
}
