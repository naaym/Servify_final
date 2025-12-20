import { Routes } from "@angular/router";
import { NewBookingComponent } from "../booking/new-booking/new-booking.component/new-booking.component";
import { ProviderDetails } from "../Search/provider-details/provider-details";
import { Provider } from "./pages/signup/provider/provider";
import { AccountReview } from "./pages/account-review/account-review.component";
import { AccountRejction } from "./pages/account-rejction/account-rejction.component";
import { ProviderDashboard } from "./pages/provider-dashboard/dashboard-provider.component";
import { ProviderBookingDetailsComponent } from "./pages/booking-details/provider-booking-details.component";
import { ProviderProfileComponent } from "./pages/profile/provider-profile.component";
import { BookingChatsComponent } from "../chat/chat.component";



export const PROVIDER_ROUTES :Routes= [
  {path:'signup',component:Provider},
  {path :'account-review',component:AccountReview},
  {path :'account-rejection',component:AccountRejction},
  {path :'dashboard',component:ProviderDashboard},
  {path :'chats',component:BookingChatsComponent},
  {path :'profile',component:ProviderProfileComponent},
  {path :'bookings/:bookingId',component:ProviderBookingDetailsComponent},

  {
    path: ':id',
    children: [
      { path: '', component: ProviderDetails },
      { path: 'booking', component: NewBookingComponent }
    ],
  },
];
