import {Component, OnInit, ViewChild} from '@angular/core';
import {Page} from "../../models/Page";
import { AuditResponseDto } from '../../models/audit-log/AuditResponseDto';
import {AuditLogService} from "../../services/audit-log/AuditLogService";
import {Table, TableModule} from 'primeng/table';
import {IconField} from "primeng/iconfield";
import {InputIcon} from "primeng/inputicon";
import {InputText} from "primeng/inputtext";
import {DatePipe, NgClass} from "@angular/common";

@Component({
    selector: 'app-audit-log',
    imports: [TableModule, IconField, InputIcon, InputText, NgClass, DatePipe],
    templateUrl: './audit-log.component.html',
    styleUrl: './audit-log.component.scss'
})
export class AuditLogComponent implements OnInit {
    auditResponseDto: AuditResponseDto[] = [];
    @ViewChild('dt') dt!: Table;

    totalRecords = 0;
    rows = 10;
    first: number = 0;

    searchExpr!: string;
    searchOperation!: string;
    searchValue!: string;

    appliedFilters: { field: string; matchMode: any; value: any }[] = [];

    isLoading = false;

    ngOnInit(): void {
        throw new Error('Method not implemented.');
    }

    constructor(private auditLogService: AuditLogService) {}

    loadAudit_log(
        page: number = 0,
        size: number = this.rows,
        searchExpr?: string,
        searchOperation?: string,
        searchValue?: string,
        filters?: {
            field: string;
            matchMode: string;
            value: string;
        }[]
    ): void {
        this.auditLogService.getAllAuditLogs(page, size, searchExpr, searchOperation, searchValue, filters).subscribe({
            next: (page: Page<AuditResponseDto>) => {
                this.auditResponseDto = page.content;
                this.totalRecords = page.totalElements;
                this.isLoading = false;
            },
            error: (error) => {
                console.error('Error loading Users', error);
                this.isLoading = false;
            }
        });
    }

    onPageChange(event: any): void {
        let pageIndex = event.first / event.rows;
        const pageSize = event.rows; // Nombre d'éléments par page
        pageIndex = pageIndex * pageSize; //
        this.loadAudit_log(pageIndex, pageSize, this.searchExpr, this.searchOperation, this.searchValue);
        // Recharge les données pour la nouvelle page
    }

    onGlobalFilter(event: any): void {
        this.searchExpr = 'entityName'; // Champ par défaut pour la recherche
        this.searchOperation = 'contains'; // Opération par défaut
        this.searchValue = event.target.value;

        this.loadAudit_log(0, this.rows, this.searchExpr, this.searchOperation, this.searchValue);
    }

    onFilter(event: any) {
        this.appliedFilters = [];

        const filters = event.filters;
        for (const field in filters) {
            if (filters[field] && Array.isArray(filters[field])) {
                // Vérifier que c'est bien un tableau
                filters[field].forEach((filter: any) => {
                    if (filter.value !== null && filter.value !== '') {
                        this.appliedFilters.push({
                            field,
                            matchMode: filter.matchMode,
                            value: filter.value
                        });
                    }
                });
            }
        }

        console.log('Filtres appliqués :', this.appliedFilters);

        // Recharger les données avec les filtres
        this.loadAudit_log(0, this.rows, this.searchExpr, this.searchOperation, this.searchValue, this.appliedFilters);
    }

    showUserDetails(id: number) {}

    getHttpMethodClass(httpMethod: string): string {
        switch (httpMethod.toLowerCase()) {
            case 'get':
                return 'http-get';
            case 'post':
                return 'http-post';
            case 'put':
                return 'http-put';
            case 'delete':
                return 'http-delete';
            case 'patch':
                return 'http-patch';
            default:
                return '';
        }
    }
}
