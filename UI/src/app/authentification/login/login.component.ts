import { Component } from '@angular/core';
import { AppFloatingConfigurator } from '../../layout/component/app.floatingconfigurator';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { LoginService } from '../../services/login.service';
import { Button } from 'primeng/button';
import { Router } from '@angular/router';
import { Toast } from 'primeng/toast';
import { NotificationService } from '../../services/notification.service';

@Component({
    selector: 'app-login',
    imports: [AppFloatingConfigurator, FormsModule, Button, Toast],
    templateUrl: './login.component.html',
    styleUrl: './login.component.scss'
})
export class LoginComponent {
    constructor(
        private metamask: LoginService,
        private http: HttpClient,
        private notificationService: NotificationService,
        private router: Router
    ) {}

    async loginWithMetaMask() {
        try {
            const address = await this.metamask.connectWallet();
            if (!address) {
                this.notificationService.showError('Error', 'Failed to connect wallet');
                return;
            }

            // Create a message to sign
            const message = `Sign this message to prove ownership of address ${address} for login`;

            // Request signature from MetaMask
            const signature = await this.metamask.requestSignature(message);
            if (!signature) {
                this.notificationService.showError('Error', 'Failed to get signature');
                return;
            }

            // Send login request with address and signature
            this.http
                .post('http://localhost:3000/users/login', {
                    address,
                    signature,
                    message
                })
                .subscribe(
                    (res: any) => {
                        localStorage.setItem('token', res.token);
                        this.router.navigate(['/erp']);
                        this.notificationService.showSuccess('Success', 'Login Successful');
                    },
                    (error) => {
                        this.notificationService.showError('Error', `Login Failed: ${error.error?.message || error}`);
                        console.error('Login error:', error);
                    }
                );
        } catch (error) {
            this.notificationService.showError('Error', `Login Failed: ${error}`);
            console.error('Login error:', error);
        }
    }
}
