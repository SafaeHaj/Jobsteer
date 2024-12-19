import { defineStore } from 'pinia';
import axiosInstance from './config';
import { useUserStore } from './user';
export const useJobPostStore = defineStore('jobPostStore', {
  state: () => ({
    jobPosts: [],
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
            this.jobPosts = response.data;
          })
          .catch(error => {
            console.error("There was an error fetching the job posts:", error);
          });
      },
    fetchBestCandidates(jobPostId) {
        axiosInstance.get(`/api/jobpost/${jobPostId}/best-candidates`)
          .then(response => {
            console.log("Best candidates:", response.data);
            alert(`Best candidates for job ${jobPostId}: ${JSON.stringify(response.data)}`);
          })
          .catch(error => {
            console.error("There was an error fetching best candidates:", error);
          });
      }
  },
});