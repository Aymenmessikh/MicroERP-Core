// import { Injectable } from '@angular/core';
// import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
// import { Observable, from } from 'rxjs';
// import { mergeMap } from 'rxjs/operators';
// import { KeycloakService } from '../KeycloakService';
//
// @Injectable({
//     providedIn: 'root'
// })
// export class AuthInterceptorService implements HttpInterceptor {
//
//     constructor(private keycloakService: KeycloakService) {}
//
//     intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
//         return from(this.keycloakService.keycloak.updateToken(30)).pipe(
//             mergeMap(() => {
//                 const token = this.keycloakService.keycloak.token;
//                 if (token) {
//                     const clonedRequest = req.clone({
//                         setHeaders: {
//                             Authorization: `Bearer ${token}`
//                         }
//                     });
//                     return next.handle(clonedRequest);
//                 }
//                 return next.handle(req);
//             })
//         );
//     }
// }
import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor {

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // Get the token from localStorage (or sessionStorage if that's where you store it)
        const token = localStorage.getItem('token');

        if (token) {
            const clonedRequest = req.clone({
                setHeaders: {
                    Authorization: `Bearer ${token}`
                }
            });

            return next.handle(clonedRequest);
        }

        // If no token, proceed without modifying the request
        return next.handle(req);
    }
}
