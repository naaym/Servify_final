import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProviderCardComponent } from '../components/provider-card.component/provider-card.component';
import { ActivatedRoute, Router } from '@angular/router';
import { SearchProviderService } from '../services/provider-search.service';
import { SearchProviderRequest } from '../models/relsult-search.model';
import { Provider } from '../models/provider.model';
import { Header } from '../../../shared/header/header';

@Component({
  selector: 'app-search-results.component',
  standalone: true,
  imports: [CommonModule, FormsModule, ProviderCardComponent, Header],
  templateUrl: './search-results.component.html',
  styleUrl: './search-results.component.scss',
})
export class SearchResultsComponent implements OnInit {
  activatedRoute = inject(ActivatedRoute);
  searchService = inject(SearchProviderService);
  router = inject(Router);

  serviceCategory = '';
  city = '';
  delegation = '';
  minPrice?: number;
  maxPrice?: number;
  minRating?: number;
  sort: SearchProviderRequest['sortBy'] = 'RATING_DESC';
  ratingOptions: number[] = [5, 4, 3];

  providers: Provider[] = [];
  total = 0;

  ngOnInit(): void {
    this.serviceCategory =
      this.activatedRoute.snapshot.queryParamMap.get('service') || '';
    this.city = this.activatedRoute.snapshot.queryParamMap.get('city') || '';
    this.delegation =
      this.activatedRoute.snapshot.queryParamMap.get('delegation') || '';
    this.fetchResults();
  }

  fetchResults() {
    const request: SearchProviderRequest = {
      serviceCategory: this.serviceCategory,
      governorate: this.city,
      delegation: this.delegation,
      minPrice: this.minPrice,
      maxPrice: this.maxPrice,
      minRating: this.minRating,
      sortBy: this.sort,
    };

    this.searchService.searchProvider(request).subscribe((resp) => {
      this.providers = resp.providers;
      this.total = resp.total;
    });
  }

  showProviderDetails(id: number) {
    this.router.navigate(['/search/providers', id]);
  }

  newBooking(id: number) {
    this.router.navigate(['/providers', id, 'booking']);
  }

  onMinPriceChange(value: number | string | null | undefined) {
    this.minPrice = value === null || value === undefined || value === ''
      ? undefined
      : Number(value);
    this.fetchResults();
  }

  onMaxPriceChange(value: number | string | null | undefined) {
    this.maxPrice = value === null || value === undefined || value === ''
      ? undefined
      : Number(value);
    this.fetchResults();
  }

  onRatingChange(rate: number | null) {
    this.minRating = rate ?? undefined;
    this.fetchResults();
  }

  onSortChange(value: SearchProviderRequest['sortBy']) {
    this.sort = value;
    this.fetchResults();
  }

  onMaxPriceSliderChange(value: number | string | null) {
    this.maxPrice = value === null || value === '' ? undefined : Number(value);
    this.fetchResults();
  }

  resetFilters() {
    this.sort = 'RATING_DESC';
    this.minPrice = undefined;
    this.maxPrice = undefined;
    this.minRating = undefined;
    this.fetchResults();
  }























}


