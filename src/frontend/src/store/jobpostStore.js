import { defineStore } from 'pinia';
import axiosInstance from './config';
import { useUserStore } from './user';

export const useJobPostStore = defineStore('jobPostStore', {
  state: () => ({
    jobPosts: JSON.parse(localStorage.getItem('jobPosts')) || [],
    candidates: JSON.parse(localStorage.getItem('candidates')) || {}  
  }),

  actions: {
    async addJobPost(jobPostData) {
      try {
        const response = await axiosInstance.post('/api/jobpost/internal', jobPostData);
        await this.fetchJobPosts();
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
          await this.fetchJobPosts();
        }
        return response;
      } catch (error) {
        console.error('Error deleting job post:', error);
        throw error;
      }
    },
    fetchJobPosts() {
      const userStore = useUserStore();
      axiosInstance.get(`/api/jobpost/recruiter/${userStore.user.id}`)
        .then(response => {
          this.jobPosts = [...response.data];
          localStorage.setItem('jobPosts', JSON.stringify(this.jobPosts));
        })
        .catch(error => {
          console.error('There was an error fetching the job posts:', error);
        });
    },
    fetchBestCandidates(jobPostId) {
      axiosInstance.get(`/api/matching/candidates/${jobPostId}`)
        .then(response => {
          // Store the candidates data in the map with the job post ID as the key
          this.candidates = { ...this.candidates, [jobPostId]: response.data };
          localStorage.setItem('candidates', JSON.stringify(this.candidates));
        })
        .catch(error => {
          console.error('There was an error fetching best candidates:', error);
        });
    }
  },
});
