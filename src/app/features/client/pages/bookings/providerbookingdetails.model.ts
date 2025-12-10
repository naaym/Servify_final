import { metadata } from "./clientbookingdetailattachments.model";

export interface providerBookingDetails{
  providerId:number;
  providerLocalisation:string;
  providerName:string;
  providerPhone:string;
  providerImage?:metadata;
  providerRate:number;

}
