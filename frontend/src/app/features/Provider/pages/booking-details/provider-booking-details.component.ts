import { CommonModule, DatePipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Status } from '../../../booking/models/status.model';
import { ClientBookingDetails } from '../../../client/pages/bookings/clientbookingdetail.model';
import { ProviderBookingService } from '../../services/provider-booking.service';

@Component({
  selector: 'app-provider-booking-details',
  standalone: true,
  imports: [CommonModule, RouterModule, DatePipe],
  templateUrl: './provider-booking-details.component.html',
  styleUrl: './provider-booking-details.component.scss',
})
export class ProviderBookingDetailsComponent implements OnInit {
  private readonly bookingService = inject(ProviderBookingService);
  private readonly route = inject(ActivatedRoute);

  booking: ClientBookingDetails | null = null;
  loading = false;
  errorMessage = '';

  ngOnInit(): void {
    const bookingId = Number(this.route.snapshot.paramMap.get('bookingId'));
    if (!bookingId) {
      this.errorMessage = 'Identifiant de réservation invalide';
      return;
    }
    this.loadBooking(bookingId);
  }

  loadBooking(bookingId: number) {
    this.loading = true;
    this.errorMessage = '';
    this.bookingService.getBookingDetails(bookingId).subscribe({
      next: (booking) => {
        this.booking = booking;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err.message;
        this.loading = false;
      },
    });
  }

  updateStatus(status: Status) {
    if (!this.booking) return;
    this.bookingService.updateStatus(this.booking.bookingId, status).subscribe({
      next: (updated) => (this.booking = updated),
      error: (err) => (this.errorMessage = err.message),
    });
  }

  get canAcceptOrReject() {
    return this.booking?.status === 'PENDING';
  }

  get canComplete() {
    return this.booking?.status === 'ACCEPTED';
  }
}
