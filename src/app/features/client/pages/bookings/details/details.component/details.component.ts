import { Component, inject, OnInit } from '@angular/core';
import { Attachment,DetailsRequest,TimelineStep } from '../../../../models/details.model';
import { CommonModule, DatePipe } from '@angular/common';
import { ClientBookingService } from '../../clientbooking.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ClientBookingDetails } from '../../clientbookingdetail.model';

@Component({
  selector: 'app-details.component',
  imports: [CommonModule,DatePipe],
  templateUrl: './details.component.html',
  styleUrl: './details.component.scss',
})
export class DetailsComponent implements OnInit {
  private readonly clientbookingservice=inject(ClientBookingService);
  private readonly route=inject(ActivatedRoute)
  bookingDetails:ClientBookingDetails|null=null;
  errorMessage:string="";

  ngOnInit(): void {
    const bookingId=Number(this.route.snapshot.paramMap.get("bookingId"));
    this.loadDetails(bookingId)

  }
  loadDetails(id:number){
    this.clientbookingservice.getBookingsById(id).subscribe({next:res=>{this.bookingDetails=res},
      error:err=>this.errorMessage=err.message})

    }


    cancelRequest() {
      console.log('Request cancelled');
    }

    contactProvider() {
      console.log('Contact provider');
    }
    //-----------------------------------------------------------------

    //  request = {
    //   id: 1,
    //   category: 'Plomberie – Fuite salle de bain',
    //   date: '12 Jan 2025',
    //   provider: 'Ahmed',
    //   status: 'accepted',
    //   description: 'Fuite sous le lavabo, urgence.',
    //   timeline: [
    //     { label: 'Demande créée', date: '12 Jan, 14:20' },
    //     { label: 'Consultée par Ahmed', date: '12 Jan, 14:35' },
    //     { label: 'Acceptée', date: '12 Jan, 14:40' }
    //   ]
    // };


}
