// import { HTTP_INTERCEPTORS, provideHttpClient, withFetch } from '@angular/common/http';
// import { APP_INITIALIZER, ApplicationConfig } from '@angular/core';
// import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
// import { provideRouter, withEnabledBlockingInitialNavigation, withInMemoryScrolling } from '@angular/router';
// import Aura from '@primeng/themes/aura';
// import { providePrimeNG } from 'primeng/config';
// import { appRoutes } from './app.routes';
// import { AuthInterceptorService } from './app/services/interceptors/auth-interceptor.service';
// import { ConfirmationService, MessageService } from 'primeng/api';
// import { KeycloakService } from './app/services/KeycloakService';
//
// export function kcFactory(kcService: KeycloakService) {
//     return () => kcService.init();
// }
//
// export const appConfig: ApplicationConfig = {
//     providers: [
//         {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true},
//         provideRouter(appRoutes, withInMemoryScrolling({ anchorScrolling: 'enabled', scrollPositionRestoration: 'enabled' }), withEnabledBlockingInitialNavigation()),
//         provideHttpClient(withFetch()),
//         provideAnimationsAsync(),
//         providePrimeNG({ theme: { preset: Aura, options: { darkModeSelector: '.app-dark' } } }),
//         MessageService,ConfirmationService,
//         {provide: APP_INITIALIZER,
//             deps: [KeycloakService],
//             useFactory: kcFactory,
//             multi: true}
//     ]
// };
import { ApplicationConfig } from '@angular/core';
import { HTTP_INTERCEPTORS, provideHttpClient, withFetch } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideRouter, withEnabledBlockingInitialNavigation, withInMemoryScrolling } from '@angular/router';
import Aura from '@primeng/themes/aura';
import { providePrimeNG } from 'primeng/config';
import { appRoutes } from './app.routes';
import { AuthInterceptorService } from './app/services/interceptors/auth-interceptor.service';
import { ConfirmationService, MessageService } from 'primeng/api';

export let appConfig: ApplicationConfig;
appConfig = {
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptorService,
            multi: true
        },
        provideRouter(appRoutes, withInMemoryScrolling({ anchorScrolling: 'enabled', scrollPositionRestoration: 'enabled' }), withEnabledBlockingInitialNavigation()),
        provideHttpClient(withFetch()),
        provideAnimationsAsync(),
        providePrimeNG({ theme: { preset: Aura, options: { darkModeSelector: '.app-dark' } } }),
        MessageService,
        ConfirmationService
    ]
};
