import { defineStore } from 'pinia';
import axiosInstance from './config';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
  }),
  actions: {
    initializeUser() {
      const cachedUser = localStorage.getItem('user');
      if (cachedUser) {
        this.user = JSON.parse(cachedUser);
      }
    },
    
    async fetchDataFromApi() {
      const token = localStorage.getItem('authToken');
      if (!token) {
        console.error('No token found for authentication.');
        return;
      }
      try {
        const response = await axiosInstance.get('/api/auth/user', {
          headers: {
            Authorization: `Bearer ${token}`
          },
        });

        this.user = { ...response.data, role: this.user?.role };  
        localStorage.setItem('user', JSON.stringify(this.user));
      } catch (error) {
        console.error('Error fetching user data:', error);
        throw error;
      }
    },
    
    async registerUser(formData) {
      try {
        const response = await axiosInstance.post('/api/auth/register', formData); 
        this.user = {...response.data};
        return response.data; 
      } catch (error) {
        console.error('Registration error:', error);
        throw error; 
      }
    },
    
    async loginUser(formData) {
      try {
        const response = await axiosInstance.post('/api/auth/login', formData);
        this.user = {...response.data};
        localStorage.setItem('user', this.user); 
        localStorage.setItem('authToken', response.data.token); 
        return response; 
      } catch (error) {
        console.error('Login error:', error);
        throw error; 
      }
    },
    async updateUser(userData) {
      const token = localStorage.getItem('authToken');
      const response = await axiosInstance.put(`/api/users/${this.user.id}`, userData, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      this.user = response.data;
      localStorage.setItem('user', JSON.stringify(this.user));
      return response.data;
    },
    logoutUser() {
      this.user = null;
      localStorage.removeItem("authToken");
      localStorage.removeItem("user");
    }
    
  },
  
  getters: {
    userDetails: (state) => state.user, 
  },
});
