export interface ClientRequest{
   id:string|number;
  category:string;
  description?:string;
  date:string;
  providerId?:string;
  status: 'pending' | 'accepted' | 'done' | 'rejected';
  attachment?:string[]

}
export interface ClientResponse{
  id:string|number;
  category:string;
  description:string;
  date:string;
  providerName:string;
  attachment?:string[]

}
