import { Component, OnInit } from '@angular/core';
import { DashboardService } from '../services/dashboard/dashboard.service';
import { ModulesService } from '../services/module/modules.service';
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    imports: [DropdownModule, FormsModule],
    styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
    totalAuthoritiesType: number = 0;
    totalAuthorities: number = 0;

    totalRoles: number = 0;
    totalModules: number = 0;
    totalGroups: number = 0;
    totalUsers: number = 0;
    totalActifUsers: number = 0;
    totalProfiles: number = 0;

    modules: any[] = [];
    selectedModuleId:number=0;

    constructor(
        private dashboardService: DashboardService,
        private moduleService: ModulesService
    ) {}

    ngOnInit(): void {
        this.fetchModules();
    }
    fetchModules() {
        this.moduleService.getModules().subscribe((data) => {
            this.modules = [{ moduleName: 'All Modules', id: 0 }, ...data];
            this.selectedModuleId = 0; // Ensure it's selected by default
            this.fetchAllCounts();     // Load counts immediately with default
        });
    }

    fetchAllCounts() {
        if (this.selectedModuleId === 0) {
            this.loadModuleCount();
            this.loadRoleCount();
            this.loadGroupeCount();
            this.loadTotalAuthorityCount();
            this.loadUserCount();
            this.loadProfileCount();
            this.loadTotalAuthorityTypeCount();
            this.loadUserAcrifCount();
        } else {
            this.loadAuthorityCountByModule(this.selectedModuleId);
            this.loadRoleCountByModule(this.selectedModuleId);
        }
    }

    onModuleChange(event: any) {
       this.fetchAllCounts()
    }

    loadTotalAuthorityCount() {
        this.dashboardService.getAuthorityCount().subscribe(
            (count) => (this.totalAuthorities = count),
            (error) => console.error('Error loading total authority count:', error)
        );
    }
    loadTotalAuthorityTypeCount() {
        this.dashboardService.getAuthorityCount().subscribe(
            (count) => (this.totalAuthoritiesType = count),
            (error) => console.error('Error loading total authority Type count:', error)
        );
    }

    loadAuthorityCountByModule(moduleId: number) {
        this.dashboardService.getAuthorityCountByModule(moduleId).subscribe(
            (count) => (this.totalAuthorities = count),
            (error) => console.error('Error loading authority count by module:', error)
        );
    }

    loadRoleCount() {
        this.dashboardService.getRoleCount().subscribe(
            (count) => (this.totalRoles = count),
            (error) => console.error('Error loading role count:', error)
        );
    }

    loadRoleCountByModule(moduleId: number) {
        this.dashboardService.getRoleCountByModule(moduleId).subscribe(
            (count) => (this.totalRoles = count),
            (error) => console.error('Error loading role count by module:', error)
        );
    }

    loadModuleCount() {
        this.dashboardService.getModuleCount().subscribe(
            (count) => (this.totalModules = count),
            (error) => console.error('Error loading module count:', error)
        );
    }

    loadGroupeCount() {
        this.dashboardService.getGroupeCount().subscribe(
            (count) => (this.totalGroups = count),
            (error) => console.error('Error loading groupe count:', error)
        );
    }

    loadUserCount() {
        this.dashboardService.getUserCount().subscribe(
            (count) => (this.totalUsers = count),
            (error) => console.error('Error loading user count:', error)
        );
    }
    loadUserAcrifCount() {
        this.dashboardService.getUserActifCount().subscribe(
            (count) => (this.totalActifUsers = count),
            (error) => console.error('Error loading user count:', error)
        );
    }

    loadProfileCount() {
        this.dashboardService.getProfileCount().subscribe(
            (count) => (this.totalProfiles = count),
            (error) => console.error('Error loading profile count:', error)
        );
    }
}
