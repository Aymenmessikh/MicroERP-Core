import { Component, OnInit } from '@angular/core';
import { Button } from 'primeng/button';
import { InputText } from 'primeng/inputtext';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Toolbar } from 'primeng/toolbar';
import { ModuleResponse } from '../../../models/module/ModuleResponse';
import { ModulesService } from '../../../services/module/modules.service';
import { Location, NgIf } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from '../../../services/notification.service';
import { ModuleRequest } from '../../../models/module/ModuleRequest';

@Component({
    selector: 'app-update-module',
    imports: [Button, InputText, ReactiveFormsModule, Toolbar, NgIf],
    templateUrl: './update-module.component.html',
    styleUrl: './update-module.component.scss'
})
export class UpdateModuleComponent implements OnInit {
    moduleResponse!: ModuleResponse;
    module!: ModuleRequest;
    isLoading = true;
    errorMessage = '';
    updateModuleForm: FormGroup | any;
    moduleId: any;
    originalValues = { moduleName: '', moduleCode: '', uri: '', color: '', icon: '' };

    constructor(
        private moduleService: ModulesService,
        private location: Location,
        private router: Router,
        private route: ActivatedRoute,
        private notificationService: NotificationService
    ) {
        this.module = {
            color: '',
            icon: '',
            moduleName: '',
            uri: '',
            moduleCode: ''
        };
        this.updateModuleForm = new FormGroup({
            moduleName: new FormControl('', [Validators.required, Validators.minLength(5)]),
            moduleCode: new FormControl('', [Validators.required]),
            uri: new FormControl('', [Validators.required]),
            color: new FormControl('', [Validators.required]),
            icon: new FormControl('', [Validators.required])
        });
    }

    ngOnInit(): void {
        this.moduleId = this.route.snapshot.paramMap.get('id');
        console.log('Module ID:', this.moduleId);
        this.getModuleById();
    }

    cancel() {
        this.location.back();
    }

    getModuleById() {
        this.moduleService.getModuleById(this.moduleId).subscribe({
            next: (data) => {
                this.moduleResponse = data;

                this.originalValues = {
                    moduleName: this.moduleResponse.moduleName,
                    moduleCode: this.moduleResponse.moduleCode,
                    uri: this.moduleResponse.uri,
                    color: this.moduleResponse.color,
                    icon: this.moduleResponse.icon
                };

                // Populate the form fields with existing values
                this.updateModuleForm.patchValue({
                    moduleName: this.moduleResponse.moduleName,
                    moduleCode: this.moduleResponse.moduleCode, // Set default selected module
                    uri: this.moduleResponse.uri, // Set default selected module
                    color: this.moduleResponse.color, // Set default selected module
                    icon: this.moduleResponse.icon // Set default selected module
                });

                this.isLoading = false;
            },
            error: (error) => {
                this.errorMessage = error;
                this.isLoading = false;
            }
        });
    }

    hasChanged(): boolean {
        const formValues = this.updateModuleForm.value;
        return (
            formValues.moduleName !== this.originalValues.moduleName ||
            formValues.moduleCode !== this.originalValues.moduleCode ||
            formValues.uri !== this.originalValues.uri ||
            formValues.color !== this.originalValues.color ||
            formValues.icon !== this.originalValues.icon
        );
    }

    updateModule() {
        this.module.moduleName = this.updateModuleForm.get('moduleName').value;
        this.module.moduleCode = this.updateModuleForm.get('moduleCode').value;
        this.module.uri = this.updateModuleForm.get('uri').value;
        this.module.color = this.updateModuleForm.get('color').value;
        this.module.icon = this.updateModuleForm.get('icon').value;
        this.moduleService.updateModule(this.moduleId, this.module).subscribe({
            next: (data) => {
                this.moduleResponse = data;
                this.router.navigate(['/modules/detail', this.moduleId]);
                this.notificationService.showSuccess('Succès', 'Module updated successfully');
            },
            error: (error) => {
                this.notificationService.showError('Error', 'Failed to update Module');
                this.errorMessage = error;
            }
        });
    }
}
