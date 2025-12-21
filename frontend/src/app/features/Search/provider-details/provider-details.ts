import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { SearchProviderService } from '../services/provider-search.service';
import { Provider } from '../models/provider.model';
import { Header } from '../../../shared/header/header';
import { ProviderReview } from '../models/provider-review.model';

@Component({
  selector: 'app-provider-details',
  imports: [CommonModule, RouterLink, Header],
  templateUrl: './provider-details.html',
})
export class ProviderDetails implements OnInit, OnDestroy {
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly searchService = inject(SearchProviderService);
  private readonly router = inject(Router);

  provider?: Provider;
  reviews: ProviderReview[] = [];
  isLoading = true;
  isReviewsLoading = true;
  errorMessage = '';
  reviewsErrorMessage = '';
  readonly defaultAvatarUrl = '/assets/default-avatar.png';
  selectedWorkImageIndex = 0;
  readonly ratingStars = [1, 2, 3, 4, 5];
  private rotationIntervalId?: number;

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((params) => {
      const providerId = Number(params.get('id'));
      if (!providerId) {
        this.isLoading = false;
        this.errorMessage = 'Provider introuvable.';
        return;
      }

      this.isLoading = true;
      this.errorMessage = '';

      this.searchService.getProviderById(providerId).subscribe({
        next: (provider) => {
          this.provider = provider;
          this.isLoading = false;
          this.selectedWorkImageIndex = 0;
          this.startWorkImageRotation();
        },
        error: () => {
          this.errorMessage = 'Impossible de charger le profil du provider.';
          this.isLoading = false;
        },
      });

      this.isReviewsLoading = true;
      this.reviewsErrorMessage = '';
      this.searchService.getProviderReviews(providerId).subscribe({
        next: (reviews) => {
          this.reviews = reviews;
          this.isReviewsLoading = false;
        },
        error: () => {
          this.reviewsErrorMessage = 'Impossible de charger les avis.';
          this.isReviewsLoading = false;
        },
      });
    });
  }

  ngOnDestroy(): void {
    this.stopWorkImageRotation();
  }

  bookProvider() {
    if (!this.provider) {
      return;
    }

    this.router.navigate(['/providers', this.provider.id, 'booking']);
  }

  get activeWorkImage(): string | null {
    if (!this.provider?.workImages?.length) {
      return null;
    }
    return this.provider.workImages[this.selectedWorkImageIndex] ?? this.provider.workImages[0];
  }

  selectWorkImage(index: number) {
    if (!this.provider?.workImages?.length) {
      return;
    }
    this.selectedWorkImageIndex = index;
    this.restartWorkImageRotation();
  }

  private startWorkImageRotation() {
    this.stopWorkImageRotation();
    if (!this.provider?.workImages?.length || this.provider.workImages.length < 2) {
      return;
    }
    this.rotationIntervalId = window.setInterval(() => {
      if (!this.provider?.workImages?.length) {
        return;
      }
      this.selectedWorkImageIndex = (this.selectedWorkImageIndex + 1) % this.provider.workImages.length;
    }, 3000);
  }

  private restartWorkImageRotation() {
    this.startWorkImageRotation();
  }

  private stopWorkImageRotation() {
    if (this.rotationIntervalId) {
      window.clearInterval(this.rotationIntervalId);
      this.rotationIntervalId = undefined;
    }
  }
}
