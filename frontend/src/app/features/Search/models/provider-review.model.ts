export interface ProviderReview {
  id: number;
  clientName: string;
  clientProfileImageUrl?: string;
  overallRating: number;
  politenessRating: number;
  qualityRating: number;
  punctualityRating: number;
  comment?: string;
  createdAt: number;
}
