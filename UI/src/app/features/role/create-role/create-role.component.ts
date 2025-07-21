import { Component, OnInit } from '@angular/core';
import { Location, NgIf } from '@angular/common';
import { InputText } from 'primeng/inputtext';
import { Toolbar } from 'primeng/toolbar';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Select } from 'primeng/select';
import { ModulesService } from '../../../services/module/modules.service';
import { ModuleResponse } from '../../../models/module/ModuleResponse';
import { RoleRequest } from '../../../models/role/RoleRequest';
import { RolesService } from '../../../services/role/roles.service';
import { RoleResponse } from '../../../models/role/RoleResponse';
import { Button } from 'primeng/button';
import { Router } from '@angular/router';
import { NotificationService } from '../../../services/notification.service';

@Component({
    selector: 'app-create-role',
    imports: [InputText, Toolbar, ReactiveFormsModule, FormsModule, Select, Button, NgIf],
    templateUrl: './create-role.component.html',
    styleUrl: './create-role.component.scss'
})
export class CreateRoleComponent implements OnInit {
    modules: ModuleResponse[] = [];
    role!: RoleRequest;
    isLoading = true;
    errorMessage = '';
    createRole: FormGroup | any;
    roleResponse!: RoleResponse;

    constructor(
        private moduleService: ModulesService,
        private rolesService: RolesService,
        private location: Location,
        private router: Router,
        private notificationService: NotificationService
    ) {
        this.role = {
            libelle: '',
            moduleId: 0
        };
        this.createRole = new FormGroup({
            libelle: new FormControl('', [Validators.required, Validators.minLength(5)]),
            moduleId: new FormControl('', [Validators.required])
        });
    }

    ngOnInit(): void {
        this.getModules();
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

    createRoles() {
        this.role.libelle = this.createRole.get('libelle').value;
        this.role.moduleId = this.createRole.get('moduleId').value;

        this.rolesService.createRole(this.role).subscribe({
            next: (data) => {
                this.roleResponse = data;
                this.createRole.reset();
                this.notificationService.showSuccess('Succès', 'Role created successfully');
                this.router.navigate(['/roles/detail', this.roleResponse.id]);
            },
            error: (error) => {
                console.error('Error creating role:', error);
                this.notificationService.showError('Error', 'Failed to create role');
            }
        });
    }

    cancel() {
        this.location.back();
    }
}
