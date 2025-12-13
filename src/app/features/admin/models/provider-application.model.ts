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
  cin: string;
  cv: string;
  diplome: string;
  createdAt: string;
}
