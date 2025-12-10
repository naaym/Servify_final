import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { ClientBookingService } from '../../clientbooking.service';
import { BookingResponse } from '../../../../../booking/models/booking-response.model';
import { CommonModule, DatePipe } from '@angular/common';

@Component({
  selector: 'bookings-list',
  imports: [RouterModule,CommonModule,DatePipe],
  templateUrl: './bookings-list.component.html',
})
export class BookingsListComponent implements OnInit{
 private readonly router=inject(Router)
  bookingsService=inject(ClientBookingService);
   listBookings:BookingResponse[]=[]

  ngOnInit(): void {

    this.bookingsService.getMyBookings().subscribe({
      next:(val)=>this.listBookings=val,
      error:(err)=>console.log(err.message)
  })
};



  toViewDetails(id: number|string) {
    this.router.navigate([`/clients/bookings/${id}`])
  }


}
