// import { Injectable } from '@angular/core';
// import { CanActivate, Router } from '@angular/router';
// import { KeycloakService } from '../../services/KeycloakService';
//
// @Injectable({
//     providedIn: 'root',
// })
// export class AuthGuard implements CanActivate {
//     constructor(private keycloakService: KeycloakService, private router: Router) {}
//
//     async canActivate(): Promise<boolean> {
//         try {
//             const authenticated = await this.keycloakService.keycloak.authenticated;
//             if (authenticated) {
//                 return true;
//             } else {
//                 this.router.navigate(['/auth/login']);
//                 return false;
//             }
//         } catch (error) {
//             console.error('AuthGuard error:', error);
//             this.router.navigate(['/auth/login']);
//             return false;
//         }
//     }
// }
import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {
    constructor(private router: Router) {}

    canActivate(): boolean {
        const token = localStorage.getItem('token');

        if (token) {
            // Optionnel : tu peux ajouter une vérification de validité du token ici (expiration, structure JWT, etc.)
            return true;
        } else {
            this.router.navigate(['/auth/login']);
            return false;
        }
    }
}
