export interface AdminRequest {
  name: string;
  email: string;
  password: string;
  phone: string;
  governorate: string;
}

export interface AdminResponse {
  id: number;
  name: string;
  email: string;
  phone: string;
  governorate: string;
  createdAt: string;
}
