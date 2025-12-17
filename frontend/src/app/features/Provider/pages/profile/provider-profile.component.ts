import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProviderProfileService } from '../../services/provider-profile.service';
import { ProviderProfile, UpdateProviderProfileRequest } from '../../models/provider-profile.model';

@Component({
  selector: 'app-provider-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './provider-profile.component.html'
})
export class ProviderProfileComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly profileService = inject(ProviderProfileService);

  profileForm = this.fb.group({
    name: this.fb.control<string | null>('', { validators: [Validators.required] }),
    email: this.fb.control({ value: '', disabled: true }),
    phone: this.fb.control<string | null>('', { validators: [Validators.required] }),
    governorate: this.fb.control<string | null>('', { validators: [Validators.required] }),
    delegation: this.fb.control<string | null>('', { validators: [Validators.required] }),
    age: this.fb.control<number | null>(null),
    serviceCategory: this.fb.control<string | null>('', { validators: [Validators.required] }),
    basePrice: this.fb.control<number | null>(null, { validators: [Validators.required] }),
    description: this.fb.control<string | null>('')
  });

  loading = false;
  saving = false;
  photoSaving = false;
  error = '';
  profileImageUrl: string | null = null;
  pendingPhotoFile: File | null = null;
  pendingPhotoPreview: string | null = null;

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile() {
    this.loading = true;
    this.error = '';
    this.profileService.getProfile().subscribe({
      next: (profile) => {
        this.setProfileForm(profile);
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message ?? 'Impossible de charger votre profil.';
        this.loading = false;
      }
    });
  }

  setProfileForm(profile: ProviderProfile) {
    this.profileForm.patchValue({
      name: profile.name,
      email: profile.email,
      phone: profile.phone,
      governorate: profile.governorate,
      delegation: profile.delegation,
      age: profile.age,
      serviceCategory: profile.serviceCategory,
      basePrice: profile.basePrice,
      description: profile.description || ''
    });
    this.profileImageUrl = profile.profileImageUrl || null;
  }

  onSubmit() {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }

    const formValue = this.profileForm.getRawValue();
    const payload: UpdateProviderProfileRequest = {
      name: formValue.name || undefined,
      phone: formValue.phone || undefined,
      governorate: formValue.governorate || undefined,
      delegation: formValue.delegation || undefined,
      age: formValue.age,
      serviceCategory: formValue.serviceCategory || undefined,
      basePrice: formValue.basePrice,
      description: formValue.description || ''
    };

    this.saving = true;
    this.error = '';
    this.profileService.updateProfile(payload).subscribe({
      next: (updated) => {
        this.setProfileForm(updated);
        this.saving = false;
      },
      error: (err) => {
        this.error = err.message ?? 'Impossible de sauvegarder vos changements.';
        this.saving = false;
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
        this.profileImageUrl = profile.profileImageUrl || null;
        this.pendingPhotoFile = null;
        this.pendingPhotoPreview = null;
        this.photoSaving = false;
      },
      error: (err) => {
        this.error = err.message ?? 'Impossible de mettre à jour la photo.';
        this.photoSaving = false;
      }
    });
  }
}
