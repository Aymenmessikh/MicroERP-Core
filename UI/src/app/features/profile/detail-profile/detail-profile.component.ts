import { Component, OnInit } from '@angular/core';
import { ProfileResponse } from '../../../models/profile/ProfileResponse';
import { ActivatedRoute } from '@angular/router';
import { ProfilesService } from '../../../services/profile/profiles.service';
import { NotificationService } from '../../../services/notification.service';
import { Toolbar } from 'primeng/toolbar';
import { Button } from 'primeng/button';
import { Tab, TabList, TabPanel, TabPanels, Tabs } from 'primeng/tabs';
import { TableModule } from 'primeng/table';
import { AuthorityResponse } from '../../../models/authority/AuthorityResponse';
import { AuthorityService } from '../../../services/authority/authority.service';
import { PickList } from 'primeng/picklist';
import { ProfileAuthorityResponse } from '../../../models/profile/ProfileAuthorityResponse';
import { RoleResponse } from '../../../models/role/RoleResponse';
import { RolesService } from '../../../services/role/roles.service';
import { ModuleResponse } from '../../../models/module/ModuleResponse';
import { ModulesService } from '../../../services/module/modules.service';
import { BlockUI } from 'primeng/blockui';
import { Dialog } from 'primeng/dialog';

@Component({
    selector: 'app-detail-profile',
    imports: [Toolbar, Button, Tabs, TabList, Tab, TabPanels, TabPanel, TableModule, PickList, BlockUI, Dialog],
    templateUrl: './detail-profile.component.html',
    styleUrl: './detail-profile.component.scss'
})
export class DetailProfileComponent implements OnInit {
    profile!: ProfileResponse;
    idProfile!: any;
    isLoading: boolean = true;
    //Authz
    profileAuthority: ProfileAuthorityResponse[] = [];
    aythorityProfile!: AuthorityResponse[];
    authority: AuthorityResponse[] = [];
    modulesAuthoritiesExcludingProfileAuthorities: AuthorityResponse[] = [];
    //Role
    roleProfile: RoleResponse[] = [];
    modulesRolesExcludingProfileRole: RoleResponse[] = [];
    //Moudle
    moduleProfile: ModuleResponse[] = [];
    modulesExcludingModuleProfile: ModuleResponse[] = [];
    modules: ModuleResponse[] = [];

    displayActifConfirmation: boolean = false;
    idConfirmation: number = 0;
    yesConfirmation!: string;
    noConfirmation!: string;
    messageConfirmation: string = '';
    actifValue!: boolean;

    constructor(
        private route: ActivatedRoute,
        private notificationService: NotificationService,
        private authorityService: AuthorityService,
        private profileService: ProfilesService,
        private roleService: RolesService,
        private moduleService: ModulesService
    ) {}

    ngOnInit(): void {
        this.idProfile = this.route.snapshot.paramMap.get('id');
        this.getProfileById();
        this.getModulesAuthoritiesExcludingProfileAuthorities();
        this.getModulesRolesExcludingProfileRole();
        this.getModulesExcludingModuleProfile();
    }

    getProfileById() {
        this.profileService.getProfileById(this.idProfile).subscribe(
            (profile) => {
                this.profile = profile;
                this.aythorityProfile = profile.profileAuthorityResponses.map((profileAuthority) => profileAuthority.authorityResponse);
                this.roleProfile = this.profile.roleResponses;
                this.moduleProfile = this.profile.moduleResponses;
                this.isLoading = false;
            },
            (err) => {
                console.log(err);
            }
        );
    }

    toggleProfileStatus() {
        const action = this.actifValue ? this.profileService.disableProfile(this.idConfirmation) : this.profileService.enableProfile(this.idConfirmation);

        const successMessage = this.actifValue ? 'Profile disabled successfully' : 'Profile enabled successfully';

        const errorMessage = this.actifValue ? 'Failed to disable Profile' : 'Failed to enable Profile';

        action.subscribe({
            next: () => {
                this.notificationService.showSuccess('Success', successMessage);
                this.profile.actif = !this.actifValue;
            },
            error: (err) => {
                console.error('Error updating authority:', err);
                this.notificationService.showError('Error', `${errorMessage}: ${err.message || err}`);
            }
        });
    }

    //Authz_________________
    getModulesAuthoritiesExcludingProfileAuthorities() {
        this.authorityService.getModulesAuthoritiesExcludingProfileAuthorities(this.idProfile).subscribe({
            next: (data) => {
                this.modulesAuthoritiesExcludingProfileAuthorities = data;
            },
            error: (error) => {
                console.error('Error to uplaod authoritys:', error);
            }
        });
    }

    trackByFn(index: number, item: AuthorityResponse): number {
        console.log('item', item.id);
        return item.id;
    }

    onMoveToTarget(event: any) {
        const authorityIds = event.items.map((authority: AuthorityResponse) => authority.id);
        this.profileService.grantAuthoritiesToProfile(this.idProfile, authorityIds).subscribe({
            next: (data) => {
                this.profile = data;
                this.getModulesAuthoritiesExcludingProfileAuthorities();
            },
            error: () => {
                this.notificationService.showError('Erreur', "Impossible d'ajouter les autorités");
            }
        });
    }

    onMoveToSource(event: any) {
        const authorityIds = event.items.map((authority: AuthorityResponse) => authority.id);
        console.log(authorityIds);
        this.profileService.revokeAuthoritiesFromProfile(this.idProfile, authorityIds).subscribe({
            next: (data) => {
                this.getModulesAuthoritiesExcludingProfileAuthorities();
            },
            error: () => {
                this.notificationService.showError('Erreur', 'Impossible de retirer les autorités');
            }
        });
    }

    onMoveAllToTarget(event: any) {
        const authorityIds = event.items.map((authority: AuthorityResponse) => authority.id);
        this.profileService.grantAuthoritiesToProfile(this.idProfile, authorityIds).subscribe({
            next: (data) => {
                this.getModulesAuthoritiesExcludingProfileAuthorities();
            },
            error: () => {
                this.notificationService.showError('Erreur', "Impossible d'ajouter toutes les autorités");
            }
        });
    }

    onMoveAllToSource(event: any) {
        const authorityIds = event.items.map((authority: AuthorityResponse) => authority.id);
        this.profileService.revokeAuthoritiesFromProfile(this.idProfile, authorityIds).subscribe({
            next: (data) => {
                this.getModulesAuthoritiesExcludingProfileAuthorities();
            },
            error: () => {
                this.notificationService.showError('Erreur', 'Impossible de retirer toutes les autorités');
            }
        });
    }

    //Role
    trackRoleByFn(index: number, item: RoleResponse): number {
        console.log('item', item.id);
        return item.id;
    }

    getModulesRolesExcludingProfileRole() {
        this.roleService.getModulesRolesExcludingProfileRole(this.idProfile).subscribe({
            next: (data) => {
                this.modulesRolesExcludingProfileRole = data;
            },
            error: (error) => {
                console.error('Error to uplaod Roles:', error);
            }
        });
    }

    onMoveRoleToTarget(event: any) {
        const roleIds = event.items.map((role: RoleResponse) => role.id);
        console.log(roleIds);
        this.profileService.grantRolesToProfile(this.idProfile, roleIds).subscribe({
            next: (data) => {
                this.getModulesRolesExcludingProfileRole();
            },
            error: () => {
                this.notificationService.showError('Erreur', "Impossible d'ajouter les Roles");
            }
        });
    }

    onMoveRoleToSource(event: any) {
        const roleIds = event.items.map((role: RoleResponse) => role.id);
        console.log(roleIds);
        this.profileService.revokeRolesFromProfile(this.idProfile, roleIds).subscribe({
            next: (data) => {
                this.getModulesAuthoritiesExcludingProfileAuthorities();
            },
            error: () => {
                this.notificationService.showError('Erreur', "Impossible d'ajouter toutes les Roles");
            }
        });
    }

    onMoveAllRoleToTarget(event: any) {
        const roleIds = event.items.map((role: RoleResponse) => role.id);
        this.profileService.grantRolesToProfile(this.idProfile, roleIds).subscribe({
            next: (data) => {
                this.getModulesRolesExcludingProfileRole();
            },
            error: () => {
                this.notificationService.showError('Erreur', "Impossible d'ajouter toutes les autorités");
            }
        });
    }

    onMoveAllRoleToSource(event: any) {
        const roleIds = event.items.map((role: RoleResponse) => role.id);
        this.profileService.revokeRolesFromProfile(this.idProfile, roleIds).subscribe({
            next: (data) => {
                this.getModulesRolesExcludingProfileRole();
            },
            error: () => {
                this.notificationService.showError('Erreur', 'Impossible de retirer toutes les roles');
            }
        });
    }

    //Module
    trackModuleByFn(index: number, item: RoleResponse): number {
        console.log('item', item.id);
        return item.id;
    }

    getModulesExcludingModuleProfile() {
        this.moduleService.getModulesExcludingModuleProfile(this.idProfile).subscribe({
            next: (data) => {
                this.modulesExcludingModuleProfile = data;
            },
            error: (error) => {
                console.error('Error fetching modules:', error);
            }
        });
    }

    onMoveModuleToTarget(event: any) {
        const roleIds = event.items.map((role: RoleResponse) => role.id);
        console.log(roleIds);
        this.profileService.addModuleToProfile(this.idProfile, roleIds).subscribe({
            next: (data) => {
                this.getModulesExcludingModuleProfile();
            },
            error: () => {
                this.notificationService.showError('Erreur', "Impossible d'ajouter les Modules");
            }
        });
    }

    onMoveModuleToSource(event: any) {
        const roleIds = event.items.map((role: RoleResponse) => role.id);
        console.log(roleIds);
        this.profileService.removeModulesFromProfile(this.idProfile, roleIds).subscribe({
            next: (data) => {
                this.getModulesExcludingModuleProfile();
            },
            error: () => {
                this.notificationService.showError('Erreur', "Impossible d'ajouter toutes les Modules");
            }
        });
    }

    confirmActifAndDisactif(id: number, actif: boolean) {
        this.displayActifConfirmation = true;
        this.messageConfirmation = actif ? 'Are you sure you want to disable this Profile?' : 'Are you sure you want to enable this Profile?';
        this.noConfirmation = 'Cancel';
        this.idConfirmation = id;
        this.yesConfirmation = actif ? 'Disactf' : 'Actif';
        this.actifValue = actif;
    }

    cancelActifDisactif() {
        this.displayActifConfirmation = false;
    }
}
