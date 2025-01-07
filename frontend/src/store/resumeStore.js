import { defineStore } from 'pinia';
import axiosInstance from './config';
import { useUserStore } from './user';

export const useResumeStore = defineStore('resumeStore', {
  state: () => ({
    currentResume: null, 
    jobs: [],
    error: null,
  }),
  actions: {
    async uploadResume(file) {
      if (file.type !== 'application/pdf') throw new Error('File must be a PDF');

      const formData = new FormData();
      formData.append('file', file);

      const customConfig = {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      };

      const userStore = useUserStore();
      if (!userStore.user?.id) throw new Error('User ID is not available.');

      try {
        this.currentResume = null;
        this.jobs = [];
        console.log("reached 1");
        const response = await axiosInstance.post(
          `/api/resume/upload/${userStore.user.id}`,
          formData,
          customConfig
        );
        console.log("reached 2");
        this.currentResume = response.data;
        return response.data;
      } catch (error) {
        console.error('Error uploading resume:', error);
        throw error.response?.data?.message || 'Failed to upload resume';
      }
    },

    async fetchMatchingJobs() {
      if (!this.currentResume?.resumeId) {
        throw new Error('No resume available to fetch matching jobs.');
      }

      try {
        const response = await axiosInstance.get(
          `/api/matching/jobs/${this.currentResume.resumeId}`
        );
        this.jobs = response.data;
        return response.data;
      } catch (error) {
        console.error('Error fetching matching jobs:', error);
        throw error.response?.data?.message || 'Failed to fetch matching jobs';
      }
    },
  },
});
