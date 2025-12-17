

export const MOCKS = {

  CLIENTS: 'clients/register',
  PROVIDERS: 'providers/register',
  LOGIN:'auth/login',
  BOOKING:'/bookings',
  BOOKINGALL:'/bookings/me',
  BOOKINGSTAT:'bookings/me/stats',
  BOOKINGSDETAIL:(id:number)=>`bookings/${id}`
};
