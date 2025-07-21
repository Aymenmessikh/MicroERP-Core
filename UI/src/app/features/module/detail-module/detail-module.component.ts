import { Component, OnInit, ViewChild } from '@angular/core';
import { Tab, TabList, TabPanel, TabPanels, Tabs } from 'primeng/tabs';
import { ModuleResponse } from '../../../models/module/ModuleResponse';
import { ModulesService } from '../../../services/module/modules.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from '../../../services/notification.service';
import { Button } from 'primeng/button';
import { Table, TableModule } from 'primeng/table';
import { RoleResponse } from '../../../models/role/RoleResponse';
import { RolesService } from '../../../services/role/roles.service';
import { ConfirmationService } from 'primeng/api';
import { Page } from '../../../models/Page';
import { BlockUI } from 'primeng/blockui';
import { AuthorityResponse } from '../../../models/authority/AuthorityResponse';
import { AuthorityService } from '../../../services/authority/authority.service';
import { Toolbar } from 'primeng/toolbar';
import { Dialog } from 'primeng/dialog';
import {UsersService} from "../../../services/user/users.service";
import {UserProfileInfo} from "../../../models/user/UserProfileInfo";

@Component({
    selector: 'app-detail-module',
    imports: [Tabs, TabList, Tab, TabPanels, TabPanel, Button, TableModule, BlockUI, Toolbar, Dialog],
    templateUrl: './detail-module.component.html',
    styleUrl: './detail-module.component.scss'
})
export class DetailModuleComponent implements OnInit {
    moduleId: any;
    errorMessage = '';
    isLoading = true;
    moduleResponse!: ModuleResponse;

    //Role
    @ViewChild('dt') dt!: Table;

    totalRecordsRole = 0;
    totalRecordsAuth = 0;
    rows = 7;
    first: number = 0;

    searchExpr!: string;
    searchOperation!: string;
    searchValue!: string;

    appliedFilters: { field: string; matchMode: any; value: any }[] = [];

    RoleisLoading = true;
    statuses!: any[];
    roles: RoleResponse[] = [];
    selectedRoles: RoleResponse[] = [];

    moduleIdl= 1;
    users: UserProfileInfo[] = [];
    //Authz
    isAuthLoading = true;
    authoritys: AuthorityResponse[] = [];

    moduleName!: string;

    displayActifConfirmation: boolean = false;
    idConfirmation: number = 0;
    yesConfirmation!: string;
    noConfirmation!: string;
    messageConfirmation: string = '';
    actifValue!: boolean;

    constructor(
        private moduleService: ModulesService,
        private router: Router,
        private route: ActivatedRoute,
        private roleService: RolesService,
        private confirmationService: ConfirmationService,
        private notificationService: NotificationService,
        private authorityService: AuthorityService,
        private userService:UsersService
    ) {}

    ngOnInit(): void {
        this.moduleId = this.route.snapshot.paramMap.get('id');
        console.log('Module ID:', this.moduleId);
        this.getModuleById();
    }

    getModuleById() {
        this.moduleService.getModuleById(this.moduleId).subscribe({
            next: (data) => {
                this.moduleResponse = data;
                this.moduleName = this.moduleResponse.moduleName;
                this.isLoading = false;
            },
            error: (error) => {
                this.errorMessage = error;
                this.isLoading = false;
            }
        });
    }

    //start Role
    loadRoles(
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
        filters = filters || [];

        filters.push({
            field: 'module.moduleName',
            matchMode: 'equals',
            value: this.moduleName
        });
        this.roleService.getRoles(page, size, searchExpr, searchOperation, searchValue, filters).subscribe({
            next: (page: Page<RoleResponse>) => {
                this.roles = page.content;
                this.totalRecordsRole = page.totalElements;
                this.RoleisLoading = false;
            },
            error: (error) => {
                console.error('Error loading Roles', error);
                this.RoleisLoading = false;
            }
        });
    }

    confirmDelete(roleId: number) {
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete this role?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            acceptLabel: 'Yes',
            rejectLabel: 'No',
            accept: () => {
                this.deleteRole(roleId);
            },
            reject: () => {
                console.log('Deletion cancelled');
            }
        });
    }

    deleteRole(roleId: number) {
        this.roleService.deleteRole(roleId).subscribe({
            next: () => {
                // Supprime le rôle supprimé de la liste locale
                this.roles = this.roles.filter((role) => role.id !== roleId);
                // Affiche une notification de succès
                this.notificationService.showSuccess('Success', 'Role deleted successfully');
            },
            error: (err) => {
                // Affiche une notification d'erreur
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

    confirmActifAndDisactif(id: number, actif: boolean) {
        this.displayActifConfirmation = true;
        this.messageConfirmation = actif ? 'Are you sure you want to disable this Module?' : 'Are you sure you want to enable this Module?';
        this.noConfirmation = 'Cancel';
        this.idConfirmation = id;
        this.yesConfirmation = actif ? 'Disactf' : 'Actif';
        this.actifValue = actif;
    }

    cancelActifDisactif() {
        this.displayActifConfirmation = false;
    }

    toggleRoleStatus(id: number, actif: boolean) {
        const action = actif ? this.roleService.disableRole(id) : this.roleService.enableRole(id);

        const successMessage = actif ? 'Module disabled successfully' : 'Module enabled successfully';

        const errorMessage = actif ? 'Failed to disable Module' : 'Failed to enable Module';

        action.subscribe({
            next: () => {
                this.notificationService.showSuccess('Success', successMessage);
                this.cancelActifDisactif();
                const index = this.roles.findIndex((auth) => auth.id === id);
                if (index !== -1) {
                    this.roles[index] = {
                        ...this.roles[index],
                        actif: !actif // Mettre à jour l'état actif/inactif
                    };
                }
            },
            error: (err) => {
                console.error('Error updating authority:', err);
                this.notificationService.showError('Error', `${errorMessage}: ${err.message || err}`);
            }
        });
    }

    //End Role

    //Start Auth
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
        filters = filters || [];

        filters.push({
            field: 'module.moduleName',
            matchMode: 'equals',
            value: this.moduleName
        });
        this.authorityService.getAuthorities(page, size, searchExpr, searchOperation, searchValue, filters).subscribe({
            next: (page: Page<AuthorityResponse>) => {
                this.authoritys = page.content;
                this.totalRecordsAuth = page.totalElements;
                this.isAuthLoading = false;
            },
            error: (error) => {
                console.error('Error loading authoritys', error);
                this.isLoading = false;
            }
        });
    }

    updateAuthority(id: number) {
        this.router.navigate(['/authoritys/update', id]);
    }

    // Supprimer une autorité
    deleteAuthority(authorityId: number): void {
        this.authorityService.deleteAuthority(authorityId).subscribe({
            next: () => {
                this.authoritys = this.authoritys.filter((author) => author.id !== authorityId);
                this.notificationService.showSuccess('Success', 'Authority deleted successfully');
            },
            error: (err) => {
                this.errorMessage = err;
                this.notificationService.showError('Error', `Failed to delete Authority: ${this.errorMessage}`);
            }
        });
    }

    confirmDeleteAuth(id: number) {
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete this Authority?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            rejectLabel: 'No',
            acceptLabel: 'Yes',
            accept: () => {
                this.deleteAuthority(id);
            },
            reject: () => {
                console.log('Deletion cancelled');
            }
        });
    }

    confirmActifAndDisactifAuth(id: number, actif: boolean) {
        const message = actif ? 'Are you sure you want to disable this Authority?' : 'Are you sure you want to enable this Authority?';

        this.confirmationService.confirm({
            message,
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            rejectLabel: 'No',
            acceptLabel: 'Yes',
            accept: () => this.toggleAuthorityStatus(id, actif),
            reject: () => console.log('Action cancelled')
        });
    }

    toggleAuthorityStatus(id: number, actif: boolean) {
        const action = actif ? this.authorityService.disableAuthority(id) : this.authorityService.enableAuthority(id);

        const successMessage = actif ? 'Authority disabled successfully' : 'Authority enabled successfully';

        const errorMessage = actif ? 'Failed to disable Authority' : 'Failed to enable Authority';

        action.subscribe({
            next: () => {
                this.notificationService.showSuccess('Success', successMessage);

                // Mettre à jour uniquement l'élément concerné
                const index = this.authoritys.findIndex((auth) => auth.id === id);
                if (index !== -1) {
                    this.authoritys[index] = {
                        ...this.authoritys[index],
                        actif: !actif // Mettre à jour l'état actif/inactif
                    };
                }
            },
            error: (err) => {
                console.error('Error updating authority:', err);
                this.notificationService.showError('Error', `${errorMessage}: ${err.message || err}`);
            }
        });
    }

    //End Auth
    // Gérer la pagination
    onPageChangeRole(event: any): void {
        let pageIndex = event.first / event.rows;
        const pageSize = event.rows; // Nombre d'éléments par page
        pageIndex = pageIndex * pageSize; //
        this.loadRoles(pageIndex, pageSize, this.searchExpr, this.searchOperation, this.searchValue);
        // Recharge les données pour la nouvelle page
    }

    onPageChangeAuth(event: any): void {
        let pageIndex = event.first / event.rows;
        const pageSize = event.rows; // Nombre d'éléments par page
        pageIndex = pageIndex * pageSize; //
        this.loadAuthorities(pageIndex, pageSize, this.searchExpr, this.searchOperation, this.searchValue);
        // Recharge les données pour la nouvelle page
    }

    onFilterRole(event: any) {
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

    onFilterAuth(event: any) {
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
        this.loadAuthorities(0, this.rows, this.searchExpr, this.searchOperation, this.searchValue, this.appliedFilters);
    }

    toggleModuleStatus() {}
    fetchUsersByModules(): void {
        this.userService.getUsersByModuleId(this.moduleIdl).subscribe({
            next: (data) => this.users = data,
            error: (err) => console.error('Erreur lors du chargement des utilisateurs', err)
        });
    }
}
