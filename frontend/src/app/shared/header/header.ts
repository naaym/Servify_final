import { Component, OnInit, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { TokenService } from '../../core/services/token.service';
import { ClientProfileService } from '../../features/client/services/client-profile.service';
import { ProviderProfileService } from '../../features/Provider/services/provider-profile.service';


@Component({
  selector: 'app-header',
  imports: [RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header implements OnInit {
  router = inject(Router);
  private readonly tokenService = inject(TokenService);
  private readonly clientProfileService = inject(ClientProfileService);
  private readonly providerProfileService = inject(ProviderProfileService);

  isMenuOpen = false;
  isAuthenticated = false;
  hasClientOrProviderRole = false;
  readonly defaultAvatarUrl = '/assets/default-avatar.png';
  profileImageUrl = this.defaultAvatarUrl;

  ngOnInit(): void {
    this.isAuthenticated = this.tokenService.isLoggedIn();
    if (!this.isAuthenticated) {
      return;
    }

    const roles = this.tokenService.getRoles();
    this.hasClientOrProviderRole = roles.includes('CLIENT') || roles.includes('PROVIDER');

    if (this.hasClientOrProviderRole) {
      this.loadProfileImage(roles);
    }
  }

  goToHome() {
    this.router.navigate(['/']);
  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  get showUserAvatar(): boolean {
    return this.isAuthenticated && this.hasClientOrProviderRole;
  }

  private loadProfileImage(roles: string[]) {
    if (roles.includes('CLIENT')) {
      this.clientProfileService.getProfile().subscribe({
        next: (profile) => {
          this.profileImageUrl = profile.profileImageUrl || this.defaultAvatarUrl;
        },
        error: () => (this.profileImageUrl = this.defaultAvatarUrl),
      });
      return;
    }

    if (roles.includes('PROVIDER')) {
      this.providerProfileService.getProfile().subscribe({
        next: (profile) => {
          this.profileImageUrl = profile.profileImageUrl || this.defaultAvatarUrl;
        },
        error: () => (this.profileImageUrl = this.defaultAvatarUrl),
      });
    }
  }
}
