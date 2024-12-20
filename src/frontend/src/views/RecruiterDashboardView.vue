<template>
  <div class="dashboard-container">
    <div class="dashboard-grid">
      <h2 class="section-title">Job Posts for {{ user.companyName }}</h2>

      <div class="actions-section">
        <button @click="fetchJobPosts" class="action-button primary">
          Refresh Job Posts
        </button>
        <button v-if="!showJobPostForm" @click="toggleJobPostForm" class="action-button secondary">
          Create Job Post
        </button>
        <VModal :isVisible="showJobPostForm" @close="showJobPostForm = false">
          <UploadJobForm @job-post-created="onJobPostCreated" />
        </VModal>
      </div>

      <div class="job-posts-section">
        <p class="section-subtitle">Manage your recruitment listings</p>

        <div v-if="jobPosts.length === 0" class="no-posts-message">
          <p>No job posts found for this recruiter.</p>
        </div>

        <div v-else class="job-posts-list">
          <RecruiterJobComponent
            v-for="job in jobPosts"
            :key="job.job_post_id"
            :job="job"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UploadJobForm from '@/components/UploadJobForm.vue';
import RecruiterJobComponent from '@/components/RecruiterJobComponent.vue';
import VModal from '@/components/VModal.vue';
import { useUserStore } from '@/store/user.js';
import { useJobPostStore } from '@/store/jobpostStore.js';

export default {
  name: 'RecruiterDashboardView',
  components: {
    UploadJobForm,
    RecruiterJobComponent,
    VModal
  },
  data() {
    return {
      showJobPostForm: false
    }
  },
  computed: {
    user() {
      const userStore = useUserStore();
      return userStore.user;
    },
    jobPosts() {
      const jobpostStore = useJobPostStore();
      return jobpostStore.jobPosts;
    }
  },
  methods: {
    toggleJobPostForm() {
      this.showJobPostForm = !this.showJobPostForm;
    },
    onJobPostCreated(newJobPost) {
      const jobpostStore = useJobPostStore();
      jobpostStore.addJobPost(newJobPost);
      this.showJobPostForm = false;
    },
    fetchJobPosts() {
      const jobpostStore = useJobPostStore();
      jobpostStore.fetchJobPosts();
    }
  },
  created() {
    this.fetchJobPosts();
  },
};
</script>

<style scoped>
.dashboard-container {
  min-height: 100vh;
  padding: 2rem;
  background-color: #f9f9f9;
}

.dashboard-grid {
  display: grid;
  grid-template-rows: auto 1fr; /* Title row and second row */
  grid-template-columns: 1fr 2fr; /* Actions section and Job posts */
  gap: 2rem;
  max-width: 1280px;
  margin: 0 auto;
}

.section-title {
  grid-column: 1 / -1; /* Span across both columns */
  font-size: 1.75rem;
  color: #5a4e8c;
  margin-bottom: 1rem;
}

.actions-section {
  grid-row: 2;
  grid-column: 1; /* Left side of second row */
  background-color: #ffffff;
  border-radius: 8px;
  padding: 1rem;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 1rem; /* Space between buttons */
}

.job-posts-section {
  grid-row: 2;
  grid-column: 2; /* Right side of second row */
  background-color: #ffffff;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.section-subtitle {
  font-size: 1rem;
  color: #6c6c6c;
  margin-bottom: 1.5rem;
}

.no-posts-message {
  text-align: center;
  font-size: 1.1rem;
  color: #6c6c6c;
}

.job-posts-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.action-button {
  padding: 0.6rem 1.2rem;
  border-radius: 6px;
  border: none;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.action-button.primary {
  background-color: #633b81;
  color: white;
}

.action-button.primary:hover {
  background-color: #5a0ca0;
}

.action-button.secondary {
  background-color: #845AA4;
  color: white;
}

.action-button.secondary:hover {
  background-color: #7a23c0;
}

/* Responsive Adjustments */
@media screen and (max-width: 768px) {
  .dashboard-grid {
    grid-template-rows: auto auto auto;
    grid-template-columns: 1fr; /* Single column layout */
  }

  .actions-section {
    grid-column: 1;
  }

  .job-posts-section {
    grid-column: 1;
  }
}

</style>
