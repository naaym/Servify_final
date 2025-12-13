export const API_ENDPOINTS = {
  BASE: 'http://localhost:8084/api',
  AUTH: {
    LOGIN: 'auth/login',
  },

  CLIENT: {
    REGISTER: 'clients/register',

  },

  PROVIDER: {
    BASE:"register",
    REGISTER: 'providers/register',
    SEARCH: 'providers/search',
    BY_ID: (id: number) => `providers/${id}`,
    CHECK: 'providers/check'
  },
  BOOKING:{
    BASE:'bookings',
    ME:'me',
    STATS:'stats'

  }
  ,
  ADMIN:{
    BASE:'admins',
    PROVIDER_REQUESTS:'admins/providers',
    PROVIDER_STATUS:(id:number)=>`admins/providers/${id}/status`
  }
};
