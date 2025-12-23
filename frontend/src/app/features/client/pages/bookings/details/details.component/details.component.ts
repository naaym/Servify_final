import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClientBookingService } from '../../clientbooking.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ClientBookingDetails } from '../../clientbookingdetail.model';
import { Status } from '../../../../../booking/models/status.model';
import { ReviewRequest } from '../../review-request.model';
import { BookingUpdatePayload } from '../../booking-update.model';

@Component({
  selector: 'app-details.component',
  imports: [CommonModule, DatePipe, FormsModule],
  templateUrl: './details.component.html',
  styleUrl: './details.component.scss',
})
export class DetailsComponent implements OnInit {
  private readonly clientbookingservice=inject(ClientBookingService);
  private readonly route=inject(ActivatedRoute)
  private readonly router = inject(Router);
  bookingDetails:ClientBookingDetails|null=null;
  errorMessage:string="";
  reviewError = '';
  reviewSuccess = '';
  isSubmittingReview = false;
  isSavingEdit = false;
  editError = '';
  editMode = false;
  editPayload: BookingUpdatePayload = {
    date: '',
    time: '',
    description: '',
  };
  reviewPayload: ReviewRequest = {
    politenessRating: 5,
    qualityRating: 5,
    punctualityRating: 5,
    comment: '',
  };

  ngOnInit(): void {
    const bookingId=Number(this.route.snapshot.paramMap.get("bookingId"));
    this.editMode = this.route.snapshot.queryParamMap.get('edit') === 'true';
    this.loadDetails(bookingId)

  }
  loadDetails(id:number){
    this.clientbookingservice.getBookingsById(id).subscribe({next:res=>{this.bookingDetails=res;
      this.initializeEditPayload(res);
      if (this.editMode && res.status !== 'PENDING') {
        this.editMode = false;
      }
    },
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

    toggleEdit() {
      if (!this.bookingDetails || this.bookingDetails.status !== 'PENDING') {
        return;
      }
      this.editError = '';
      this.editMode = !this.editMode;
      if (this.editMode) {
        this.initializeEditPayload(this.bookingDetails);
      }
    }

    saveEdit() {
      if (!this.bookingDetails || this.bookingDetails.status !== 'PENDING') {
        return;
      }
      this.isSavingEdit = true;
      this.editError = '';
      this.clientbookingservice.updateBooking(this.bookingDetails.bookingId, this.editPayload).subscribe({
        next: (updated) => {
          this.bookingDetails = updated;
          this.initializeEditPayload(updated);
          this.editMode = false;
          this.isSavingEdit = false;
        },
        error: (err) => {
          this.isSavingEdit = false;
          this.editError = err?.message || "Impossible de modifier la réservation.";
        },
      });
    }

    private initializeEditPayload(details: ClientBookingDetails) {
      const dateValue = new Date(details.date);
      this.editPayload = {
        date: dateValue.toISOString().slice(0, 10),
        time: dateValue.toISOString().slice(11, 16),
        description: details.description ?? '',
      };
    }

    payBooking() {
      if (!this.bookingDetails || this.bookingDetails.status !== 'ACCEPTED') {
        return;
      }
      this.router.navigate(['/checkout'], { queryParams: { bookingId: this.bookingDetails.bookingId } });
    }



}
