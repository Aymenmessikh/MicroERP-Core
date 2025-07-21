import { Routes } from '@angular/router';
import { AppLayout } from './app/layout/component/app.layout';
import { Notfound } from './app/pages/notfound/notfound';
import { UsersComponent } from './app/features/user/users/users.component';
import { RolesComponent } from './app/features/role/roles/roles.component';
import { CreateRoleComponent } from './app/features/role/create-role/create-role.component';
import { UpdateRoleComponent } from './app/features/role/update-role/update-role.component';
import { DetailRoleComponent } from './app/features/role/detail-role/detail-role.component';
import { AuthoritysComponent } from './app/features/authority/authoritys/authoritys.component';
import { CreateAuthorityComponent } from './app/features/authority/create-authority/create-authority.component';
import { UpdateAuthorityComponent } from './app/features/authority/update-authority/update-authority.component';
import { DetailAuthorityComponent } from './app/features/authority/detail-authority/detail-authority.component';
import { ModulesComponent } from './app/features/module/modules/modules.component';
import { CreateModuleComponent } from './app/features/module/create-module/create-module.component';
import { UpdateModuleComponent } from './app/features/module/update-module/update-module.component';
import { DetailModuleComponent } from './app/features/module/detail-module/detail-module.component';
import { CreateUserComponent } from './app/features/user/create-user/create-user.component';
import { UpdateUserComponent } from './app/features/user/update-user/update-user.component';
import { DetailUserComponent } from './app/features/user/detail-user/detail-user.component';
import { DetailProfileComponent } from './app/features/profile/detail-profile/detail-profile.component';
import { GroupesComponent } from './app/features/groupe/groupes/groupes.component';
import { AuthorityTypeComponent } from './app/features/authorityType/authority-type/authority-type.component';
import { AuthGuard } from './app/authentification/guards/auth.guard';
import { ErpComponent } from './app/home/erp/erp.component';
import { DashboardComponent } from './app/dashboard/dashboard.component';
import {LoginComponent} from "./app/authentification/login/login.component";
import {AuditLogComponent} from "./app/features/audit-log/audit-log.component";

export const appRoutes: Routes = [
    {
        path: '',
        component: AppLayout,
        children: [
            { path: '', component: DashboardComponent, data: { breadcrumb: 'Home' } ,canActivate: [AuthGuard]},
            { path: 'audit-log', component: AuditLogComponent, data: { breadcrumb: 'audit Log' } ,canActivate: [AuthGuard]},

            // Users
            { path: 'users', component: UsersComponent, data: { breadcrumb: 'Users' } ,canActivate: [AuthGuard] },
            { path: 'users/create', component: CreateUserComponent, data: { breadcrumb: 'Create User' },canActivate: [AuthGuard] },
            { path: 'users/update/:id', component: UpdateUserComponent, data: { breadcrumb: 'Update User' },canActivate: [AuthGuard] },
            { path: 'users/detail/:id', component: DetailUserComponent, data: { breadcrumb: 'User Detail' },canActivate: [AuthGuard]},
            { path: 'profils/detail/:id', component: DetailProfileComponent, data: { breadcrumb: 'Profile Detail' } ,canActivate: [AuthGuard]},


            // Roles
            { path: 'roles', component: RolesComponent, data: { breadcrumb: 'Roles' },canActivate: [AuthGuard] },
            { path: 'roles/create', component: CreateRoleComponent, data: { breadcrumb: 'Create Role' },canActivate: [AuthGuard] },
            { path: 'roles/update/:id', component: UpdateRoleComponent, data: { breadcrumb: 'Update Role' },canActivate: [AuthGuard] },
            { path: 'roles/detail/:id', component: DetailRoleComponent, data: { breadcrumb: 'Role Detail' } ,canActivate: [AuthGuard]},

            // Authoritys
            { path: 'authoritys', component: AuthoritysComponent, data: { breadcrumb: 'Authoritys' } ,canActivate: [AuthGuard]},
            { path: 'authoritys/create', component: CreateAuthorityComponent, data: { breadcrumb: 'Create Authority' } ,canActivate: [AuthGuard]},
            { path: 'authoritys/update/:id', component: UpdateAuthorityComponent, data: { breadcrumb: 'Update Authority' },canActivate: [AuthGuard] },
            { path: 'authoritys/detail/:id', component: DetailAuthorityComponent, data: { breadcrumb: 'Authority Detail' } ,canActivate: [AuthGuard]},

            // Modules
            { path: 'modules', component: ModulesComponent, data: { breadcrumb: 'Modules' },canActivate: [AuthGuard] },
            { path: 'modules/create', component: CreateModuleComponent, data: { breadcrumb: 'Create Module' } ,canActivate: [AuthGuard]},
            { path: 'modules/update/:id', component: UpdateModuleComponent, data: { breadcrumb: 'Update Module' } ,canActivate: [AuthGuard]},
            { path: 'modules/detail/:id', component: DetailModuleComponent, data: { breadcrumb: 'Module Detail' },canActivate: [AuthGuard] },
            // Groupes
            { path: 'groups', component: GroupesComponent, data: { breadcrumb: 'groupes' },canActivate: [AuthGuard] },

            // Profile
            { path: 'profile/detail/:id', component: DetailProfileComponent, data: { breadcrumb: 'Profile Detail' } ,canActivate: [AuthGuard]},
            //Authority Type
            { path: 'authority/type', component: AuthorityTypeComponent, data: { breadcrumb: 'Authority Type' } ,canActivate: [AuthGuard]},
        ]
    },
    { path: 'notfound', component: Notfound ,canActivate: [AuthGuard]},
    { path: 'erp', component: ErpComponent ,canActivate: [AuthGuard]},
    { path: 'auth', loadChildren: () => import('./app/pages/auth/auth.routes') },
    { path: '**', redirectTo: '/notfound' }
];
