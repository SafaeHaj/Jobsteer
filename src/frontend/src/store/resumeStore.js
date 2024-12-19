import { defineStore } from 'pinia';
import axiosInstance from './config';

export const useResumeStore = defineStore('resumeStore', {
  state: () => ({
    resumes: []
  }),
  actions: {
    async fetchResumes() {
      try {
        const response = await axiosInstance.get('/api/resumes');
        this.resumes = response.data.resumes;
      } catch (error) {
        console.error('Error fetching resumes:', error);
      }
    }
  }
});
