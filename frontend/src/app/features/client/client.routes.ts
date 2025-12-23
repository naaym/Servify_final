import { DashboardComponent } from './pages/dashboard/dashboard.component/dashboard.component';
import { DetailsComponent } from './pages/bookings/details/details.component/details.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { Routes } from '@angular/router';
import { Client } from './pages/signup/client/client';
import { BookingChatsComponent } from '../chat/chat.component';
import { ClientPaymentHistoryComponent } from '../payments/pages/client-payment-history/client-payment-history.component';
import { AdminChatsComponent } from '../admin-chat/admin-chat.component';

export const CLIENTS_ROUTES: Routes = [
  {
    path: 'dashboard',
    component: DashboardComponent,
  },
  { path: 'profil', component: ProfileComponent },
  { path: 'chats', component: BookingChatsComponent },
  { path: 'admin-chats', component: AdminChatsComponent },
  {
    path: 'bookings',
    children: [
      { path: ':bookingId', component: DetailsComponent }
    ],
  },
  { path: 'payments', component: ClientPaymentHistoryComponent },
  {path:'signup',component:Client},
];
