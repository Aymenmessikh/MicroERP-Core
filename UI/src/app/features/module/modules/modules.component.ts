import { Component, OnInit } from '@angular/core';
import { Button } from 'primeng/button';
import { Router, RouterLink } from '@angular/router';
import { TableModule } from 'primeng/table';
import { Toolbar } from 'primeng/toolbar';
import { NotificationService } from '../../../services/notification.service';
import { ModuleResponse } from '../../../models/module/ModuleResponse';
import { ModulesService } from '../../../services/module/modules.service';
import { Dialog } from 'primeng/dialog';

@Component({
    selector: 'app-modules',
    imports: [Button, RouterLink, TableModule, Toolbar, Dialog],
    templateUrl: './modules.component.html',
    styleUrl: './modules.component.scss'
})
export class ModulesComponent implements OnInit {
    rows = 10;

    isLoading = true;
    errorMessage = '';
    statuses!: any[];
    modules: ModuleResponse[] = [];
    displayConfirmation: boolean = false;
    displayActifConfirmation: boolean = false;
    idConfirmation: number = 0;
    yesConfirmation!: string;
    noConfirmation!: string;
    messageConfirmation: string = '';
    actifValue!: boolean;
    displayDeleteSelectedConfirmation: boolean = false;

    constructor(
        private moduleService: ModulesService,
        private notificationService: NotificationService,
        private router: Router
    ) {}

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

    deleteModule() {
        this.moduleService.deleteModule(this.idConfirmation).subscribe({
            next: () => {
                this.modules = this.modules.filter((module) => module.id !== this.idConfirmation);
                this.notificationService.showSuccess('Success', 'Module deleted successfully');
                this.cancelDelete();
            },
            error: (err) => {
                this.notificationService.showError('Error', `Failed to delete Module: ${this.errorMessage}`);
            }
        });
    }

    updateModule(moduleId: number) {
        this.router.navigate(['/modules/update', moduleId]);
    }

    showModuleDetails(moduleId: number) {
        this.router.navigate(['/modules/detail', moduleId]);
    }

    toggleModuleStatus() {
        const action = this.actifValue ? this.moduleService.disableModule(this.idConfirmation) : this.moduleService.enableModule(this.idConfirmation);

        const successMessage = this.actifValue ? 'Module disabled successfully' : 'Module enabled successfully';

        const errorMessage = this.actifValue ? 'Failed to disable Module' : 'Failed to enable Module';

        action.subscribe({
            next: () => {
                this.notificationService.showSuccess('Success', successMessage);
                this.cancelActifDisactif();
                const index = this.modules.findIndex((mod) => mod.id === this.idConfirmation);
                if (index !== -1) {
                    this.modules[index] = {
                        ...this.modules[index],
                        actif: !this.actifValue
                    };
                }
            },
            error: (err) => {
                console.error('Error updating Module:', err);
                this.notificationService.showError('Error', `${errorMessage}: ${err.message || err}`);
            }
        });
    }

    confirmActifAndDisactif(id: number, actif: boolean) {
        this.displayActifConfirmation = true;
        this.messageConfirmation = actif ? 'Are you sure you want to disable this Module?' : 'Are you sure you want to enable this Module?';
        this.noConfirmation = 'Cancel';
        this.idConfirmation = id;
        this.yesConfirmation = actif ? 'Disactf' : 'Actif';
        this.actifValue = actif;
    }

    confirmDelete(id: number) {
        this.displayConfirmation = true;
        this.idConfirmation = id;
    }

    cancelActifDisactif() {
        this.displayActifConfirmation = false;
    }

    cancelDelete() {
        this.displayConfirmation = false;
    }

    cancelDeleteSelected(): void {
        this.displayDeleteSelectedConfirmation = false;
    }

    deleteSelectedAuthorities() {}

    deleteSelectedUsersConfirmed() {}
}
