import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from '../../models/Page';
import { AuditResponseDto } from '../../models/audit-log/AuditResponseDto';

@Injectable({
    providedIn: 'root'
})
export class AuditLogService {

    private apiUrl = 'http://localhost:8089/api/v1/audit-log';

    constructor(private http: HttpClient) {}
    getAllAuditLogs(page: number, size: number, searchExpr?: string, searchOperation?: string, searchValue?: string, filters?: { field: string; matchMode: string; value: string }[]): Observable<Page<AuditResponseDto>> {
        let params = new HttpParams().set('skip', page.toString()).set('take', size.toString());

        if (searchExpr && searchOperation && searchValue) {
            params = params.set('searchExpr', searchExpr).set('searchOperation', searchOperation).set('searchValue', searchValue);
        }
        if (filters && filters.length > 0) {
            const filterQuery = filters.map((filter) => `${filter.field},${filter.matchMode},${filter.value}`).join(','); // On utilise ',' pour séparer chaque filtre

            params = params.set('filter', filterQuery);
        }

        return this.http.get<Page<AuditResponseDto>>(this.apiUrl, { params });
    }
}
