import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Button } from 'primeng/button';
import { InputText } from 'primeng/inputtext';
import { Toolbar } from 'primeng/toolbar';
import { UserRequest } from '../../../models/user/UserRequest';
import { ActivatedRoute, Router } from '@angular/router';
import { UsersService } from '../../../services/user/users.service';
import { Users } from '../../../models/user/Users';
import { Location } from '@angular/common';
import { NotificationService } from '../../../services/notification.service';
import { BlockUI } from 'primeng/blockui';

@Component({
    selector: 'app-update-user',
    imports: [ReactiveFormsModule, Button, InputText, Toolbar, BlockUI],
    templateUrl: './update-user.component.html',
    styleUrl: './update-user.component.scss'
})
export class UpdateUserComponent implements OnInit {
    updateUserForm: FormGroup | any;
    user!: UserRequest;
    userResponse!: Users;
    idUser!: any;
    errorMessage = '';
    originalValues = { firstName: '', lastName: '', userName: '', email: '', phoneNumber: '' };
    isLoading: boolean = true;

    constructor(
        private route: ActivatedRoute,
        private userService: UsersService,
        private notificationService: NotificationService,
        private router: Router,
        private location: Location
    ) {
        this.router = router;

        this.updateUserForm = new FormGroup({
            firstName: new FormControl('', [Validators.required]),
            lastName: new FormControl('', [Validators.required]),
            userName: new FormControl('', [Validators.required]),
            email: new FormControl('', [Validators.required, Validators.email]),
            phoneNumber: new FormControl('', [Validators.required])
        });
        this.user = {
            email: '',
            lastName: '',
            address: '',
            phoneNumber: '',
            userName: '',
            firstName: ''
        };
    }
    ngOnInit(): void {
        this.idUser = this.route.snapshot.paramMap.get('id');
        this.getUserById();
    }
    getUserById() {
        this.userService.getUserById(this.idUser).subscribe({
            next: (result) => {
                this.userResponse = result;
                this.originalValues = {
                    firstName: this.userResponse.firstName,
                    lastName: this.userResponse.lastName,
                    userName: this.userResponse.userName,
                    email: this.userResponse.email,
                    phoneNumber: this.userResponse.phoneNumber
                };
                this.updateUserForm.patchValue({
                    firstName: this.userResponse.firstName,
                    lastName: this.userResponse.lastName,
                    userName: this.userResponse.userName,
                    email: this.userResponse.email,
                    phoneNumber: this.userResponse.phoneNumber
                });
                this.isLoading = false;
            },
            error: (error) => {
                this.errorMessage = error;
            }
        });
    }
    hasChanged(): boolean {
        const formValues = this.updateUserForm.value;
        return (
            formValues.firstName !== this.originalValues.firstName ||
            formValues.lastName !== this.originalValues.lastName ||
            formValues.userName !== this.originalValues.userName ||
            formValues.email !== this.originalValues.email ||
            formValues.phoneNumber !== this.originalValues.phoneNumber
        );
    }
    updateUser() {
        if (this.updateUserForm.valid) {
            const updatedUser: UserRequest = {
                firstName: this.updateUserForm.value.firstName,
                lastName: this.updateUserForm.value.lastName,
                userName: this.updateUserForm.value.userName,
                email: this.updateUserForm.value.email,
                phoneNumber: this.updateUserForm.value.phoneNumber,
                address:''
                //password: ''
            };

            this.userService.updateUser(this.idUser, updatedUser).subscribe({
                next: (data) => {
                    this.userResponse = data;
                    this.notificationService.showSuccess('Succès', 'User updated successfully');
                },
                error: (error) => {
                    this.notificationService.showError('Error', 'Failed to update user');
                    this.errorMessage = error;
                }
            });
        } else {
            console.error('Form is not valid');
        }
    }

    cancel() {
        this.location.back();
    }
}
