export interface ProviderSignUpRequest {
  name:string;
  email:string;
  phone:string;
  password:string;
  governorate:string;
  delegation:string;
  age:number;

  cin:File;
  cv:File;
  diplome:File;
}
export interface ProviderSignUpResponse {
  providerId: number;
  status: "PENDING" | "APPROVED" | "REJECTED";
  message: string;
}
