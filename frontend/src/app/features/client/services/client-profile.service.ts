import { Injectable, inject } from '@angular/core';
import { ClientProfile } from '../models/profile.model';
import { Http } from '../../../core/api/http';
import { API_ENDPOINTS } from '../../../core/api/endpoints';

@Injectable({ providedIn: 'root' })
export class ClientProfileService {
  private readonly http = inject(Http);

  getProfile() {
    return this.http.get<ClientProfile>(API_ENDPOINTS.CLIENT.PROFILE);
  }

  updateProfilePhoto(photo: File) {
    const formData = new FormData();
    formData.append('photo', photo);
    return this.http.post<ClientProfile>(API_ENDPOINTS.CLIENT.PROFILE_PHOTO, formData);
  }
}
