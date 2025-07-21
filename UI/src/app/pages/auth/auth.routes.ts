import { Routes } from '@angular/router';
import { Access } from './access';
import { Error } from './error';
import { LoginComponent } from '../../authentification/login/login.component';
import { AuthComponent } from '../../authentification/auth/auth.component';

export default [
    { path: 'access', component: Access },
    { path: 'error', component: Error },
    { path: 'login', component: LoginComponent }
] as Routes;
