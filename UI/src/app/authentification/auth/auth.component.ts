import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-auth',
    imports: [FormsModule],
    templateUrl: './auth.component.html',
    styleUrl: './auth.component.scss'
})
export class AuthComponent {
    // userName: string = '';
    //
    // password: string = '';
    //
    // private _profile: UserProfile | undefined;
    //
    // private keycloakUrl = 'http://localhost:8080/realms/NFR/protocol/openid-connect/token';
    // constructor(
    //     private http: HttpClient,
    //     private keycloakService: KeycloakService,
    //     private router: Router
    // ) {}
    //
    // login() {
    //     const headers = new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' });
    //
    //     const body = new HttpParams()
    //         .set('client_id', 'NFR')
    //         .set('grant_type', 'password')
    //         .set('client_secret', 'JIGhqoAMbVLNMEuIWL7Dp1jP9Urcgfou')
    //         .set('username', this.userName)
    //         .set('password', this.password);
    //     console.log(this.userName,this.password)
    //
    //     this.http.post(this.keycloakUrl, body.toString(), { headers }).subscribe(
    //         async (response: any) => {
    //             try {
    //                 const token = response.access_token;
    //                 // const refreshToken = response.refresh_token;
    //                 localStorage.setItem('token', token);
    //                 // localStorage.setItem('refresh_token', refreshToken);
    //
    //                 this.keycloakService.keycloak.token = token;
    //                 // this.keycloakService.keycloak.refreshToken = refreshToken;
    //                 // await this.keycloakService.keycloak.updateToken(10);
    //
    //                 this._profile = (await this.keycloakService.keycloak.loadUserProfile()) as UserProfile;
    //                 this._profile.token = token || '';
    //                 console.log(this._profile);
    //
    //                 this.router.navigate(['/erp']);
    //             } catch (error) {
    //                 console.error('Error during login process', error);
    //             }
    //         },
    //         (error) => {
    //             console.error('Login failed', error);
    //         }
    //     );
    // }
    // loogin() {
    //     return this.keycloakService.keycloak.login();
    // }
    //
    // logout() {
    //     // this.keycloak.accountManagement();
    //     return this.keycloakService.keycloak.logout({ redirectUri: 'http://localhost:4200' });
    // }
    // loginWithGoogle() {
    //     const googleLoginUrl = 'http://localhost:8080/realms/Keycloak-Angular/protocol/openid-connect/auth?client_id=Angular&response_type=code&scope=openid&redirect_uri=http://localhost:4200/auth-callback&kc_idp_hint=google';
    //     window.location.href = googleLoginUrl;
    // }
}
