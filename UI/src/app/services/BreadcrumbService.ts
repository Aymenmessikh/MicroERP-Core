import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, NavigationEnd, Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { filter } from 'rxjs/operators';
import { MenuItem } from 'primeng/api';

@Injectable({
    providedIn: 'root'
})
export class BreadcrumbService {
    private breadcrumbsSubject = new BehaviorSubject<MenuItem[]>([]); // Initialize with an empty array
    breadcrumbs$: Observable<MenuItem[]> = this.breadcrumbsSubject.asObservable();

    constructor(private router: Router) {
        this.router.events
            .pipe(filter(event => event instanceof NavigationEnd))
            .subscribe(() => {
                const root = this.router.routerState.snapshot.root;
                const breadcrumbs = this.createBreadcrumbs(root);
                this.breadcrumbsSubject.next(breadcrumbs);
            });
    }

    private createBreadcrumbs(route: ActivatedRouteSnapshot, url: string = '', breadcrumbs: MenuItem[] = []): MenuItem[] {
        const children: ActivatedRouteSnapshot[] = route.children;

        if (children.length === 0) {
            return breadcrumbs;
        }

        for (const child of children) {
            const routeURL: string = child.url.map(segment => segment.path).join('/');
            if (routeURL !== '') {
                url += `/${routeURL}`;
            }

            const label = child.data['breadcrumb'];
            if (label) {
                breadcrumbs.push({ label, url });
            }

            return this.createBreadcrumbs(child, url, breadcrumbs);
        }

        return breadcrumbs;
    }
}
