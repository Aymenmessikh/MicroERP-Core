import { Component, OnInit } from '@angular/core';
import { LayoutService } from '../../layout/service/layout.service';
import { NgClass } from '@angular/common';
import { Router } from '@angular/router';
import { ModulesService } from '../../services/module/modules.service';
import { ProfilesService } from '../../services/profile/profiles.service';
import { ModuleResponse } from '../../models/module/ModuleResponse';
import { ProfileResponse } from '../../models/profile/ProfileResponse';
import { Card } from 'primeng/card';
import { Button } from 'primeng/button';
import { Popover } from 'primeng/popover';
import { jwtDecode } from 'jwt-decode';
import { UsersService } from '../../services/user/users.service';

@Component({
    selector: 'app-erp',
    imports: [NgClass, Card, Button, Popover],
    templateUrl: './erp.component.html',
    styleUrl: './erp.component.scss'
})
export class ErpComponent implements OnInit {
    modules: ModuleResponse[] = [];
    profiles: ProfileResponse[] = [];
    actifProfile !:ProfileResponse;
    username: string | null = null;

    constructor(
        public layoutService: LayoutService,
        private moduleService: ModulesService,
        private router: Router,
        private profileService: ProfilesService,
        private userService: UsersService
    ) {}

    async ngOnInit(): Promise<void> {
        this.username = this.getUsernameFromToken();
        console.log('Username:', this.username);

        if (this.username) {
            this.getAllModules();
            this.loadProfiles()
        } else {
            console.error('No username found - user might not be authenticated');
            // Rediriger vers la page de login si nécessaire
            // this.router.navigate(['/login']);
        }
    }

    private getUsernameFromToken(): string | null {
        const token = localStorage.getItem('token');
        if (!token) {
            console.error('No token found in localStorage');
            return null;
        }

        try {
            const decodedToken: any = jwtDecode(token);
            // Essayez différents champs possibles selon votre backend
            return decodedToken?.preferred_username || decodedToken?.username || decodedToken?.sub || null;
        } catch (error) {
            console.error('Error decoding token:', error);
            return null;
        }
    }
    lougout() {
        localStorage.clear();
        this.router.navigate(['/auth/login']);
    }
    toggleDarkMode() {
        this.layoutService.layoutConfig.update((state) => ({ ...state, darkTheme: !state.darkTheme }));
    }

    getAllModules() {
        if (!this.username) {
            console.error('Cannot get profiles - username is null');
            return;
        }
        this.moduleService.getModulesByProfile(this.username).subscribe({
            next: (data) => {
                this.modules = data;
            },
            error: (error) => {
                console.log(error);
            }
        });
    }

    loadProfiles(): void {
        if (!this.username) {
            console.error('Cannot get profiles - username is null');
            return;
        }
        this.userService.getActiveProfile(this.username).subscribe({
            next: (profile) => this.actifProfile = profile,
            error: (err) => console.error('Error loading active profile', err)
        });

        this.userService.getInactiveProfiles(this.username).subscribe({
            next: (profiles) => this.profiles = profiles,
            error: (err) => console.error('Error loading inactive profiles', err)
        });
    }

    navigateToModule(uri: string) {
        try {
            window.location.href = `http://${uri}`;
        } catch (error) {
            console.error('Navigation failed:', error);
        }
    }

    changeActiveProfile(profile: number) {
        if (!this.username) {
            console.error('Cannot get profiles - username is null');
            return;
        }
        this.userService.changeActifProfile(profile, this.username).subscribe({
            next: (data) => {
                console.log(data);
                this.loadProfiles()
                this.getAllModules();
            }
        });
    }
}
