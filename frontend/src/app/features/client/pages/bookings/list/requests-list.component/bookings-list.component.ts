import { Component, EventEmitter, Output, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { ClientBookingService } from '../../clientbooking.service';
import { BookingResponse } from '../../../../../booking/models/booking-response.model';
import { CommonModule, DatePipe } from '@angular/common';
import { Status } from '../../../../../booking/models/status.model';

@Component({
  selector: 'bookings-list',
  imports: [RouterModule,CommonModule,DatePipe],
  templateUrl: './bookings-list.component.html',
})
export class BookingsListComponent implements OnInit{
 private readonly router=inject(Router)
  bookingsService=inject(ClientBookingService);
   listBookings:BookingResponse[]=[]
  @Output() statusChanged = new EventEmitter<Status>();

  ngOnInit(): void {

    this.bookingsService.getMyBookings().subscribe({
      next:(val)=>this.listBookings=val,
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


}
