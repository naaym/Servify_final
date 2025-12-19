export const API_ENDPOINTS = {
  BASE: 'http://localhost:8084/api',
  WS_BASE: 'http://localhost:8084/ws',
  AUTH: {
    LOGIN: 'auth/login',
  },

  CLIENT: {
    REGISTER: 'clients/register',
    PROFILE: 'clients/profile',
    PROFILE_PHOTO: 'clients/profile/photo',

  },

  PROVIDER: {
    BASE:"register",
    REGISTER: 'providers/register',
    SEARCH: 'providers/search',
    BY_ID: (id: number) => `providers/${id}`,
    CHECK: 'providers/check',
    PROFILE: 'providers/profile',
    PROFILE_PHOTO: 'providers/profile/photo',
    SEARCH_OPTIONS: {
      SERVICES: 'providers/search/options/services',
      GOVERNORATES: 'providers/search/options/governorates',
    }
  },
  BOOKING:{
    BASE:'bookings',
    ME:'me',
    STATS:'stats',
    PROVIDER:'provider',
    MESSAGES:(id:number)=>`bookings/${id}/messages`

  }
  ,
  ADMIN:{
    BASE:'admins',
    STATS:'admins/stats',
    PROVIDER_REQUESTS:'admins/providers',
    PROVIDER_STATUS:(id:number)=>`admins/providers/${id}/status`
    ,
    PROVIDERS:'admins/providers'
  }
};
