import { Component, OnInit, ViewChild } from '@angular/core';
import { Toolbar } from 'primeng/toolbar';
import { Button } from 'primeng/button';
import { Table, TableModule } from 'primeng/table';
import { IconField } from 'primeng/iconfield';
import { InputIcon } from 'primeng/inputicon';
import { Users } from '../../../models/user/Users';
import { UsersService } from '../../../services/user/users.service';
import { FormsModule } from '@angular/forms';
import { ConfirmationService } from 'primeng/api';
import { Router, RouterLink } from '@angular/router';
import { InputText } from 'primeng/inputtext';
import { NotificationService } from '../../../services/notification.service';
import { Page } from '../../../models/Page';
import { Dialog } from 'primeng/dialog';

@Component({
    selector: 'app-users',
    imports: [Button, TableModule, Toolbar, IconField, InputIcon, FormsModule, RouterLink, InputText, Dialog],
    providers: [ConfirmationService],
    templateUrl: './users.component.html',
    styleUrl: './users.component.scss'
})
export class UsersComponent implements OnInit {
    @ViewChild('dt') dt!: Table;

    totalRecords = 0;
    rows = 7;
    first: number = 0;

    searchExpr!: string;
    searchOperation!: string;
    searchValue!: string;

    appliedFilters: { field: string; matchMode: any; value: any }[] = [];

    users: Users[] = [];
    selectedUsers: Users[] = [];
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
        private usersService: UsersService,
        private notificationService: NotificationService,
        private router: Router
    ) {}

    ngOnInit(): void {}

    loadUsers(
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
        this.usersService.getUsers(page, size, searchExpr, searchOperation, searchValue, filters).subscribe({
            next: (page: Page<Users>) => {
                this.users = page.content;
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
        this.loadUsers(pageIndex, pageSize, this.searchExpr, this.searchOperation, this.searchValue);
        // Recharge les données pour la nouvelle page
    }

    onGlobalFilter(event: any): void {
        this.searchExpr = 'userName'; // Champ par défaut pour la recherche
        this.searchOperation = 'contains'; // Opération par défaut
        this.searchValue = event.target.value;

        this.loadUsers(0, this.rows, this.searchExpr, this.searchOperation, this.searchValue);
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
        this.loadUsers(0, this.rows, this.searchExpr, this.searchOperation, this.searchValue, this.appliedFilters);
    }

    updateUser(id: number) {
        this.router.navigate(['/users/update', id]);
    }

    deleteUser(): void {
        this.usersService.deleteUser(this.idConfirmation).subscribe({
            next: () => {
                this.users = this.users.filter((author) => author.id !== this.idConfirmation);
                this.notificationService.showSuccess('Success', 'User deleted successfully');
                this.cancelDelete();
            },
            error: (err) => {
                this.errorMessage = err;
                this.notificationService.showError('Error', `Failed to delete User: ${this.errorMessage}`);
            }
        });
    }

    confirmDelete(id: number) {
        this.displayConfirmation = true;
        this.idConfirmation = id;
    }

    cancelDelete() {
        this.displayConfirmation = false;
    }

    deleteSelectedUsers(): void {
        if (this.selectedUsers.length === 0) {
            this.notificationService.showError('Warning', 'No users selected for deletion.');
            return;
        }

        this.displayDeleteSelectedConfirmation = true;
    }

    deleteSelectedUsersConfirmed(): void {
        this.selectedUsers.forEach((user) => {
            this.usersService.deleteUser(user.id).subscribe({
                next: () => {
                    this.users = this.users.filter((a) => a.id !== user.id);
                    this.totalRecords--;
                    this.notificationService.showSuccess('Success', 'Selected users deleted successfully.');
                },
                error: (err) => {
                    this.notificationService.showError('Error', `Failed to delete User with ID ${user.id}: ${err.message || err}`);
                }
            });
        });

        this.selectedUsers = [];
        this.displayDeleteSelectedConfirmation = false;
    }

    cancelDeleteSelected(): void {
        this.displayDeleteSelectedConfirmation = false;
    }

    confirmActifAndDisactif(id: number, actif: boolean) {
        this.displayActifConfirmation = true;
        this.messageConfirmation = actif ? 'Are you sure you want to disable this User?' : 'Are you sure you want to enable this User?';
        this.noConfirmation = 'Cancel';
        this.idConfirmation = id;
        this.yesConfirmation = actif ? 'Disactf' : 'Actif';
        this.actifValue = actif;
    }

    cancelActifDisactif() {
        this.displayActifConfirmation = false;
    }

    toggleUserStatus() {
        const action = this.usersService.enableDisableUser(this.idConfirmation, this.actifValue);

        const successMessage = this.actifValue ? 'User disabled successfully' : 'User enabled successfully';

        const errorMessage = this.actifValue ? 'Failed to disable User' : 'Failed to enable User';

        action.subscribe({
            next: () => {
                this.notificationService.showSuccess('Success', successMessage);
                this.cancelActifDisactif();
                // Mettre à jour uniquement l'élément concerné
                const index = this.users.findIndex((us) => us.id === this.idConfirmation);
                if (index !== -1) {
                    this.users[index] = {
                        ...this.users[index],
                        actif: !this.actifValue // Problème ici si `actif` n'existe pas dans `Users`
                    } as Users; // Ajout du `as Users`
                }
            },
            error: (err) => {
                console.error('Error updating user:', err);
                this.notificationService.showError('Error', `${errorMessage}: ${err.message || err}`);
            }
        });
    }

    showUserDetails(id: number) {
        this.router.navigate(['/users/detail', id]);
    }
}
