import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';

@Injectable({
    providedIn: 'root'
})
export class KeycloakService {
    // private _keycloak: Keycloak;
    //
    // constructor() {
    //     this._keycloak = new Keycloak({
    //         url: 'http://localhost:8080',
    //         realm: 'NFR',
    //         clientId: 'NFR_UI'
    //     });
    // }
    //
    // get keycloak() {
    //     return this._keycloak;
    // }
    //
    // async init() {
    //     try {
    //         const authenticated = await this._keycloak.init({
    //             onLoad: 'login-required',
    //         });
    //         if (authenticated) {
    //             console.log('User is authenticated');
    //         } else {
    //             console.log('User is not authenticated');
    //         }
    //     } catch (error) {
    //         console.error('Keycloak initialization failed', error);
    //     }
    // }
}
