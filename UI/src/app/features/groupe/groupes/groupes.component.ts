import { Component, ViewChild } from '@angular/core';
import { Button } from 'primeng/button';
import { IconField } from 'primeng/iconfield';
import { InputIcon } from 'primeng/inputicon';
import { InputText } from 'primeng/inputtext';
import { Router } from '@angular/router';
import { Table, TableModule } from 'primeng/table';
import { ConfirmationService } from 'primeng/api';
import { NotificationService } from '../../../services/notification.service';
import { Page } from '../../../models/Page';
import { GroupeResponse } from '../../../models/groupe/GroupeResponse';
import { GroupesService } from '../../../services/groupe/groupes.service';
import { Toolbar } from 'primeng/toolbar';
import { Dialog } from 'primeng/dialog';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { GroupeRequest } from '../../../models/groupe/GroupeRequest';

@Component({
    selector: 'app-groupes',
    imports: [Button, IconField, InputIcon, InputText, TableModule, Toolbar, Dialog, ReactiveFormsModule],
    templateUrl: './groupes.component.html',
    styleUrl: './groupes.component.scss'
})
export class GroupesComponent {
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
    groups: GroupeResponse[] = [];
    groupRequest!: GroupeRequest;
    groupResponse!: GroupeResponse;

    newGroup = false;
    updateGroupDailogue = false;
    createGroup: FormGroup | any;
    updateGroupForm: FormGroup | any;
    groupeId!: number;
    originalValues = { libelle: '' };

    displayConfirmation: boolean = false;
    displayActifConfirmation: boolean = false;
    idConfirmation: number = 0;
    yesConfirmation!: string;
    noConfirmation!: string;
    messageConfirmation: string = '';
    actifValue!: boolean;

    constructor(
        private groupeService: GroupesService,
        private confirmationService: ConfirmationService,
        private notificationService: NotificationService,
        private router: Router
    ) {
        this.groupRequest = {
            libelle: ''
        };
        this.createGroup = new FormGroup({
            libelle: new FormControl('', [Validators.required, Validators.minLength(5)])
        });
        this.updateGroupForm = new FormGroup({
            libelle: new FormControl('', [Validators.required, Validators.minLength(5)])
        });
    }

    ngOnInit(): void {}

    loadGroups(
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
        this.groupeService.getGroupes(page, size, searchExpr, searchOperation, searchValue, filters).subscribe({
            next: (page: Page<GroupeResponse>) => {
                this.groups = page.content;
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
        this.loadGroups(pageIndex, pageSize, this.searchExpr, this.searchOperation, this.searchValue);
        // Recharge les données pour la nouvelle page
    }

    onGlobalFilter(event: any): void {
        this.searchExpr = 'libelle'; // Champ par défaut pour la recherche
        this.searchOperation = 'contains'; // Opération par défaut
        this.searchValue = event.target.value;

        this.loadGroups(0, this.rows, this.searchExpr, this.searchOperation, this.searchValue);
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
        this.loadGroups(0, this.rows, this.searchExpr, this.searchOperation, this.searchValue, this.appliedFilters);
    }

    confirmDelete(id: number) {
        this.displayConfirmation = true;
        this.idConfirmation = id;
    }

    cancelDelete() {
        this.displayConfirmation = false;
    }

    deleteGroup() {
        this.groupeService.deleteGroupe(this.idConfirmation).subscribe({
            next: () => {
                this.groups = this.groups.filter((group) => group.id !== this.idConfirmation);
                this.notificationService.showSuccess('Success', 'Group deleted successfully');
                this.cancelDelete()
            },
            error: (err) => {
                this.notificationService.showError('Error', `Failed to delete Group: ${this.errorMessage}`);
            }
        });
    }
    cancelActifDisactif() {
        this.displayActifConfirmation = false;
    }
    confirmActifAndDisactif(id: number, actif: boolean) {
        this.displayActifConfirmation = true;
        this.messageConfirmation = actif ? 'Are you sure you want to disable this User?' : 'Are you sure you want to enable this User?';
        this.noConfirmation = 'Cancel';
        this.idConfirmation = id;
        this.yesConfirmation = actif ? 'Disactf' : 'Actif';
        this.actifValue = actif;
    }

    toggleGroupStatus() {
        const action = this.actifValue ? this.groupeService.disableGroupe(this.idConfirmation) : this.groupeService.enableGroupe(this.idConfirmation);

        const successMessage = this.actifValue ? 'Group disabled successfully' : 'Group enabled successfully';

        const errorMessage = this.actifValue ? 'Failed to disable Group' : 'Failed to enable Group';

        action.subscribe({
            next: () => {
                this.notificationService.showSuccess('Success', successMessage);
                this.cancelActifDisactif()
                const index = this.groups.findIndex((gr) => gr.id === this.idConfirmation);
                if (index !== -1) {
                    this.groups[index] = {
                        ...this.groups[index],
                        actif: !this.actifValue
                    };
                }
            },
            error: (err) => {
                this.notificationService.showError('Error', `${errorMessage}: ${err.message || err}`);
            }
        });
    }

    newGroupe() {
        this.newGroup = true;
    }

    CreateGroup() {
        this.groupRequest.libelle = this.createGroup.get('libelle').value;
        this.groupeService.createGroupe(this.groupRequest).subscribe({
            next: (data) => {
                this.groupResponse = data;
                this.newGroup = false;
                this.notificationService.showSuccess('Succès', 'Group created successfully');
                this.loadGroups();
            },
            error: (error) => {
                this.notificationService.showError('Error', 'Failed to create Group');
            }
        });
    }

    hideDialog() {
        this.newGroup = false;
    }

    hideUpdateDialog() {
        this.updateGroupDailogue = false;
    }

    afficheUpdateGroup(id: number) {
        this.getGroupeById(id);
        this.updateGroupDailogue = true;
        this.groupeId = id;
    }

    UpdateGroup() {
        this.groupRequest.libelle = this.updateGroupForm.get('libelle').value;
        this.groupeService.updateGroupe(this.groupeId, this.groupRequest).subscribe({
            next: (data) => {
                this.groupResponse = data;
                this.updateGroupDailogue = false;
                this.loadGroups();
                this.notificationService.showSuccess('Succès', 'Group updated successfully');
            },
            error: (error) => {
                console.log(error);
                this.notificationService.showError('Error', 'Failed to update Group');
            }
        });
    }

    getGroupeById(id: number) {
        this.groupeService.getGroupeById(id).subscribe({
            next: (data) => {
                this.groupResponse = data;
                this.originalValues = {
                    libelle: this.groupResponse.libelle
                };
                this.updateGroupForm.patchValue({
                    libelle: this.groupResponse.libelle
                });
            },
            error: (error) => {
                console.log(error);
            }
        });
    }

    hasChanged(): boolean {
        const formValues = this.updateGroupForm.value;
        return formValues.libelle !== this.originalValues.libelle;
    }
}
