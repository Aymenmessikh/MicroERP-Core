import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ProfileRequest } from '../../models/profile/ProfileRequest';
import { Observable } from 'rxjs';
import { ProfileResponse } from '../../models/profile/ProfileResponse';

@Injectable({
    providedIn: 'root'
})
export class ProfilesService {
    private apiUrl = 'http://localhost:8089/api/v1/admin/profile';

    constructor(private http: HttpClient) {}

    createProfile(id: number, profile: ProfileRequest): Observable<ProfileResponse> {
        return this.http.post<ProfileResponse>(`${this.apiUrl}/${id}`, profile);
    }

    getProfilesByUser(userName: string): Observable<ProfileResponse[]> {
        return this.http.get<ProfileResponse[]>(`${this.apiUrl}/Profiles/${userName}`);
    }

    deleteProfile(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    getProfileById(id: number): Observable<ProfileResponse> {
        return this.http.get<ProfileResponse>(`${this.apiUrl}/${id}`);
    }

    updateProfile(id: number, profile: ProfileRequest): Observable<ProfileResponse> {
        return this.http.put<ProfileResponse>(`${this.apiUrl}/${id}`, profile);
    }

    enableProfile(id: number): Observable<ProfileResponse> {
        return this.http.put<ProfileResponse>(`${this.apiUrl}/enable/${id}`, null);
    }

    disableProfile(id: number): Observable<ProfileResponse> {
        return this.http.put<ProfileResponse>(`${this.apiUrl}/disable/${id}`, null);
    }

    grantAuthoritiesToProfile(id: number, authorityIds: number[]): Observable<ProfileResponse> {
        return this.http.post<ProfileResponse>(`${this.apiUrl}/addAuthority/${id}`, authorityIds);
    }

    revokeAuthoritiesFromProfile(id: number, authorityIds: number[]): Observable<ProfileResponse> {
        return this.http.request<ProfileResponse>('delete', `${this.apiUrl}/removeAuthority/${id}`, { body: authorityIds });
    }
    grantRolesToProfile(id: number, roleIds: number[]): Observable<ProfileResponse> {
        return this.http.put<ProfileResponse>(`${this.apiUrl}/addRole/${id}`, roleIds);
    }

    revokeRolesFromProfile(id: number, roleIds: number[]): Observable<ProfileResponse> {
        return this.http.put<ProfileResponse>(`${this.apiUrl}/removeRole/${id}`,roleIds);
    }
    addModuleToProfile(id: number, roleId: number): Observable<ProfileResponse> {
        return this.http.put<ProfileResponse>(`${this.apiUrl}/addModule/${id}/${roleId}`, null);
    }

    removeModulesFromProfile(id: number, roleId: number): Observable<ProfileResponse> {
        return this.http.put<ProfileResponse>(`${this.apiUrl}/removeModule/${id}/${roleId}`,null);
    }
}
