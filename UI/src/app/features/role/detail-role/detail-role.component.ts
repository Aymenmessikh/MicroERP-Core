import { Component, OnInit } from '@angular/core';
import { NotificationService } from '../../../services/notification.service';
import { Toolbar } from 'primeng/toolbar';
import { RolesService } from '../../../services/role/roles.service';
import { RoleResponse } from '../../../models/role/RoleResponse';
import { ActivatedRoute } from '@angular/router';
import { Button } from 'primeng/button';
import { ConfirmationService, MessageService, PrimeTemplate } from 'primeng/api';
import { AuthorityResponse } from '../../../models/authority/AuthorityResponse';
import { PickList } from 'primeng/picklist';
import { AuthorityService } from '../../../services/authority/authority.service';
import { NgIf } from '@angular/common';
import { BlockUI } from 'primeng/blockui';
import { Dialog } from 'primeng/dialog';

@Component({
    selector: 'app-detail-role',
    imports: [Toolbar, Button, PickList, PrimeTemplate, NgIf, BlockUI, Dialog],
    templateUrl: './detail-role.component.html',
    styleUrl: './detail-role.component.scss',
    providers: [ConfirmationService, MessageService]
})
export class DetailRoleComponent implements OnInit {
    roleAuthority: AuthorityResponse[] = [];
    authority: AuthorityResponse[] = [];
    moduleAuthoritiesExcludingRoleAuthorities: AuthorityResponse[] = [];
    roleResponse: RoleResponse = {
        authoritys: [],
        module: {
            moduleName: '',
            moduleCode: '',
            id: 0,
            color: '',
            uri: '',
            icon: '',
            actif: true
        },
        id: 0,
        libelle: '',
        actif: false
    };

    roleId: any;
    moduleId!: number;
    isLoading = true;
    errorMessage = '';

    displayConfirmation: boolean = false;
    displayActifConfirmation: boolean = false;
    idConfirmation: number = 0;
    yesConfirmation!: string;
    noConfirmation!: string;
    messageConfirmation: string = '';
    actifValue!: boolean;

    constructor(
        private notificationService: NotificationService,
        private rolesService: RolesService,
        private route: ActivatedRoute,
        private authService: AuthorityService
    ) {}

    ngOnInit(): void {
        this.roleId = this.route.snapshot.paramMap.get('id');
        this.getRoleById();
    }

    getRoleById() {
        this.rolesService.getRoleById(this.roleId).subscribe({
            next: (data) => {
                this.roleResponse = data;
                this.roleAuthority = this.roleResponse.authoritys;
                this.getModuleAuthoritiesExcludingRoleAuthorities(this.roleResponse.module.id);
            },
            error: (error) => {
                this.errorMessage = error;
                this.isLoading = false;
            }
        });
    }

    confirmActifAndDisactif(id: number, actif: boolean) {
        this.displayActifConfirmation = true;
        this.messageConfirmation = actif ? 'Are you sure you want to disable this Role?' : 'Are you sure you want to enable this Role?';
        this.noConfirmation = 'Cancel';
        this.idConfirmation = id;
        this.yesConfirmation = actif ? 'Disactf' : 'Actif';
        this.actifValue = actif;
    }

    cancelActifDisactif() {
        this.displayActifConfirmation = false;
    }

    toggleRoleStatus() {
        const action = this.actifValue ? this.rolesService.disableRole(this.idConfirmation) : this.rolesService.enableRole(this.idConfirmation);

        const successMessage = this.actifValue ? 'Role disabled successfully' : 'Role enabled successfully';

        const errorMessage = this.actifValue ? 'Failed to disable Role' : 'Failed to enable Role';

        action.subscribe({
            next: () => {
                this.notificationService.showSuccess('Success', successMessage);
                this.cancelActifDisactif();
                this.roleResponse.actif = !this.actifValue;
            },
            error: (err) => {
                console.error('Error updating authority:', err);
                this.notificationService.showError('Error', `${errorMessage}: ${err.message || err}`);
            }
        });
    }

    getModuleAuthoritiesExcludingRoleAuthorities(idModule: number) {
        this.authService.getModuleAuthoritiesExcludingRoleAuthorities(this.roleId, idModule).subscribe({
            next: (data) => {
                this.moduleAuthoritiesExcludingRoleAuthorities = data;
                this.isLoading = false;
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
        this.rolesService.grantAuthoritiesToRole(this.roleId, authorityIds).subscribe({
            next: (data) => {
                this.roleResponse = data;
                this.getModuleAuthoritiesExcludingRoleAuthorities(this.moduleId);
            },
            error: () => {
                this.notificationService.showError('Erreur', "Impossible d'ajouter les autorités");
            }
        });
    }

    onMoveToSource(event: any) {
        const authorityIds = event.items.map((authority: AuthorityResponse) => authority.id);
        this.rolesService.revokeAuthoritiesFromRole(this.roleId, authorityIds).subscribe({
            next: (data) => {
                console.log('Authorities removed:', data);
                this.getModuleAuthoritiesExcludingRoleAuthorities(this.moduleId);
            },
            error: () => {
                this.notificationService.showError('Erreur', 'Impossible de retirer les autorités');
            }
        });
    }

    onMoveAllToTarget(event: any) {
        const authorityIds = event.items.map((authority: AuthorityResponse) => authority.id);
        this.rolesService.grantAuthoritiesToRole(this.roleId, authorityIds).subscribe({
            next: (data) => {
                console.log('All authoritys assigned:', data);
                this.getModuleAuthoritiesExcludingRoleAuthorities(this.moduleId);
            },
            error: () => {
                this.notificationService.showError('Erreur', "Impossible d'ajouter toutes les autorités");
            }
        });
    }

    onMoveAllToSource(event: any) {
        const authorityIds = event.items.map((authority: AuthorityResponse) => authority.id);
        this.rolesService.revokeAuthoritiesFromRole(this.roleId, authorityIds).subscribe({
            next: (data) => {
                console.log('All authoritys removed:', data);
                this.getModuleAuthoritiesExcludingRoleAuthorities(this.moduleId);
            },
            error: () => {
                this.notificationService.showError('Erreur', 'Impossible de retirer toutes les autorités');
            }
        });
    }
}
