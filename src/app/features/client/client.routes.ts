import { DashboardComponent } from './pages/dashboard/dashboard.component/dashboard.component';
import { DetailsComponent } from './pages/bookings/details/details.component/details.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { Routes } from '@angular/router';
import { Client } from './pages/signup/client/client';

export const CLIENTS_ROUTES: Routes = [
  {
    path: 'dashboard',
    component: DashboardComponent,
  },
  { path: 'profil', component: ProfileComponent },
  {
    path: 'bookings',
    children: [
      { path: ':bookingId', component: DetailsComponent }
    ],
  },
  {path:'signup',component:Client},
];
