export interface Provider {
  id: number;
  name: string;
  serviceCategory: string;
  governorate: string;
  delegation: string;
  basePrice?: number;
  rating?: number;
  reviewCount?: number;
  politenessRating?: number;
  qualityRating?: number;
  punctualityRating?: number;
  description?: string;
  imageProviderUrl?: string;
  workImages?: string[];
}
