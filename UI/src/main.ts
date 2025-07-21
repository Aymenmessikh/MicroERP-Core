import { bootstrapApplication } from '@angular/platform-browser';
import { importProvidersFrom } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { appConfig } from './app.config';

bootstrapApplication(AppComponent, {
    providers: [
        importProvidersFrom(HttpClientModule), // Ensure HttpClientModule is registered
        ...appConfig.providers
    ]
}).catch(err => console.error(err));
