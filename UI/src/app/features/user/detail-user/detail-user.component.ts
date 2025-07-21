import { Component, OnInit } from '@angular/core';
import { UsersService } from '../../../services/user/users.service';
import { Users } from '../../../models/user/Users';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { Toolbar } from 'primeng/toolbar';
import { Button } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { ProfileResponse } from '../../../models/profile/ProfileResponse';
import { Dialog } from 'primeng/dialog';
import { InputText } from 'primeng/inputtext';
import { Select } from 'primeng/select';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { GroupeResponse } from '../../../models/groupe/GroupeResponse';
import { TreeNode } from 'primeng/api';
import { NotificationService } from '../../../services/notification.service';
import { ProfilesService } from '../../../services/profile/profiles.service';
import { ProfileRequest } from '../../../models/profile/ProfileRequest';
import { NgIf } from '@angular/common';
import { TreeSelect } from 'primeng/treeselect';
import { GroupesService } from '../../../services/groupe/groupes.service';
import { BlockUI } from 'primeng/blockui';

@Component({
    selector: 'app-detail-user',
    imports: [Toolbar, Button, TableModule, Dialog, InputText, Select, ReactiveFormsModule, NgIf, TreeSelect, RouterOutlet, BlockUI],
    templateUrl: './detail-user.component.html',
    styleUrl: './detail-user.component.scss'
})
export class DetailUserComponent implements OnInit {
    idUser!: any;
    user: Users = {
        actif: false,
        email: '',
        id: 0,
        lastName: '',
        phoneNumber: '',
        profiles: [],
        userName: '',
        uuid: '',
        firstName: '',
        // @ts-ignore
        actifProfile: undefined
    };
    errorMessage = '';
    profiles!: ProfileResponse[];
    profileDialog = false;
    //createProfile
    profileRequest!: ProfileRequest;
    createProfileForm: FormGroup | any;
    profile!: ProfileResponse;
    users: Users[] = [];
    usersTreeNodes: TreeNode[] = [];
    idProfil: number = -1;
    selectedProfileLabel: string = 'Sélectionnez un profil';
    //Update
    UpdateprofileDialog: boolean = false;
    updateProfileForm: FormGroup | any;
    idUpdatePofile!: number;
    updateProfileResponse!:ProfileResponse;
    //groupes
    groupes: GroupeResponse[] = [];
    isLoading: boolean = true;

    displayConfirmation: boolean = false;
    displayActifConfirmation: boolean = false;
    idConfirmation: number = 0;
    yesConfirmation!: string;
    noConfirmation!: string;
    messageConfirmation: string = '';
    actifValue!: boolean;

    displayActifProfileConfirmation: boolean = false;
    //
    originalValues = { libelle: '', groupeId: 0 };
    constructor(
        private userService: UsersService,
        private route: ActivatedRoute,
        private notificationService: NotificationService,
        private router: Router,
        private profilesService: ProfilesService,
        private groupeService: GroupesService
    ) {
        this.profileRequest = {
            libelle: '',
            userId: 0,
            groupId: 0
        };
        this.createProfileForm = new FormGroup({
            libelle: new FormControl('', [Validators.required, Validators.minLength(5)]),
            groupId: new FormControl('')
        });
        this.updateProfileForm = new FormGroup({
            libelle: new FormControl('', [Validators.required, Validators.minLength(5)]),
            groupId: new FormControl('')
        });
    }

    ngOnInit(): void {
        this.idUser = this.route.snapshot.paramMap.get('id');
        this.getUserById();
    }

    getUserById() {
        this.userService.getUserById(this.idUser).subscribe({
            next: (result) => {
                this.user = result;
                this.profiles = this.user.profiles;
                this.isLoading = false;
            },
            error: (error) => {
                this.errorMessage = error;
            }
        });
    }

    showProfilDetails(id: number) {
        this.router.navigate(['/profils/detail', id]);
    }

    updateProfile(id: number) {
        this.UpdateprofileDialog=true
        this.idUpdatePofile = id
        this.getProfileById()
        this.getGroupes()
    }

    hideDialog() {
        this.profileDialog = false;
    }

    addProfile() {
        this.getUsers();
        this.getGroupes();
    }

    //CreateProfile
    createProfile() {
        this.profileRequest.libelle = this.createProfileForm.get('libelle').value;
        this.profileRequest.userId = this.idUser;
        this.profileRequest.groupId = this.createProfileForm.get('groupId').value;

        this.profilesService.createProfile(this.idProfil, this.profileRequest).subscribe({
            next: (data) => {
                this.profile = data;
                this.createProfileForm.reset();
                this.notificationService.showSuccess('Succès', 'Profile created successfully');
                this.getUserById();
                this.profileDialog = false;
            },
            error: (error) => {
                console.error('Error creating Profile:', error);
                this.notificationService.showError('Error', 'Failed to create Profile');
                this.profileDialog = false;
            }
        });
    }
    getProfileById() {
        this.profilesService.getProfileById(this.idUpdatePofile).subscribe({
            next: (data) => {
                this.updateProfileResponse = data;
                this.originalValues = {
                    libelle: this.updateProfileResponse.libelle,
                    groupeId: this.updateProfileResponse.groupeResponse.id
                };

                this.updateProfileForm.patchValue({
                    libelle: this.updateProfileResponse.libelle,
                    groupId: this.updateProfileResponse.groupeResponse.id
                });
            },error: (error) => {
                this.errorMessage = error;
                this.notificationService.showError('Error', 'Failed to get Profile');
            }
        })
    }
    UpdateProfile() {
        this.profileRequest.libelle = this.updateProfileForm.get('libelle').value;
        this.profileRequest.groupId = this.updateProfileForm.get('groupId').value;

        this.profilesService.updateProfile(this.idUpdatePofile, this.profileRequest).subscribe({
            next: (data) => {
                this.updateProfileResponse = data;
                this.updateProfileForm.reset();
                this.notificationService.showSuccess('Succès', 'Profile updated successfully');
                this.getUserById();
                this.UpdateprofileDialog = false;
            },
            error: (error) => {
                console.error('Error creating Profile:', error);
                this.notificationService.showError('Error', 'Failed to update Profile');
                this.UpdateprofileDialog = false;
            }
        });
    }
    hasChanged(): boolean {
        const formValues = this.updateProfileForm.value;
        return formValues.libelle !== this.originalValues.libelle || formValues.groupId !== this.originalValues.groupeId;
    }

    getUsers(): void {
        this.userService.getListUsers().subscribe({
            next: (data) => {
                this.users = data;
                this.mapUsersToTreeNodes();
            },
            error: (error) => {
                this.errorMessage = error;
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
                data: { idProfile: profile.id, ...profile } //
            }))
        }));
        this.profileDialog = true;
    }

    onProfileSelect(event: any) {
        if (event.node && event.node.data) {
            // Vérifie que le nœud sélectionné est un profil (il ne doit pas avoir de 'children')
            if (!event.node.children) {
                this.selectedProfileLabel = event.node.label;
                this.idProfil = event.node.data.idProfile;
                console.log('ID du profil sélectionné:', this.idProfil);
            }
        }
    }

    getGroupes() {
        this.groupeService.getAllGroupes().subscribe({
            next: (data) => {
                this.groupes = data;
                console.log(this.groupes);
            },
            error: (error) => {
                this.errorMessage = error;
            }
        });
    }

    deleteProfile(): void {
        this.profilesService.deleteProfile(this.idConfirmation).subscribe({
            next: () => {
                this.profiles = this.profiles.filter((profile) => profile.id !== this.idConfirmation);
                this.notificationService.showSuccess('Success', 'Profile deleted successfully');
                this.cancelDelete();
            },
            error: (err) => {
                this.errorMessage = err;
                this.notificationService.showError('Error', `Failed to delete Profile: ${this.errorMessage}`);
            }
        });
    }

    confirmDelete(id: number) {
        this.displayConfirmation = true;
        this.idConfirmation = id;
    }

    cancelDelete() {
        this.displayConfirmation = false;
    }

    confirmActifAndDisactif(id: number, actif: boolean) {
        this.displayActifConfirmation = true;
        this.messageConfirmation = actif ? 'Are you sure you want to disable this User?' : 'Are you sure you want to enable this User?';
        this.noConfirmation = 'Cancel';
        this.idConfirmation = id;
        this.yesConfirmation = actif ? 'Disactf' : 'Actif';
        this.actifValue = actif;
    }

    confirmActifAndDisactifProfile_(id: number, actif: boolean) {
        this.displayActifProfileConfirmation = true;
        this.messageConfirmation = actif ? 'Are you sure you want to disable this Profile?' : 'Are you sure you want to enable this Profile?';
        this.noConfirmation = 'Cancel';
        this.idConfirmation = id;
        this.yesConfirmation = actif ? 'Disactf' : 'Actif';
        this.actifValue = actif;
    }

    cancelActifDisactif() {
        this.displayActifConfirmation = false;
    }

    cancelProfileActifDisactif() {
        this.displayActifProfileConfirmation = false;
    }

    toggleUserStatus() {
        const action = this.userService.enableDisableUser(this.idConfirmation, this.actifValue);

        const successMessage = this.actifValue ? 'User disabled successfully' : 'User enabled successfully';

        const errorMessage = this.actifValue ? 'Failed to disable User' : 'Failed to enable User';

        action.subscribe({
            next: () => {
                this.notificationService.showSuccess('Success', successMessage);
                this.cancelActifDisactif();
                this.user.actif = !this.user.actif;
            },
            error: (err) => {
                console.error('Error updating user:', err);
                this.notificationService.showError('Error', `${errorMessage}: ${err.message || err}`);
            }
        });
    }

    toggleProfileStatus() {
        const action = this.actifValue ? this.profilesService.disableProfile(this.idConfirmation) : this.profilesService.enableProfile(this.idConfirmation);

        const successMessage = this.actifValue ? 'Profile disabled successfully' : 'Profile enabled successfully';

        const errorMessage = this.actifValue ? 'Failed to disable Profile' : 'Failed to enable Profile';

        action.subscribe({
            next: () => {
                this.notificationService.showSuccess('Success', successMessage);
                this.cancelProfileActifDisactif();

                // Find the index of the profile to update
                const index = this.profiles.findIndex((pr) => pr.id === this.idConfirmation);
                if (index !== -1) {
                    // Create a new object with the updated `actif` value
                    const updatedProfile = {
                        ...this.profiles[index],
                        actif: !this.actifValue // Toggle the `actif` value
                    };

                    // Create a new array with the updated profile
                    this.profiles = [...this.profiles.slice(0, index), updatedProfile, ...this.profiles.slice(index + 1)];
                }
            },
            error: (err) => {
                console.error('Error updating authority:', err);
                this.notificationService.showError('Error', `${errorMessage}: ${err.message || err}`);
            }
        });
    }
}
