import { Component, OnInit, ViewChild } from '@angular/core';
import { Button } from 'primeng/button';
import { Dialog } from 'primeng/dialog';
import { IconField } from 'primeng/iconfield';
import { InputIcon } from 'primeng/inputicon';
import { InputText } from 'primeng/inputtext';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Table, TableModule } from 'primeng/table';
import { Toolbar } from 'primeng/toolbar';
import { GroupeResponse } from '../../../models/groupe/GroupeResponse';
import { GroupeRequest } from '../../../models/groupe/GroupeRequest';
import { GroupesService } from '../../../services/groupe/groupes.service';
import { ConfirmationService } from 'primeng/api';
import { NotificationService } from '../../../services/notification.service';
import { Router } from '@angular/router';
import { Page } from '../../../models/Page';
import { AuthorityTypeResponse } from '../../../models/authorityType/AuthorityTypeResponse';
import { AuthorityTypeService } from '../../../services/authorityType/authority-type.service';

@Component({
    selector: 'app-authority-type',
    imports: [Button, Dialog, IconField, InputIcon, InputText, ReactiveFormsModule, TableModule, Toolbar],
    templateUrl: './authority-type.component.html',
    styleUrl: './authority-type.component.scss'
})
export class AuthorityTypeComponent implements OnInit{
    @ViewChild('dt') dt!: Table;

    totalRecords = 0;
    rows = 10;
    first: number = 0;

    searchExpr!: string;
    searchOperation!: string;
    searchValue!: string;

    isLoading = true;
    errorMessage = '';
    statuses!: any[];
    authorityTypes: AuthorityTypeResponse[] = [];
    authorityTypeRequest!: GroupeRequest;
    authorityTypeResponse!: AuthorityTypeResponse;

    newAuthorityType_ = false;
    updateAuthorityTypeDailogue = false;
    createAuthorityType: FormGroup | any;
    updateAuthorityTypeForm: FormGroup | any;
    authorityTypeId!: number;
    originalValues = { libelle: '' };

    displayConfirmation: boolean = false;
    displayActifConfirmation: boolean = false;
    idConfirmation: number = 0;
    yesConfirmation!: string;
    noConfirmation!: string;
    messageConfirmation: string = '';
    actifValue!: boolean;

    constructor(
        private authorityTypeService:AuthorityTypeService,
        private notificationService: NotificationService,
        private router: Router
    ) {
        this.authorityTypeRequest = {
            libelle: ''
        };
        this.createAuthorityType = new FormGroup({
            libelle: new FormControl('', [Validators.required, Validators.minLength(5)])
        });
        this.updateAuthorityTypeForm = new FormGroup({
            libelle: new FormControl('', [Validators.required, Validators.minLength(5)])
        });
    }

    ngOnInit(): void {
        this.getAllAuthorityTypes()
    }


   getAllAuthorityTypes(){
        this.authorityTypeService.getAuthorityTypes().subscribe({
            next: data => {
                this.authorityTypes = data;
                this.isLoading=false
            },error: error => {
                console.log(error);
            }
        })
   }
    confirmDelete(id: number) {
        this.displayConfirmation = true;
        this.idConfirmation = id;
    }

    cancelDelete() {
        this.displayConfirmation = false;
    }

    deleteGroup() {
        this.authorityTypeService.deleteAuthorityType(this.idConfirmation).subscribe({
            next: () => {
                this.authorityTypes = this.authorityTypes.filter((authorityType) => authorityType.id !== this.idConfirmation);
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
        this.messageConfirmation = actif ? 'Are you sure you want to disable this Authority Type?' : 'Are you sure you want to enable this Authority Type?';
        this.noConfirmation = 'Cancel';
        this.idConfirmation = id;
        this.yesConfirmation = actif ? 'Disactf' : 'Actif';
        this.actifValue = actif;
    }

    toggleGroupStatus() {
        const action = this.actifValue ? this.authorityTypeService.disableAuthorityType(this.idConfirmation) : this.authorityTypeService.enableAuthorityType(this.idConfirmation);

        const successMessage = this.actifValue ? 'Group disabled successfully' : 'Group enabled successfully';

        const errorMessage = this.actifValue ? 'Failed to disable Group' : 'Failed to enable Group';

        action.subscribe({
            next: () => {
                this.notificationService.showSuccess('Success', successMessage);
                this.cancelActifDisactif()
                const index = this.authorityTypes.findIndex((at) => at.id === this.idConfirmation);
                if (index !== -1) {
                    this.authorityTypes[index] = {
                        ...this.authorityTypes[index],
                        actif: !this.actifValue
                    };
                }
            },
            error: (err) => {
                this.notificationService.showError('Error', `${errorMessage}: ${err.message || err}`);
            }
        });
    }

    newAuthorityType() {
        this.newAuthorityType_ = true;
    }

    CreateGroup() {
        this.authorityTypeRequest.libelle = this.createAuthorityType.get('libelle').value;
        this.authorityTypeService.createAuthorityType(this.authorityTypeRequest).subscribe({
            next: (data) => {
                this.authorityTypeResponse = data;
                this.newAuthorityType_ = false;
                this.notificationService.showSuccess('Succès', 'Authority Type created successfully');
                this.getAllAuthorityTypes();
            },
            error: (error) => {
                this.notificationService.showError('Error', 'Failed to create Authority Type');
            }
        });
    }

    hideDialog() {
        this.newAuthorityType_ = false;
    }

    hideUpdateDialog() {
        this.updateAuthorityTypeDailogue = false;
    }

    afficheUpdateGroup(id: number) {
        this.getGroupeById(id);
        this.updateAuthorityTypeDailogue = true;
        this.authorityTypeId = id;
    }

    UpdateGroup() {
        this.authorityTypeRequest.libelle = this.updateAuthorityTypeForm.get('libelle').value;
        this.authorityTypeService.updateAuthorityType(this.authorityTypeId, this.authorityTypeRequest).subscribe({
            next: (data) => {
                this.authorityTypeResponse = data;
                this.updateAuthorityTypeDailogue = false;
                this.getAllAuthorityTypes();
                this.notificationService.showSuccess('Succès', 'Authority Type updated successfully');
            },
            error: (error) => {
                console.log(error);
                this.notificationService.showError('Error', 'Failed to update Authority Type');
            }
        });
    }

    getGroupeById(id: number) {
        this.authorityTypeService.getAuthorityTypeById(id).subscribe({
            next: (data) => {
                this.authorityTypeResponse = data;
                this.originalValues = {
                    libelle: this.authorityTypeResponse.libelle
                };
                this.updateAuthorityTypeForm.patchValue({
                    libelle: this.authorityTypeResponse.libelle
                });
            },
            error: (error) => {
                console.log(error);
            }
        });
    }

    hasChanged(): boolean {
        const formValues = this.updateAuthorityTypeForm.value;
        return formValues.libelle !== this.originalValues.libelle;
    }
}
