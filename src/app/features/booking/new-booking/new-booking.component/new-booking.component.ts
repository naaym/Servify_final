import { Component, inject} from '@angular/core';
import { AttachmentService } from '../../services/attachment.service';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { BookingService } from '../../services/booking.service';
import { BookingRequest } from '../../models/booking-request.model';


@Component({
  selector: 'app-new-booking.component',
  imports: [ReactiveFormsModule],
  templateUrl: './new-booking.component.html',
  styleUrl: './new-booking.component.scss',
})
export class NewBookingComponent {

  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly bookingService = inject(BookingService);
  private readonly attachmentService = inject(AttachmentService);


  preview: string[] = [];
  selectedFiles: File[] = [];
  providerId=Number(this.route.snapshot.queryParamMap.get('id'))

  onUploadAttachments(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files?.length === 0){
      return};

    this.selectedFiles = Array.from(input.files);
    this.preview = this.attachmentService.onUploadAttachments(
      this.selectedFiles,
      this.preview
    );
  }

  form=this.fb.nonNullable.group({
    date:['',Validators.required],
    time:['',Validators.required],
    description:['',Validators.required],
  })


  onSubmit(){
    const dto: BookingRequest = {
    providerId: this.providerId,
    date: this.form.value.date!,
    time: this.form.value.time!,
    description: this.form.value.description!,
    attachment:this.selectedFiles,

  };
    if(this.form.invalid){
      this.form.markAllAsTouched();
      console.log("form unsubmitted")
      return
    }
    this.bookingService.createBooking(dto).subscribe({
      next:(res)=>{
      console.log("booking created",res)
      this.router.navigate(['/clients/dashboard'])
    },

    })



    }




  isValid(nameControl:string):boolean{
    const control=this.form.get(nameControl);
    if(!control)return false;
    return control.invalid && (control.dirty||control.touched)
  }
  deletePhoto(index:number){
    this.selectedFiles.splice(index,1);
    this.preview.splice(index,1);
  }


}
