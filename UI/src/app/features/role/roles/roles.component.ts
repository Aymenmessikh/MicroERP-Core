import { Component, OnInit, ViewChild } from '@angular/core';
import { Button } from 'primeng/button';
import { IconField } from 'primeng/iconfield';
import { InputIcon } from 'primeng/inputicon';
import { Table, TableModule } from 'primeng/table';
import { Toolbar } from 'primeng/toolbar';
import { RolesService } from '../../../services/role/roles.service';
import { RoleResponse } from '../../../models/role/RoleResponse';
import { InputText } from 'primeng/inputtext';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { Router, RouterLink } from '@angular/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Page } from '../../../models/Page';
import { NotificationService } from '../../../services/notification.service';
import { Dialog } from 'primeng/dialog';

@Component({
    selector: 'app-roles',
    imports: [Button, IconField, InputIcon, TableModule, Toolbar, InputText, ReactiveFormsModule, FormsModule, DropdownModule, RouterLink, Dialog],
    templateUrl: './roles.component.html',
    styleUrl: './roles.component.scss',
    providers: [ConfirmationService, MessageService]
})
export class RolesComponent implements OnInit {
    @ViewChild('dt') dt!: Table;

    totalRecords = 0;
    rows = 10;
    first: number = 0;

    searchExpr!: string;
    searchOperation!: string;
    searchValue!: string;

    appliedFilters: { field: string; matchMode: any; value: any }[] = [];

    isLoading = true;
    errorMessage = '';
    statuses!: any[];
    roles: RoleResponse[] = [];
    selectedRoles: RoleResponse[] = [];

    displayConfirmation: boolean = false;
    displayActifConfirmation: boolean = false;
    idConfirmation: number = 0;
    yesConfirmation!: string;
    noConfirmation!: string;
    messageConfirmation: string = '';
    actifValue!: boolean;
    displayDeleteSelectedConfirmation: boolean = false;
    constructor(
        private roleService: RolesService,
        private confirmationService: ConfirmationService,
        private notificationService: NotificationService,
        private router: Router
    ) {}

    ngOnInit(): void {}

    loadRoles(page: number = 0, size: number = this.rows, searchExpr?: string, searchOperation?: string, searchValue?: string, filters?: { field: string; matchMode: string; value: string }[]): void {
        this.isLoading = true;
        this.roleService.getRoles(page, size, searchExpr, searchOperation, searchValue, filters).subscribe({
            next: (page: Page<RoleResponse>) => {
                this.roles = page.content;
                this.totalRecords = page.totalElements;
                this.isLoading = false;
            },
            error: (error) => {
                console.error('Error loading Roles', error);
                this.isLoading = false;
            }
        });
    }

    // Gérer la pagination
    onPageChange(event: any): void {
        let pageIndex = event.first / event.rows;
        const pageSize = event.rows; // Nombre d'éléments par page
        pageIndex = pageIndex * pageSize; //
        this.loadRoles(pageIndex, pageSize, this.searchExpr, this.searchOperation, this.searchValue);
        // Recharge les données pour la nouvelle page
    }

    onGlobalFilter(event: any): void {
        this.searchExpr = 'libelle'; // Champ par défaut pour la recherche
        this.searchOperation = 'contains'; // Opération par défaut
        this.searchValue = event.target.value;

        this.loadRoles(0, this.rows, this.searchExpr, this.searchOperation, this.searchValue);
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
        this.loadRoles(0, this.rows, this.searchExpr, this.searchOperation, this.searchValue, this.appliedFilters);
    }
    confirmDelete(id: number) {
        this.displayConfirmation = true;
        this.idConfirmation = id;
    }

    deleteRole() {
        this.roleService.deleteRole(this.idConfirmation).subscribe({
            next: () => {
                this.roles = this.roles.filter((role) => role.id !== this.idConfirmation);
                this.cancelDelete()
                this.notificationService.showSuccess('Success', 'Role deleted successfully');
            },
            error: (err) => {
                this.notificationService.showError('Error', `Failed to delete Role: ${this.errorMessage}`);
            }
        });
    }

    updateRole(roleId: number) {
        this.router.navigate(['/roles/update', roleId]);
    }

    showRoleDetails(roleId: number) {
        this.router.navigate(['/roles/detail', roleId]);
    }
    deleteSelectedRolesConfirmation(): void {
        if (this.selectedRoles.length === 0) {
            this.notificationService.showError('Warning', 'No Roles selected for deletion.');
            return;
        }
        this.displayDeleteSelectedConfirmation = true;
    }

    toggleRoleStatus() {
        const action = this.actifValue ? this.roleService.disableRole(this.idConfirmation) : this.roleService.enableRole(this.idConfirmation);

        const successMessage = this.actifValue ? 'Role disabled successfully' : 'Role enabled successfully';

        const errorMessage = this.actifValue ? 'Failed to disable Role' : 'Failed to enable Role';

        action.subscribe({
            next: () => {
                this.notificationService.showSuccess('Success', successMessage);
                this.cancelActifDisactif();
                const index = this.roles.findIndex((auth) => auth.id === this.idConfirmation);
                if (index !== -1) {
                    this.roles[index] = {
                        ...this.roles[index],
                        actif: !this.actifValue
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
        this.messageConfirmation = actif ? 'Are you sure you want to disable this Role?' : 'Are you sure you want to enable this Role?';
        this.noConfirmation = 'Cancel';
        this.idConfirmation = id;
        this.yesConfirmation = actif ? 'Disactf' : 'Actif';
        this.actifValue = actif;
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

    deleteSelectedUsersConfirmed() {}
}
