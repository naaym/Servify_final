import { Component, EventEmitter, Output, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { ClientBookingService } from '../../clientbooking.service';
import { BookingResponse } from '../../../../../booking/models/booking-response.model';
import { CommonModule, DatePipe } from '@angular/common';
import { Status } from '../../../../../booking/models/status.model';
import { PaymentHistoryItem, PaymentService } from '../../../../payments/services/payment.service';

@Component({
  selector: 'bookings-list',
  imports: [RouterModule,CommonModule,DatePipe],
  templateUrl: './bookings-list.component.html',
})
export class BookingsListComponent implements OnInit{
 private readonly router=inject(Router)
  bookingsService=inject(ClientBookingService);
  private readonly paymentService = inject(PaymentService);
   listBookings:BookingResponse[]=[]
  paidBookingIds = new Set<number>();
  @Output() statusChanged = new EventEmitter<Status>();

  ngOnInit(): void {

    this.bookingsService.getMyBookings().subscribe({
      next:(val)=>this.listBookings=val,
      error:(err)=>console.log(err.message)
  })

    this.paymentService.getClientHistory().subscribe({
      next: (payments: PaymentHistoryItem[]) => {
        this.paidBookingIds = new Set(
          payments
            .filter((payment) => payment.status === 'SUCCEEDED')
            .map((payment) => payment.bookingId)
        );
      },
      error:(err)=>console.log(err.message)
    })
};

  onCancel(bookingId:number){
    this.bookingsService.updateStatus(bookingId,'CANCELLED').subscribe({
      next:(updated)=>{
        this.listBookings=this.listBookings.map((b)=>b.bookingId===bookingId?{...b,status:updated.status as Status}:b)
        this.statusChanged.emit(updated.status as Status);
      },
      error:(err)=>console.log(err.message)
    })
  }



  toViewDetails(id: number|string) {
    this.router.navigate([`/clients/bookings/${id}`])
  }

  onModify(id: number) {
    this.router.navigate([`/clients/bookings/${id}`], { queryParams: { edit: 'true' } });
  }

  onPay(id: number) {
    this.router.navigate(['/checkout'], { queryParams: { bookingId: id } });
  }

  canPay(booking: BookingResponse) {
    return booking.status === 'ACCEPTED' && !this.paidBookingIds.has(booking.bookingId);
  }


}
