import { Status } from "../../booking/models/status.model";

export interface LoginRequest {

  email:string;
  password:string;

}




export interface LoginResponse {

  access_token :string ;
  id:number;
  role:string;
  status:Status;
  message:string

}
