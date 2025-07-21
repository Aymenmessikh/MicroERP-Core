import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { Breadcrumb } from 'primeng/breadcrumb';

@Component({
    selector: 'app-breadcrump',
    imports: [Breadcrumb],
    templateUrl: './breadcrump.component.html',
    styleUrl: './breadcrump.component.scss'
})
export class BreadcrumpComponent implements OnInit {
    items: any[] = [];
    home = { icon: 'pi pi-home', routerLink: '/' };

    constructor(
        private router: Router,
        private route: ActivatedRoute
    ) {}

    ngOnInit() {
        this.router.events.pipe(filter((event) => event instanceof NavigationEnd)).subscribe(() => {
            this.items = this.createBreadcrumbs(this.route.root);
        });
    }

    private createBreadcrumbs(route: ActivatedRoute, url: string = '', breadcrumbs: any[] = []): any[] {
        const children: ActivatedRoute[] = route.children;

        // Vérifie si la route actuelle a une configuration et une donnée "breadcrumb"
        if (route.routeConfig && route.routeConfig.data && route.routeConfig.data['breadcrumb']) {
            const routeURL = route.routeConfig.path ? `${url}/${route.routeConfig.path}` : url;

            breadcrumbs.push({
                label: route.routeConfig.data['breadcrumb'],
                routerLink: routeURL
            });

            url = routeURL; // Met à jour l'URL pour la prochaine itération
        }

        // Continue à parcourir les enfants récursivement
        for (const child of children) {
            breadcrumbs = this.createBreadcrumbs(child, url, breadcrumbs);
        }

        return breadcrumbs;
    }

}
