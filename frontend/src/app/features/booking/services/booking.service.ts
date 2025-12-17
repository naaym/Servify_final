import { inject, Injectable } from "@angular/core";
import { Http } from "../../../core/api/http";
import { API_ENDPOINTS } from "../../../core/api/endpoints";
import { catchError, throwError } from "rxjs";
import { BookingRequest } from "../models/booking-request.model";
import { BookingResponse } from "../models/booking-response.model";

@Injectable({providedIn:"root"})
export class BookingService {

  http=inject(Http);
  createBooking(dto:BookingRequest){
    const formdata=new FormData();
    formdata.append('providerId',String(dto.providerId));
    formdata.append('date',dto.date);
    formdata.append('time',dto.time);
    formdata.append('description',dto.description);
    dto.attachment!.forEach(file=>formdata.append('images',file))
    return this.http.post<BookingResponse>(`${API_ENDPOINTS.BOOKING.BASE}`,formdata)
    .pipe(catchError((err)=>{
      const normalized={
        message:err.message}
      return throwError(()=>normalized)
  }))
  }



}
