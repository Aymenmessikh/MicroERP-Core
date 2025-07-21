import { Component, OnInit } from '@angular/core';
import { Button } from 'primeng/button';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputText } from 'primeng/inputtext';
import { Select } from 'primeng/select';
import { Toolbar } from 'primeng/toolbar';
import { ModuleResponse } from '../../../models/module/ModuleResponse';
import { RoleRequest } from '../../../models/role/RoleRequest';
import { RoleResponse } from '../../../models/role/RoleResponse';
import { ModulesService } from '../../../services/module/modules.service';
import { RolesService } from '../../../services/role/roles.service';
import { Location, NgIf } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from '../../../services/notification.service';

@Component({
    selector: 'app-update-role',
    imports: [Button, FormsModule, InputText, ReactiveFormsModule, Select, Toolbar, NgIf],
    templateUrl: './update-role.component.html',
    styleUrl: './update-role.component.scss'
})
export class UpdateRoleComponent implements OnInit {
    modules: ModuleResponse[] = [];
    role!: RoleRequest;
    isLoading = true;
    errorMessage = '';
    updateRole: FormGroup | any;
    roleResponse!: RoleResponse;
    roleId: any;
    originalValues = { libelle: '', moduleId: 0 };

    constructor(
        private moduleService: ModulesService,
        private rolesService: RolesService,
        private location: Location,
        private router: Router,
        private route: ActivatedRoute,
        private notificationService: NotificationService
    ) {
        this.role = {
            libelle: '',
            moduleId: 0
        };
        this.updateRole = new FormGroup({
            libelle: new FormControl('', [Validators.required, Validators.minLength(5)]),
            moduleId: new FormControl('', [Validators.required])
        });
    }

    ngOnInit(): void {
        this.roleId = this.route.snapshot.paramMap.get('id');
        console.log('Role ID:', this.roleId);
        this.getModules();
        this.getRoleById();
    }

    cancel() {
        this.location.back();
    }

    getRoleById() {
        this.rolesService.getRoleById(this.roleId).subscribe({
            next: (data) => {
                this.roleResponse = data;

                this.originalValues = {
                    libelle: this.roleResponse.libelle,
                    moduleId: this.roleResponse.module.id
                };

                // Populate the form fields with existing values
                this.updateRole.patchValue({
                    libelle: this.roleResponse.libelle,
                    moduleId: this.roleResponse.module.id // Set default selected module
                });

                this.isLoading = false;
            },
            error: (error) => {
                this.errorMessage = error;
                this.isLoading = false;
            }
        });
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

    hasChanged(): boolean {
        const formValues = this.updateRole.value;
        return formValues.libelle !== this.originalValues.libelle || formValues.moduleId !== this.originalValues.moduleId;
    }

    updateRoles() {
        this.role.libelle = this.updateRole.get('libelle').value;
        this.role.moduleId = this.updateRole.get('moduleId').value;
        this.rolesService.updateRole(this.roleId, this.role).subscribe({
            next: (data) => {
                this.roleResponse = data;
                this.router.navigate(['/roles/detail', this.roleId]);
                this.notificationService.showSuccess('Succès', 'Role updated successfully');
            },
            error: (error) => {
                this.notificationService.showError('Error', 'Failed to update role');
                this.errorMessage = error;
            }
        });
    }
}
