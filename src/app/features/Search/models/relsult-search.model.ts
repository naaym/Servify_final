import { Provider } from './provider.model';

export interface SearchProviderResult {
  providers: Provider[];
  total: number;
}

export interface SearchProviderRequest {
  serviceCategory?: string;
  governorate?: string;
  minRating?: number;
  minPrice?: number;
  maxPrice?: number;
  sortBy?: 'RATING_DESC' | 'PRICE_ASC' | 'PRICE_DESC' | 'REVIEWS_DESC';
}
