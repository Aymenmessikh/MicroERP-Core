import { Component, OnInit } from '@angular/core';
import { Button } from 'primeng/button';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputText } from 'primeng/inputtext';
import { Select } from 'primeng/select';
import { Toolbar } from 'primeng/toolbar';
import { ModuleResponse } from '../../../models/module/ModuleResponse';
import { ModulesService } from '../../../services/module/modules.service';
import { Location, NgIf } from '@angular/common';
import { Router } from '@angular/router';
import { NotificationService } from '../../../services/notification.service';
import { AuthorityRequest } from '../../../models/authority/AuthorityRequest';
import { AuthorityResponse } from '../../../models/authority/AuthorityResponse';
import { AuthorityTypeResponse } from '../../../models/authorityType/AuthorityTypeResponse';
import { AuthorityService } from '../../../services/authority/authority.service';
import { AuthorityTypeService } from '../../../services/authorityType/authority-type.service';

@Component({
    selector: 'app-create-authority',
    imports: [Button, FormsModule, InputText, ReactiveFormsModule, Select, Toolbar, NgIf],
    templateUrl: './create-authority.component.html',
    styleUrl: './create-authority.component.scss'
})
export class CreateAuthorityComponent implements OnInit {
    modules: ModuleResponse[] = [];
    authorityTypes: AuthorityTypeResponse[] = [];
    authority!: AuthorityRequest;
    isLoading = true;
    errorMessage = '';
    createAuthority: FormGroup | any;
    authorityResponse!: AuthorityResponse;

    constructor(
        private moduleService: ModulesService,
        private authorityTypeService: AuthorityTypeService,
        private authorityService: AuthorityService,
        private location: Location,
        private router: Router,
        private notificationService: NotificationService
    ) {
        this.authority = {
            libelle: '',
            moduleId: 0,
            authorityTypeId: 0
        };
        this.createAuthority = new FormGroup({
            libelle: new FormControl('', [Validators.required, Validators.minLength(5)]),
            moduleId: new FormControl('', [Validators.required]),
            authorityTypeId: new FormControl('', [Validators.required])
        });
    }

    ngOnInit(): void {
        this.getModules();
        this.getAuthorityTypes();
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

    createAuthoritys() {
        this.authority.libelle = this.createAuthority.get('libelle').value;
        this.authority.moduleId = this.createAuthority.get('moduleId').value;
        this.authority.authorityTypeId = this.createAuthority.get('authorityTypeId').value;
        this.authorityService.createAuthority(this.authority).subscribe({
            next: (data) => {
                this.authorityResponse = data;
                this.createAuthority.reset();

                this.router.navigate(['/authoritys']);
                this.notificationService.showSuccess('Succès', 'Authority created successfully');
            },
            error: (error) => {
                this.errorMessage = error;
                this.notificationService.showError('Error', `Failed to create Authority: ${this.errorMessage}`);
            }
        });
    }

    cancel() {
        this.location.back();
    }
}
