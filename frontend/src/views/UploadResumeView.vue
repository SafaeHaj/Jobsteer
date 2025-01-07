<template>
  <div class="upload-resume-page">
      <div class="sidebar">
          <input
              type="file"
              id="resume-upload"
              class="upload-input"
              accept=".pdf"
              @change="onFileChange"
          />
          <label for="resume-upload" class="upload-btn">
              Upload Resume
          </label>
          <div v-if="isLoading" class="loading">
              Processing your resume...
          </div>
          <div v-if="error" class="error">
              {{ error }}
          </div>
      </div>
      <div class="main-content">
          <h2>Top Matching Jobs</h2>
          <div v-if="jobs.length === 0 && !isLoading" class="no-jobs">
              Upload your resume to see matching jobs
          </div>
          <div class="jobs-list" v-else>
              <JobComponent
                  v-for="job in jobs"
                  :key="job.job_post_id"
                  :job="job"
              />
          </div>
      </div>
  </div>
</template>

<script>
import JobComponent from '@/components/JobComponent.vue';
import { useResumeStore } from '@/store/resumeStore';

export default {
  name: 'UploadResumeView',
  components: {
    JobComponent,
  },
  data() {
    return {
      jobs: [],
      isLoading: false,
      error: null,
    };
  },
  methods: {
    async onFileChange(event) {
      const file = event.target.files[0];
      if (!file) return;

      this.error = null;
      this.isLoading = true;

      try {
        const resumeId = await this.uploadResumeFile(file);
        await this.loadMatchingJobs(resumeId);
      } catch (err) {
        this.error = err.message || 'An error occurred';
      } finally {
        this.isLoading = false;
      }
    },

    async uploadResumeFile(file) {
      if (file.type !== 'application/pdf') {
        throw new Error('Please upload a PDF file');
      }

      const resumeStore = useResumeStore();
      const uploadResponse = await resumeStore.uploadResume(file);

      if (!uploadResponse?.resumeId) {
        throw new Error('Failed to upload resume');
      }

      return uploadResponse.resumeId;
    },

    async loadMatchingJobs(resumeId) {
      const resumeStore = useResumeStore();
      this.jobs = await resumeStore.fetchMatchingJobs(resumeId);
    },
  },
};
</script>

<style scoped>
.upload-resume-page {
  display: grid;
  grid-template-columns: 1fr 3fr;
  gap: 2rem;
  padding: 2rem;
  min-height: 80vh;
}

.sidebar {
  padding: 2rem;
  background: #f8f9fa;
  border-radius: 8px;
  text-align: center;
}

.upload-input {
  display: none;
}

.upload-btn {
  background-color: white;
  color: #845aa4;
  border: 2px solid #845aa4;
  padding: 1rem 2rem;
  font-size: 1rem;
  font-weight: 600;
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s;
  display: inline-block;
}

.upload-btn:hover {
  background-color: #845aa4;
  color: white;
}

.main-content {
  padding: 2rem;
}

.main-content h2 {
  margin-bottom: 2rem;
  color: #333;
}

.loading, .error, .no-jobs {
  margin-top: 1rem;
  padding: 1rem;
  text-align: center;
  border-radius: 8px;
}

.loading {
  background: #e9ecef;
  color: #666;
}

.error {
  background: #fee;
  color: #dc3545;
}

.no-jobs {
  background: #f8f9fa;
  color: #666;
}

.jobs-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
</style>
