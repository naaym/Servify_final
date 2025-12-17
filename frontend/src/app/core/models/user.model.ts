import { Role } from "../../features/auth/models/role.model";

export interface currentUser {
  id: string;
  email: string;
  phone: string;
  adress: string;
  roles : Role[] ;
}
