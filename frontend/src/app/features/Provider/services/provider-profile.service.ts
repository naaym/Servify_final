import { Injectable, inject } from '@angular/core';
import { Http } from '../../../core/api/http';
import { API_ENDPOINTS } from '../../../core/api/endpoints';
import { ProviderProfile, UpdateProviderProfileRequest } from '../models/provider-profile.model';

@Injectable({ providedIn: 'root' })
export class ProviderProfileService {
  private readonly http = inject(Http);

  getProfile() {
    return this.http.get<ProviderProfile>(API_ENDPOINTS.PROVIDER.PROFILE);
  }

  updateProfile(payload: UpdateProviderProfileRequest) {
    return this.http.patch<ProviderProfile>(API_ENDPOINTS.PROVIDER.PROFILE, payload);
  }

  updateProfilePhoto(file: File) {
    const formData = new FormData();
    formData.append('photo', file);
    return this.http.post<ProviderProfile>(API_ENDPOINTS.PROVIDER.PROFILE_PHOTO, formData);
  }

  addWorkImages(files: File[]) {
    const formData = new FormData();
    files.forEach((file) => {
      formData.append('images', file);
    });
    return this.http.post<ProviderProfile>(API_ENDPOINTS.PROVIDER.PROFILE_WORK_IMAGES, formData);
  }
}
