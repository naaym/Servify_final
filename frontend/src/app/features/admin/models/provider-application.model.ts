export type ProviderStatus = 'PENDING' | 'ACCEPTED' | 'REJECTED';

export interface ProviderApplication {
  id: number;
  name: string;
  email: string;
  phone: string;
  governorate: string;
  delegation: string;
  age: number;
  status: ProviderStatus;
  cinUrl: string;
  cvUrl: string;
  diplomeUrl: string;
  cvName:string;
  cinName:string;
  diplomeName:string;
  createdAt: string;
}
