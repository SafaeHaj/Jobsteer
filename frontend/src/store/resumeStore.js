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
      this.validateFileType(file);

      const formData = this.prepareFormData(file);
      const customConfig = this.getMultipartConfig();
      const userStore = useUserStore();

      if (!userStore.user?.id) {
        throw new Error('User ID is not available.');
      }

      try {
        return this.currentResume
          ? await this.updateResume(file)
          : await this.createResume(file, userStore.user.id, formData, customConfig);
      } catch (error) {
        this.handleError('Error uploading resume', error);
      }
    },

    async updateResume(file) {
      this.validateFileType(file);

      const formData = this.prepareFormData(file);
      const customConfig = this.getMultipartConfig();

      try {
        const response = await axiosInstance.put(
          `/api/resume/update/${this.currentResume.id}`,
          formData,
          customConfig
        );
        this.currentResume = response.data;
        return response.data;
      } catch (error) {
        this.handleError('Error updating resume', error);
      }
    },

    async createResume(file, userId, formData, customConfig) {
      this.currentResume = null;
      this.jobs = [];

      const response = await axiosInstance.post(
        `/api/resume/upload/${userId}`,
        formData,
        customConfig
      );

      this.currentResume = response.data;
      return response.data;
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
        this.handleError('Error fetching matching jobs', error);
      }
    },

    validateFileType(file) {
      if (file.type !== 'application/pdf') {
        throw new Error('File must be a PDF');
      }
    },

    prepareFormData(file) {
      const formData = new FormData();
      formData.append('file', file);
      return formData;
    },

    getMultipartConfig() {
      return {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      };
    },

    handleError(message, error) {
      console.error(message, error);
      throw error.response?.data?.message || message;
    },
  },
});
