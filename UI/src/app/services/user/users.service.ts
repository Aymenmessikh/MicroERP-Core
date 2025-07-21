import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Users } from '../../models/user/Users';
import { Page } from '../../models/Page';
import { UserRequest } from '../../models/user/UserRequest';
import { UserProfileInfo } from '../../models/user/UserProfileInfo';
import {ProfileResponse} from "../../models/profile/ProfileResponse";

@Injectable({
    providedIn: 'root'
})
export class UsersService {
    private apiUrl = 'http://localhost:8089/api/v1/admin/user';

    constructor(private http: HttpClient) {}
    getUsers(page: number, size: number, searchExpr?: string, searchOperation?: string, searchValue?: string, filters?: { field: string; matchMode: string; value: string }[]): Observable<Page<Users>> {
        let params = new HttpParams().set('skip', page.toString()).set('take', size.toString());

        if (searchExpr && searchOperation && searchValue) {
            params = params.set('searchExpr', searchExpr).set('searchOperation', searchOperation).set('searchValue', searchValue);
        }

        // Ajouter les filtres directement sans les indexer
        if (filters && filters.length > 0) {
            const filterQuery = filters.map((filter) => `${filter.field},${filter.matchMode},${filter.value}`).join(','); // On utilise ',' pour séparer chaque filtre

            params = params.set('filter', filterQuery);
        }

        return this.http.get<Page<Users>>(this.apiUrl, { params });
    }
    getListUsers(): Observable<Users[]> {
        return this.http.get<Users[]>(`${this.apiUrl}/get`);
    }
    createUser(user: UserRequest, id: number): Observable<Users> {
        return this.http.post<Users>(`${this.apiUrl}/${id}`, user);
    }
    getUserById(id: number): Observable<Users> {
        return this.http.get<Users>(`${this.apiUrl}/${id}`);
    }
    updateUser(id:number,user:UserRequest): Observable<Users>{
        return this.http.put<Users>(`${this.apiUrl}/${id}`, user);
    }
    deleteUser(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
    enableDisableUser(id: number, enable: boolean): Observable<Users> {
        return this.http.put<Users>(`${this.apiUrl}/enableDisable/${id}/${!enable}`, null);
    }
    getUsersByModuleId(moduleId: number): Observable<UserProfileInfo[]> {
        return this.http.get<UserProfileInfo[]>(`${this.apiUrl}/by-module/${moduleId}`);
    }
    changeActifProfile(id:number,username:string): Observable<Users>{
        return this.http.put<Users>(`${this.apiUrl}/changeActiveProfile/${id}/${username}`,null);
    }
    getActiveProfile(username: string): Observable<ProfileResponse> {
        return this.http.get<ProfileResponse>(`${this.apiUrl}/getActifProfil/${username}`);
    }

    getInactiveProfiles(username: string): Observable<ProfileResponse[]> {
        return this.http.get<ProfileResponse[]>(`${this.apiUrl}/getInActifProfil/${username}`);
    }
}
