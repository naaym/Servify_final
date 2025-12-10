import { Routes } from '@angular/router';
import { ProviderDetails } from './provider-details/provider-details';
import { SearchResultsComponent } from './search-results.component/search-results.component';

export const SEARCH_ROUTES: Routes = [
  {path:'',component:SearchResultsComponent

  },
  {
    path: 'providers',
    children: [
      {
        path: ':id',
        component: ProviderDetails,
      },
    ],
  },
];
