import { Status } from '../../booking/models/status.model';

export interface ProviderBookingResponse {
  bookingId: number;
  status: Status;
  category: string;
  clientName?: string;
  date: number;
}
