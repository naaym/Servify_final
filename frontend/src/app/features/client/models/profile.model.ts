export interface ClientProfile {
  id: number;
  name: string;
  email: string;
  phone: string;
  governorate: string;
  profileImageUrl?: string | null;
  createdAt?: string;
}
