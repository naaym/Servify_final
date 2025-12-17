import { Status } from "../../../booking/models/status.model";
import { metadata } from "./clientbookingdetailattachments.model";
import { providerBookingDetails } from "./providerbookingdetails.model";
export interface ClientBookingDetails{
  status:Status
  description:string;
  bookingId:number;
  bookingStatus:Status;
  date:number;
  updatedAt:number;
  providerInfo:providerBookingDetails;
  clientName?:string;
  clientPhone?:string;
  serviceGategory:string;
  attachments?:metadata[];

}
