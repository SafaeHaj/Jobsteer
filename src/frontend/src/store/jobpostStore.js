import { defineStore } from 'pinia';
import axiosInstance from './config';

export const useJobPostStore = defineStore('jobPostStore', {
  state: () => ({
    jobPosts: [],
  }),

  actions: {
    async fetchJobPosts(recruiterId = null) {
      try {
        const endpoint = recruiterId 
          ? `/api/jobpost/recruiter/${recruiterId}` 
          : '/api/jobpost';
        
        const response = await axiosInstance.get(endpoint);
        console.log("Fetched job posts:", response.data); // Debugging output
        this.jobPosts = response.data; // Set the job posts to the store
      } catch (error) {
        console.error('Error fetching job posts:', error);
      }
    },

    async addJobPost(jobPostData) {
      try {
        const response = await axiosInstance.post('/api/jobpost/internal', jobPostData);
        await this.fetchJobPosts(); // Refresh the list after adding
        return response.data;
      } catch (error) {
        console.error('Error adding job post:', error);
        throw error;
      }
    },

    async deleteJobPost(jobId) {
      try {
        const response = await axiosInstance.delete(`/api/jobpost/${jobId}`);
        if (response.status === 200) {
          await this.fetchJobPosts(); // Refresh the list after deletion
        }
        return response;
      } catch (error) {
        console.error('Error deleting job post:', error);
        throw error;
      }
    }
  },
});