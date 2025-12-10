import { Routes } from '@angular/router';
import { SignUp } from './features/auth/signup/signup';
import { Landing } from './features/landing/landing';
import { Client } from './features/client/pages/signup/client/client';
import { Login } from './features/auth/login/login';
import { Test } from './features/test/test';


export const routes: Routes = [
  {path:'',component:Landing},

  // {path:'admin',
  // canMatch:[roleMatchGuard(['ADMIN','SUPER_ADMIN'])],
  // canActivate:[AuthActivateGuard],
  // loadComponent: () => import('./features/landing/landing').then(m => m.Landing)},
  {path:'login',component:Login},
  {path:'test',component:Test},

  {
    path: 'clients',
    loadChildren: () => import('./features/client/client.routes').then(m=>m.CLIENTS_ROUTES),
  },
  {path:"providers",
    loadChildren:()=>import("./features/Provider/provider.routes").then(m=>m.PROVIDER_ROUTES)
  },
  {path:'signup',component:SignUp},


   {path:'search',
    loadChildren:()=>import('./features/Search/search.routes').then(m=>m.SEARCH_ROUTES)
  },{
    path:'admins',loadChildren:()=>import('./features/admin/admin.routes').then(m=>m.ADMIN_ROUTS)
  }
  // {path:'**',component:Error404}
];
