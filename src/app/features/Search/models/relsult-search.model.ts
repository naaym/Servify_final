import { Provider } from "./provider.model";


export interface SearchProviderResult  {
  provider:Provider[]
  page:number; // num de page actuel
  size:number;
  totalElements:number; //guedesh on a de providers fi search
  totalPages:number;  //guedesh 3ana men page fel template search
  sort:string;
}

export interface SearchProviderRequest{
  service: string;
  governorate:string;  // expl ena na3tih ariana howa ya3tini li fi 7ay ettadhamen , mnihla ...
  rate?:number;
  minRate?:number;
  minPrice?:number;
  maxPrice?:number;
  skills?:string[]; // khater expl devloppeur ynajem ykoun web , mobile ,...
  sort?:string;
  page?:number;
  size? : number; // gudech men provider par page

}
