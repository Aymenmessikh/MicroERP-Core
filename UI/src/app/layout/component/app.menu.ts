import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { AppMenuitem } from './app.menuitem';

@Component({
    selector: 'app-menu',
    standalone: true,
    imports: [CommonModule, AppMenuitem, RouterModule],
    template: `<ul class="layout-menu">
        <ng-container *ngFor="let item of model; let i = index">
            <li app-menuitem *ngIf="!item.separator" [item]="item" [index]="i" [root]="true"></li>
            <li *ngIf="item.separator" class="menu-separator"></li>
        </ng-container>
    </ul> `
})
export class AppMenu {
    model: MenuItem[] = [];

    ngOnInit() {
        this.model = [
            {
                label: 'Home',
                items: [{ label: 'Dashboard', icon: 'pi pi-fw pi-home', routerLink: ['/'] }]
            },
            {
                label: 'Admin Modules',
                items: [
                    { label: 'Users', icon: 'pi pi-users', routerLink: ['/users'] },
                    { label: 'Groups', icon: 'pi pi-sitemap', class: 'rotated-icon', routerLink: ['/groups'] },
                    { label: 'Roles', icon: 'pi pi-key', routerLink: ['/roles'] },
                    { label: 'Authorities', icon: 'pi pi-lock', routerLink: ['/authoritys'] },
                    { label: 'Modules', icon: 'pi pi-th-large', routerLink: ['/modules'] },
                    { label: 'Authorities Type', icon: 'pi pi-pause-circle', routerLink: ['/authority/type'] },
                    { label: 'Audit Logs', icon: 'pi pi-book', routerLink: ['/audit-log'] },
                ]
            },
         ];
     }
}
