import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClientProfile } from '../../models/profile.model';
import { ClientProfileService } from '../../services/client-profile.service';

@Component({
  selector: 'app-client-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {
  private readonly profileService = inject(ClientProfileService);

  profile: ClientProfile | null = null;
  loading = false;
  photoSaving = false;
  error = '';
  pendingPhotoFile: File | null = null;
  pendingPhotoPreview: string | null = null;

  ngOnInit(): void {
    this.loadProfile();
  }

  get profileImage(): string {
    return this.pendingPhotoPreview || this.profile?.profileImageUrl || '/assets/default-avatar.png';
  }

  loadProfile() {
    this.loading = true;
    this.error = '';
    this.profileService.getProfile().subscribe({
      next: (profile) => {
        this.profile = profile;
        this.loading = false;
      },
      error: (err) => {
        this.error = err?.message ?? 'Impossible de charger votre profil.';
        this.loading = false;
      }
    });
  }

  onPhotoSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) {
      return;
    }

    const file = input.files[0];
    this.pendingPhotoFile = file;

    const reader = new FileReader();
    reader.onload = () => {
      this.pendingPhotoPreview = reader.result as string;
    };
    reader.readAsDataURL(file);
  }

  uploadPhoto() {
    if (!this.pendingPhotoFile) {
      return;
    }

    this.photoSaving = true;
    this.error = '';
    this.profileService.updateProfilePhoto(this.pendingPhotoFile).subscribe({
      next: (profile) => {
        this.profile = profile;
        this.pendingPhotoFile = null;
        this.pendingPhotoPreview = null;
        this.photoSaving = false;
      },
      error: (err) => {
        this.error = err?.message ?? 'Impossible de mettre à jour la photo.';
        this.photoSaving = false;
      }
    });
  }
}
