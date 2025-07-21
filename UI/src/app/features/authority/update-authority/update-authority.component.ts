import { Component, OnInit } from '@angular/core';
import { Button } from 'primeng/button';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputText } from 'primeng/inputtext';
import { Select } from 'primeng/select';
import { Toolbar } from 'primeng/toolbar';
import { ModulesService } from '../../../services/module/modules.service';
import { AuthorityTypeService } from '../../../services/authorityType/authority-type.service';
import { AuthorityService } from '../../../services/authority/authority.service';
import { Location, NgIf } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from '../../../services/notification.service';
import { ModuleResponse } from '../../../models/module/ModuleResponse';
import { AuthorityTypeResponse } from '../../../models/authorityType/AuthorityTypeResponse';
import { AuthorityRequest } from '../../../models/authority/AuthorityRequest';
import { AuthorityResponse } from '../../../models/authority/AuthorityResponse';

@Component({
    selector: 'app-update-authority',
    imports: [Button, FormsModule, InputText, ReactiveFormsModule, Select, Toolbar, NgIf],
    templateUrl: './update-authority.component.html',
    styleUrl: './update-authority.component.scss'
})
export class UpdateAuthorityComponent implements OnInit {
    modules: ModuleResponse[] = [];
    authorityTypes: AuthorityTypeResponse[] = [];
    authority!: AuthorityRequest;
    authorityId!: any;
    isLoading = true;
    errorMessage = '';
    updateAuthority: FormGroup | any;
    authorityResponse!: AuthorityResponse;
    originalValues = { libelle: '', moduleId: 0, authorityTypeId: 0 };

    constructor(
        private moduleService: ModulesService,
        private authorityTypeService: AuthorityTypeService,
        private authorityService: AuthorityService,
        private location: Location,
        private router: Router,
        private route: ActivatedRoute,
        private notificationService: NotificationService
    ) {
        this.authority = {
            libelle: '',
            moduleId: 0,
            authorityTypeId: 0
        };
        this.updateAuthority = new FormGroup({
            libelle: new FormControl('', [Validators.required, Validators.minLength(5)]),
            moduleId: new FormControl('', [Validators.required]),
            authorityTypeId: new FormControl('', [Validators.required])
        });
    }

    ngOnInit(): void {
        this.authorityId = this.route.snapshot.paramMap.get('id');
        this.getModules();
        this.getAuthorityTypes();
        this.getAuthorityById();
    }

    getModules() {
        this.moduleService.getModules().subscribe({
            next: (data) => {
                this.modules = data;
                this.isLoading = false;
            },
            error: (error) => {
                this.errorMessage = error;
                this.isLoading = false;
            }
        });
    }

    getAuthorityTypes() {
        this.authorityTypeService.getAuthorityTypes().subscribe({
            next: (data) => {
                this.authorityTypes = data;
                this.isLoading = false;
            },
            error: (error) => {
                this.errorMessage = error;
                this.isLoading = false;
            }
        });
    }

    cancel() {
        this.location.back();
    }

    getAuthorityById() {
        this.authorityService.getAuthorityById(this.authorityId).subscribe({
            next: (data) => {
                this.authorityResponse = data;
                this.originalValues = {
                    libelle: this.authorityResponse.libelle,
                    moduleId: this.authorityResponse.module.id,
                    authorityTypeId: this.authorityResponse.authorityType.id
                };
                this.updateAuthority.patchValue({
                    libelle: this.authorityResponse.libelle,
                    moduleId: this.authorityResponse.module.id,
                    authorityTypeId: this.authorityResponse.authorityType.id
                });
                this.isLoading = false;
            },
            error: (error) => {
                this.errorMessage = error;
                this.isLoading = false;
            }
        });
    }

    updateAuthoritys() {
        this.authority.libelle = this.updateAuthority.get('libelle').value;
        this.authority.moduleId = this.updateAuthority.get('moduleId').value;
        this.authority.authorityTypeId = this.updateAuthority.get('authorityTypeId').value;
        this.authorityService.updateAuthority(this.authorityId, this.authority).subscribe({
            next: (data) => {
                this.authorityResponse = data;
                this.isLoading = false;
                this.router.navigate(['/authoritys']);
                this.notificationService.showSuccess('Succès', 'Authority updated successfully');
            },
            error: (error) => {
                this.errorMessage = error;
                this.isLoading = false;
                this.notificationService.showError('Error', `Failed to update Authority: ${this.errorMessage}`);
            }
        });
    }

    hasChanged(): boolean {
        const formValues = this.updateAuthority.value;
        return formValues.libelle !== this.originalValues.libelle || formValues.moduleId !== this.originalValues.moduleId;
    }
}
