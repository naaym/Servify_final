import { inject, Injectable } from '@angular/core';
import { Http } from '../../../../core/api/http';
import { BookingResponse } from '../../../booking/models/booking-response.model';
import { API_ENDPOINTS } from '../../../../core/api/endpoints';
import { catchError, throwError } from 'rxjs';
import { StatsBooking } from './statsbooking.model';
import { ClientBookingDetails } from './clientbookingdetail.model';
import { Status } from '../../../booking/models/status.model';

@Injectable({ providedIn: 'root' })
export class ClientBookingService {
  http = inject(Http);

  getMyBookings() {
    return this.http
      .getAll<BookingResponse>(
        `${API_ENDPOINTS.BOOKING.BASE}/${API_ENDPOINTS.BOOKING.ME}`
      )
      .pipe(
        catchError((error) => {
          const normalized = {
            message: error.message,
          };
          return throwError(() => normalized);
        })
      );
  }
  getMyStats() {
    return this.http
      .get<StatsBooking>(
        `${API_ENDPOINTS.BOOKING.BASE}/${API_ENDPOINTS.BOOKING.ME}/${API_ENDPOINTS.BOOKING.STATS}`
      )
      .pipe(
        catchError((error) => {
          const normalized = {
            message: error.message,
          };
          return throwError(() => normalized);
        })
      );
  }
  getBookingsById(id: number) {
    return this.http
      .getOne<ClientBookingDetails>(`${API_ENDPOINTS.BOOKING.BASE}`, id)
      .pipe(
        catchError((error) => {
          const normalized = { message: error.message };
          return throwError(() => normalized);
        })
      );
  }
  updateStatus(bookingId: number, status: Status) {
    return this.http.patch<ClientBookingDetails>(
      `${API_ENDPOINTS.BOOKING.BASE}/${bookingId}/status`,
      { status }
    );
  }
  //getMyRecentBookings()
}
