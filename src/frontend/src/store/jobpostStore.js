import { defineStore } from 'pinia';
import axiosInstance from './config';

export const useJobPostStore = defineStore('jobPostStore', {
  state: () => ({
    jobPosts: [],
  }),
  actions: {
    async fetchJobPosts() {
      try {
        const response = await axiosInstance.get('/api/jobposts');
        this.jobPosts = response.data.jobposts;
      } catch (error) {
        console.error('Error fetching job posts:', error);
      }
    },
    async addJobPost(jobPostData) {
      try {
        const response = await axiosInstance.post('/api/jobpost/internal', jobPostData);
        return response.data; // Return the added job post response
      } catch (error) {
        console.error('Error adding job post:', error);
        throw error;
      }
    },
  },
});
