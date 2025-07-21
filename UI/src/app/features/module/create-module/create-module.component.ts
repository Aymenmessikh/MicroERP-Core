import { Component, OnInit } from '@angular/core';
import { Button } from 'primeng/button';
import { InputText } from 'primeng/inputtext';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Toolbar } from 'primeng/toolbar';
import { FileUpload } from 'primeng/fileupload';
import { ModuleResponse } from '../../../models/module/ModuleResponse';
import { ModulesService } from '../../../services/module/modules.service';
import { Location, NgIf } from '@angular/common';
import { Router } from '@angular/router';
import { NotificationService } from '../../../services/notification.service';
import { ModuleRequest } from '../../../models/module/ModuleRequest';

@Component({
    selector: 'app-create-module',
    imports: [Button, InputText, ReactiveFormsModule, Toolbar, NgIf, FileUpload],
    templateUrl: './create-module.component.html',
    styleUrl: './create-module.component.scss'
})
export class CreateModuleComponent implements OnInit {
    moduleResponse!: ModuleResponse;
    module!: ModuleRequest;
    isLoading = false;
    errorMessage = '';
    createModuleForm: FormGroup | any;
    selectedIcon: File | null = null;

    constructor(
        private moduleService: ModulesService,
        private location: Location,
        private router: Router,
        private notificationService: NotificationService
    ) {
        this.module = {
            color: '',
            icon: '',
            moduleCode: '',
            uri: '',
            moduleName: ''
        };
        this.createModuleForm = new FormGroup({
            moduleName: new FormControl('', [Validators.required, Validators.minLength(5)]),
            moduleCode: new FormControl('', [Validators.required]),
            uri: new FormControl('', [Validators.required]),
            color: new FormControl('', [Validators.required]),
            icon: new FormControl(null, [Validators.required])
        });
    }

    ngOnInit(): void {}

    onIconSelect(event: any) {
        if (event.files && event.files.length > 0) {
            this.selectedIcon = event.files[0];
            this.createModuleForm.patchValue({
                icon: this.selectedIcon
            });
        }
    }

    createModule() {
        if (this.createModuleForm.valid && this.selectedIcon) {
            this.isLoading = true;

            const formData = new FormData();
            formData.append('moduleName', this.createModuleForm.get('moduleName').value);
            formData.append('moduleCode', this.createModuleForm.get('moduleCode').value);
            formData.append('uri', this.createModuleForm.get('uri').value);
            formData.append('color', this.createModuleForm.get('color').value);
            formData.append('icon', this.selectedIcon);

            this.moduleService.createModuleWithIcon(formData).subscribe({
                next: (data) => {
                    this.moduleResponse = data;
                    this.createModuleForm.reset();
                    this.selectedIcon = null;
                    this.isLoading = false;
                    this.notificationService.showSuccess('Succès', 'Module created successfully');
                    this.router.navigate(['/modules/detail', this.moduleResponse.id]);
                },
                error: (error) => {
                    this.isLoading = false;
                    console.error('Error creating module:', error);
                    this.notificationService.showError('Error', 'Failed to create module');
                }
            });
        } else {
            this.notificationService.showError('Error', 'Please fill all required fields');
        }
    }

    cancel() {
        this.location.back();
    }
}
