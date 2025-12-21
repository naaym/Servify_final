import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClientBookingService } from '../../clientbooking.service';
import { ActivatedRoute } from '@angular/router';
import { ClientBookingDetails } from '../../clientbookingdetail.model';
import { Status } from '../../../../../booking/models/status.model';
import { ReviewRequest } from '../../review-request.model';

@Component({
  selector: 'app-details.component',
  imports: [CommonModule, DatePipe, FormsModule],
  templateUrl: './details.component.html',
  styleUrl: './details.component.scss',
})
export class DetailsComponent implements OnInit {
  private readonly clientbookingservice=inject(ClientBookingService);
  private readonly route=inject(ActivatedRoute)
  bookingDetails:ClientBookingDetails|null=null;
  errorMessage:string="";
  reviewError = '';
  reviewSuccess = '';
  isSubmittingReview = false;
  reviewPayload: ReviewRequest = {
    politenessRating: 5,
    qualityRating: 5,
    punctualityRating: 5,
    comment: '',
  };

  ngOnInit(): void {
    const bookingId=Number(this.route.snapshot.paramMap.get("bookingId"));
    this.loadDetails(bookingId)

  }
  loadDetails(id:number){
    this.clientbookingservice.getBookingsById(id).subscribe({next:res=>{this.bookingDetails=res},
      error:err=>this.errorMessage=err.message})

    }

    updateStatus(status:Status){
      if(status !== 'CANCELLED' || !this.bookingDetails) return;
      this.clientbookingservice.updateStatus(this.bookingDetails.bookingId,status).subscribe({
        next:(updated)=>this.bookingDetails=updated,
        error:(err)=>this.errorMessage=err.message
      })
    }

    contactProvider() {
      console.log('Contact provider');
    }

    submitReview() {
      if (!this.bookingDetails || this.bookingDetails.reviewSubmitted || this.bookingDetails.status !== 'DONE') {
        return;
      }
      this.reviewError = '';
      this.reviewSuccess = '';
      this.isSubmittingReview = true;
      this.clientbookingservice.submitReview(this.bookingDetails.bookingId, this.reviewPayload).subscribe({
        next: () => {
          this.isSubmittingReview = false;
          this.reviewSuccess = 'Merci ! Votre avis a bien été envoyé.';
          this.bookingDetails = { ...this.bookingDetails!, reviewSubmitted: true };
        },
        error: (err) => {
          this.isSubmittingReview = false;
          this.reviewError = err?.message || "Impossible d'envoyer votre avis.";
        },
      });
    }




}
