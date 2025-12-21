export interface Provider {
  id: number;
  name: string;
  serviceCategory: string;
  governorate: string;
  delegation: string;
  basePrice?: number;
  rating?: number;
  reviewCount?: number;
  description?: string;
  imageProviderUrl?: string;
  workImages?: string[];
}
