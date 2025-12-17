import { Status } from "./status.model";

export interface BookingResponse {
  bookingId:number;
  status:Status;
  category:string;
  providerName?:string;
  clientName?:string;
  date:number;


}
