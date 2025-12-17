import { inject, Injectable } from '@angular/core';
import { Http } from '../../../core/api/http';
import { API_ENDPOINTS } from '../../../core/api/endpoints';
import { ProviderBookingResponse } from '../models/provider-booking.model';
import { Status } from '../../booking/models/status.model';
import { ClientBookingDetails } from '../../client/pages/bookings/clientbookingdetail.model';

@Injectable({ providedIn: 'root' })
export class ProviderBookingService {
  private readonly http = inject(Http);

  getProviderBookings() {
    return this.http.getAll<ProviderBookingResponse>(
      `${API_ENDPOINTS.BOOKING.BASE}/${API_ENDPOINTS.BOOKING.PROVIDER}`
    );
  }

  updateStatus(bookingId: number, status: Status) {
    return this.http.patch<ClientBookingDetails>(
      `${API_ENDPOINTS.BOOKING.BASE}/${API_ENDPOINTS.BOOKING.PROVIDER}/${bookingId}/status`,
      { status }
    );
  }
}
