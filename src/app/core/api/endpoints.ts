export const API_ENDPOINTS = {
  BASE: 'http://localhost:8084/api/',
  AUTH: {
    LOGIN: 'clients/login',
  },

  CLIENT: {
    BASE: 'clients/',
    BY_ID: (id: number) => `clients/requests/${id}`,
    REGISTER: 'register',

  },

  PROVIDER: {
    BASE: 'providers',
    REGISTER: 'register',
    SEARCH:'search',
    BY_ID:(id:number)=>`/${id}`,
    CHECK:'check'
  },
  BOOKING:{
    BASE:'bookings',
    ME:'me',
    STATS:'stats'

  }
};
