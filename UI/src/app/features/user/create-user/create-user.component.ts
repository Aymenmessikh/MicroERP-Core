import { Component, OnInit } from '@angular/core';
import { InputText } from 'primeng/inputtext';
import { AbstractControl, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Button } from 'primeng/button';
import { Location, NgIf } from '@angular/common';
import { Toolbar } from 'primeng/toolbar';
import { TreeSelectModule } from 'primeng/treeselect';
import { UsersService } from '../../../services/user/users.service';
import { Users } from '../../../models/user/Users';
import { TreeNode } from 'primeng/api';
import { UserRequest } from '../../../models/user/UserRequest';
import { Router } from '@angular/router';
import { NotificationService } from '../../../services/notification.service';

export function passwordMatchValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const password = control.get('password')?.value;
        const confirmPassword = control.get('confirmPassword')?.value;

        return password === confirmPassword ? null : { passwordMismatch: true };
    };
}

@Component({
    selector: 'app-create-user',
    imports: [InputText, ReactiveFormsModule, Button, NgIf, Toolbar, TreeSelectModule, FormsModule],
    templateUrl: './create-user.component.html',
    styleUrl: './create-user.component.scss'
})
export class CreateUserComponent implements OnInit {
    createUserForm: FormGroup | any;
    user!: UserRequest;
    userResponse!: Users;
    isLoading = true;
    errorMessage = '';
    users: Users[] = [];
    usersTreeNodes: TreeNode[] = [];
    idProfil!: number;
    selectedProfileLabel: string = 'Sélectionnez un profil';
   // password = true;

    constructor(
        private fb: FormBuilder,
        private userService: UsersService,
        private location: Location,
        private router: Router,
        private notificationService: NotificationService
    ) {}

    ngOnInit(): void {
        this.getUsers();
        this.createUserForm = this.fb.group(
            {
                firstName: ['', Validators.required],
                lastName: ['', Validators.required],
                userName: ['', Validators.required],
                email: ['', [Validators.required, Validators.email]],
                phoneNumber: ['', Validators.required],
                address:['',Validators.required],
             //   password: ['', [Validators.required, Validators.minLength(8)]],
               // confirmPassword: ['', Validators.required],
                profileId: [-1]
            },
            // { validators: passwordMatchValidator() }
        );

        this.user = {
            email: '',
            lastName: '',
            address: '',
            phoneNumber: '',
            userName: '',
            firstName: ''
        };
    }

    getUsers(): void {
        this.userService.getListUsers().subscribe({
            next: (data) => {
                this.users = data;
                this.mapUsersToTreeNodes();
                this.isLoading = false;
            },
            error: (error) => {
                this.errorMessage = error;
                this.isLoading = false;
            }
        });
    }

    mapUsersToTreeNodes(): void {
        this.usersTreeNodes = this.users.map((user) => ({
            label: user.userName,
            data: user,
            selectable: false,
            children: user.profiles.map((profile) => ({
                label: profile.libelle,
                data: { idProfile: profile.id, ...profile } // Vérifiez que `id` est bien présent
            }))
        }));
    }

    onProfileSelect(event: any) {
        if (event.node && event.node.data) {
            if (event.node.children) {
                const firstProfile = event.node.children[0];
                this.createUserForm.patchValue({ profileId: firstProfile.data.idProfile });
                this.selectedProfileLabel = firstProfile.label;
                this.idProfil = firstProfile.data.idProfile;
            } else {
                this.createUserForm.patchValue({ profileId: event.node.data.idProfile });
                this.selectedProfileLabel = event.node.label;
                this.idProfil = event.node.data.idProfile;
            }
        }
    }

    onSubmit(): void {
        if (this.createUserForm.valid) {
            this.user.firstName = this.createUserForm.get('firstName').value;
            this.user.lastName = this.createUserForm.get('lastName').value;
            this.user.phoneNumber = this.createUserForm.get('phoneNumber').value;
            this.user.userName = this.createUserForm.get('userName').value;
            this.user.email = this.createUserForm.get('email').value;
            this.user.address = this.createUserForm.get('address').value;

            console.log(this.user);
            this.userService.createUser(this.user, this.createUserForm.get('profileId').value).subscribe({
                next: (data) => {
                    this.userResponse = data;
                    this.createUserForm.reset();
                    this.notificationService.showSuccess('Succès', 'User created successfully');
                    this.router.navigate(['/users/detail', this.userResponse.id]);
                },
                error: (error) => {
                    this.notificationService.showError('Error', 'Failed to create User');
                }
            });
        }
    }

    onCancel(): void {
        this.createUserForm.reset();
        this.location.back();
    }
}
