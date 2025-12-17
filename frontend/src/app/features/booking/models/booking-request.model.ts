export interface BookingRequest {

  date:string;
  time:string;
  description:string;
  attachment?:File[];
  providerId:number;

}



