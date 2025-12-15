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
    CHECK: 'providers/check',
    SEARCH_OPTIONS: {
      SERVICES: 'providers/search/options/services',
      GOVERNORATES: 'providers/search/options/governorates',
    }
  },
  BOOKING:{
    BASE:'bookings',
    ME:'me',
    STATS:'stats'

  }
  ,
  ADMIN:{
    BASE:'admins',
    STATS:'admins/stats',
    PROVIDER_REQUESTS:'admins/providers',
    PROVIDER_STATUS:(id:number)=>`admins/providers/${id}/status`
  }
};
