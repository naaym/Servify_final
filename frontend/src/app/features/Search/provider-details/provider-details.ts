import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { SearchProviderService } from '../services/provider-search.service';
import { Provider } from '../models/provider.model';
import { Header } from '../../../shared/header/header';

@Component({
  selector: 'app-provider-details',
  imports: [CommonModule, RouterLink, Header],
  templateUrl: './provider-details.html',
  styleUrl: './provider-details.scss',
})
export class ProviderDetails implements OnInit {
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly searchService = inject(SearchProviderService);
  private readonly router = inject(Router);

  provider?: Provider;
  isLoading = true;
  errorMessage = '';
  readonly defaultAvatarUrl = '/assets/default-avatar.png';

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
        },
        error: () => {
          this.errorMessage = 'Impossible de charger le profil du provider.';
          this.isLoading = false;
        },
      });
    });
  }

  bookProvider() {
    if (!this.provider) {
      return;
    }

    this.router.navigate(['/providers', this.provider.id, 'booking']);
  }

}
