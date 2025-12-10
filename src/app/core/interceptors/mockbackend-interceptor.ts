import { HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { delay, Observable, of, retry } from 'rxjs';
import { MOCKS } from '../constants/routes.constants';

export const mockbackendInterceptor: HttpInterceptorFn = (req, next) => {
  const { url, body, method } = req;
  if (url.endsWith(MOCKS.LOGIN) && method === 'POST') {
    console.log('mock login active ');
    return mockLogin(body);
  }
  if (url.endsWith(MOCKS.CLIENTS) && method === 'POST') {
    console.log('mock  register active ');
    return mockRegister(body);
  }
  //------------------------------------sendfiles
  if (url.endsWith('/sendfiles') && method === 'POST') {
    console.log('Mock sendFiles actif');
    return mockSendFiles(body as FormData);
  }
  //------------------------------------------createbooking
  if (url.endsWith(MOCKS.BOOKING) && method === 'POST') {
    console.log('%cMock createBooking actif', 'color: cyan');
    return mockCreateBooking(body as FormData);
  }
  //------------------------------------------gettingBooking
  if (url.endsWith(MOCKS.BOOKINGALL) && method === 'GET') {
    return mockGettingAllBookings();
  }
  //-----------------------------------------gettingClientBookingsStats
  if (url.endsWith(MOCKS.BOOKINGSTAT) && method === 'GET') {
    return mockGetClientBookingsStats();
  }
  //---------------------------------------------------gettingClientBookingsDetails
  if(url.endsWith(MOCKS.BOOKINGSDETAIL(10)) && method ==="GET"){
    return mockGetClientBookingDetails();
  }
  //---------------------------------------------------registerProvider
  if(url.endsWith(MOCKS.PROVIDERS) && method==="POST"){
    return mockProviderSignUp(req);
  }
  return next(req);
};

function mockLogin(body: any) {
  const { email, password } = body;
  if (email == 'login@login.com' && password === '123456') {
    return withLatency(
      of(
        new HttpResponse({
          status: 200,
          body: {
            access_token: 'klqljsdfjuiazjt lknxcnmlnxognonj ',
            user: {
              id: '1',
              email: 'login@login.com',
              phone: '12345678',
              address: 'adress123',
              roles: ['ClIENT'],
            },
          },
        })
      )
    );
  }
}

function mockRegister(body: any) {
  const { email, name, address, phone } = body;
  return withLatency(
    of(
      new HttpResponse({
        status: 200,
        body: {
          id: 1,
          email,
          name,
          address,
          phone,
        },
      })
    )
  );
}
//---------------------------------------------------send files
function mockSendFiles(formData: FormData): Observable<HttpResponse<any>> {
  const files = formData.getAll('images') as File[];
  const username = formData.get('name');
  const userlastname = formData.get('lastname');

  const readers = files.map((file) => {
    return new Promise<string>((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => resolve(reader.result as string);
      reader.onerror = () => reject(reader.error);
      reader.readAsDataURL(file);
    });
  });

  return new Observable<HttpResponse<any>>((observer) => {
    Promise.all(readers).then((dataUrls: string[]) => {
      const fakeResponse = {
        message: 'Fichiers reçus et traités avec succès ',
        username,
        userlastname,
        files: dataUrls.map((url, i) => ({
          name: files[i].name,
          type: files[i].type,
          preview: url,
        })),
      };

      observer.next(new HttpResponse({ status: 200, body: fakeResponse }));
      observer.complete();
    });
  }).pipe(delay(1000));
}
//------------------------------------------------ create booking
function mockCreateBooking(fd: FormData) {
  console.log('📦 Booking FormData reçu (mock) :');
  fd.forEach((value, key) => {
    console.log(` → ${key}`, value);
  });

  const response = {
    bookingId: Math.floor(Math.random() * 9999),
    status: 'PENDING',
    providerId: Number(fd.get('providerId')),
    clientId: 77, // FAUX ID en mock
    message: 'Mock booking created successfully.',
    service: fd.get('service'),
    localisation: fd.get('localisation'),
  };
  return withLatency(
    of(
      new HttpResponse({
        status: 200,
        body: response,
      })
    )
  );
}

//------------------------------------------------------gettingAllBoooking
function mockGettingAllBookings() {
  return withLatency(
    of(
      new HttpResponse({
        status: 200,
        body: [
          {
            bookingId: 10,
            status: 'PENDING',
            category: 'WEB DEV',
            providerName: 'EL 3ISSEWI',
            date: Date.now(),
          },
          {
            bookingId: 26,
            status: 'ACCEPTED',
            category: 'DESIGN',
            providerName: 'SANA',
            date: Date.now(),
          },
        ],
      })
    )
  );
}
//-----------------------------------------------gettingClientBookingsStats
function mockGetClientBookingsStats() {
  return withLatency(
    of(
      new HttpResponse({
        status: 200,
        body: {
          totalRequests: 2,
          totalPending: 1,
          totalAccepted: 1,
          totalRejected: 0,
          totalCancelled: 0,
          totalDone: 0,
        },
      })
    )
  );
}
//--------------------------------------------gettingClientBookingDetails
function mockGetClientBookingDetails(){
  return withLatency(of( new HttpResponse({
    status:200,
    body:{
status:"PENDING",
description:"fuite dans ton cerveau ",
bookingId:15648,
date:Date.now(),
updatedAt:Date.now(),
serviceName:"Plombier-fuite cerveau lkjls",
serviceGategory:"Plomberie",
attachments:[{url:"upload//drawinmawjouda/local/image",name:"image.png"}],
providerInfo:{providerId:4562,providerLocalisation:"jandouuba",providerName:"EL 3issewi",providerPhone:"5465000465"
  ,providerImage:{url:"upload//sldifujsf",name:"test.png"},providerRate:4.9
}
    }
  })))
}
//------------------------------------register provider
function mockProviderSignUp(req:any){
const resbody={message:"Register provider done ",
  data:req.body
}

  return withLatency(of(new HttpResponse({status:200,
    body:resbody.data
})))

}

function withLatency(value$: any, time = 600) {
  return value$.pipe(delay(time));
}
