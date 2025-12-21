export interface ProviderProfile {
  id: number;
  name: string;
  email: string;
  phone: string;
  governorate: string;
  delegation: string;
  age: number | null;
  serviceCategory: string;
  basePrice: number | null;
  description?: string;
  profileImageUrl?: string;
  workImages?: string[];
}

export interface UpdateProviderProfileRequest {
  name?: string;
  phone?: string;
  governorate?: string;
  delegation?: string;
  age?: number | null;
  serviceCategory?: string;
  basePrice?: number | null;
  description?: string;
}
