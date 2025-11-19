const API_BASE_URL = 'http://localhost:8080/api';

// Helper function for API calls
const apiCall = async (endpoint, options = {}) => {
  const url = `${API_BASE_URL}${endpoint}`;
  
  const config = {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  };

  try {
    const response = await fetch(url, config);
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('API Error:', error);
    throw error;
  }
};

// Auth APIs
export const authAPI = {
  login: (email, password) => 
    apiCall('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    }),

  register: (name, email, password) => 
    apiCall('/auth/register', {
      method: 'POST',
      body: JSON.stringify({ name, email, password }),
    }),

  logout: () => 
    apiCall('/auth/logout', {
      method: 'POST',
    }),
};

// Flight APIs
export const flightAPI = {
  getAllFlights: () => apiCall('/flights'),
  
  getFlightById: (id) => apiCall(`/flights/${id}`),
  
  searchFlights: (from, to) => {
    const params = new URLSearchParams();
    if (from) params.append('from', from);
    if (to) params.append('to', to);
    return apiCall(`/flights?${params.toString()}`);
  },
};

// Booking APIs
export const bookingAPI = {
  createBooking: (bookingData) => 
    apiCall('/bookings', {
      method: 'POST',
      body: JSON.stringify(bookingData),
    }),

  getUserBookings: (email) => 
    apiCall(`/bookings?email=${email}`),

  getBookingById: (id) => 
    apiCall(`/bookings/${id}`),

  cancelBooking: (id) => 
    apiCall(`/bookings/${id}`, {
      method: 'DELETE',
    }),
};

// User APIs
export const userAPI = {
  getProfile: (email) => 
    apiCall(`/users/profile?email=${email}`),

  updateProfile: (email, updates) => 
    apiCall(`/users/profile?email=${email}`, {
      method: 'PUT',
      body: JSON.stringify(updates),
    }),

  updateLocation: (email, location) => 
    apiCall(`/users/location?email=${email}`, {
      method: 'PUT',
      body: JSON.stringify({ location }),
    }),
};