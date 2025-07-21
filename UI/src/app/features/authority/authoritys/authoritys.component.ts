import { Component, OnInit, ViewChild } from '@angular/core';
import { Table, TableModule } from 'primeng/table';
import { AuthorityService } from '../../../services/authority/authority.service';
import { AuthorityResponse } from '../../../models/authority/AuthorityResponse';
import { Page } from '../../../models/Page';
import { Toolbar } from 'primeng/toolbar';
import { Button } from 'primeng/button';
import { IconField } from 'primeng/iconfield';
import { InputIcon } from 'primeng/inputicon';
import { InputText } from 'primeng/inputtext';
import { Router, RouterLink } from '@angular/router';
import { NotificationService } from '../../../services/notification.service';
import { Dialog } from 'primeng/dialog';

@Component({
    selector: 'app-authoritys',
    templateUrl: './authoritys.component.html',
    imports: [Toolbar, Button, TableModule, IconField, InputIcon, InputText, RouterLink, Dialog],
    styleUrls: ['./authoritys.component.scss']
})
export class AuthoritysComponent implements OnInit {
    @ViewChild('dt') dt!: Table;

    totalRecords = 0;
    rows = 7;
    first: number = 0;

    searchExpr!: string;
    searchOperation!: string;
    searchValue!: string;

    appliedFilters: { field: string; matchMode: any; value: any }[] = [];

    authoritys: AuthorityResponse[] = [];
    selectedAuthorities: AuthorityResponse[] = [];
    isLoading = true;
    errorMessage = '';

    displayConfirmation: boolean = false;
    displayActifConfirmation: boolean = false;
    idConfirmation: number = 0;
    yesConfirmation!: string;
    noConfirmation!: string;
    messageConfirmation: string = '';
    actifValue!: boolean;
    displayDeleteSelectedConfirmation: boolean = false;

    constructor(
        private authorityService: AuthorityService,
        private notificationService: NotificationService,
        private router: Router
    ) {}

    ngOnInit(): void {}

    // Charger les autorités avec pagination, recherche et filtrage
    loadAuthorities(
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
        this.isLoading = true;
        this.authorityService.getAuthorities(page, size, searchExpr, searchOperation, searchValue, filters).subscribe({
            next: (page: Page<AuthorityResponse>) => {
                this.authoritys = page.content;
                this.totalRecords = page.totalElements;
                this.isLoading = false;
            },
            error: (error) => {
                console.error('Error loading authoritys', error);
                this.isLoading = false;
            }
        });
    }

    // Gérer la pagination
    onPageChange(event: any): void {
        let pageIndex = event.first / event.rows;
        const pageSize = event.rows; // Nombre d'éléments par page
        pageIndex = pageIndex * pageSize; //
        this.loadAuthorities(pageIndex, pageSize, this.searchExpr, this.searchOperation, this.searchValue);
        // Recharge les données pour la nouvelle page
    }

    onGlobalFilter(event: any): void {
        this.searchExpr = 'libelle'; // Champ par défaut pour la recherche
        this.searchOperation = 'contains'; // Opération par défaut
        this.searchValue = event.target.value;

        this.loadAuthorities(0, this.rows, this.searchExpr, this.searchOperation, this.searchValue);
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

        this.loadAuthorities(0, this.rows, this.searchExpr, this.searchOperation, this.searchValue, this.appliedFilters);
    }

    //Update Authority
    updateAuthority(id: number) {
        this.router.navigate(['/authoritys/update', id]);
    }

    // Supprimer une autorité
    deleteAuthority(): void {
        this.authorityService.deleteAuthority(this.idConfirmation).subscribe({
            next: () => {
                this.authoritys = this.authoritys.filter((author) => author.id !== this.idConfirmation);
                this.notificationService.showSuccess('Success', 'Authority deleted successfully');
                this.cancelDelete();
            },
            error: (err) => {
                this.errorMessage = err;
                this.notificationService.showError('Error', `Failed to delete Authority: ${this.errorMessage}`);
            }
        });
    }

    toggleAuthorityStatus() {
        const action = this.actifValue ? this.authorityService.disableAuthority(this.idConfirmation) : this.authorityService.enableAuthority(this.idConfirmation);

        const successMessage = this.actifValue ? 'Authority disabled successfully' : 'Authority enabled successfully';

        const errorMessage = this.actifValue ? 'Failed to disable Authority' : 'Failed to enable Authority';

        action.subscribe({
            next: () => {
                this.notificationService.showSuccess('Success', successMessage);
                this.cancelActifDisactif();
                // Mettre à jour uniquement l'élément concerné
                const index = this.authoritys.findIndex((auth) => auth.id === this.idConfirmation);
                if (index !== -1) {
                    this.authoritys[index] = {
                        ...this.authoritys[index],
                        actif: !this.actifValue // Mettre à jour l'état actif/inactif
                    };
                }
            },
            error: (err) => {
                console.error('Error updating authority:', err);
                this.notificationService.showError('Error', `${errorMessage}: ${err.message || err}`);
            }
        });
    }

    confirmActifAndDisactif(id: number, actif: boolean) {
        this.displayActifConfirmation = true;
        this.messageConfirmation = actif ? 'Are you sure you want to disable this Authority?' : 'Are you sure you want to enable this Authority?';
        this.noConfirmation = 'Cancel';
        this.idConfirmation = id;
        this.yesConfirmation = actif ? 'Disactf' : 'Actif';
        this.actifValue = actif;
    }
    confirmDelete(id: number) {
        this.displayConfirmation = true;
        this.idConfirmation = id;
    }

    cancelActifDisactif() {
        this.displayActifConfirmation = false;
    }
    cancelDelete() {
        this.displayConfirmation = false;
    }
    cancelDeleteSelected(): void {
        this.displayDeleteSelectedConfirmation = false;
    }

    deleteSelectedAuthorities() {}

    deleteSelectedUsersConfirmed() {}
}
