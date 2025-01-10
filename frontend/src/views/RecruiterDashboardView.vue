<template>
  <div class="dashboard-container">
    <div class="dashboard-header">
      <h2 class="dashboard-title">Job Posts for {{ user.companyName }}</h2>
    </div>
    <!-- Job Posts Section -->
    <div class="job-posts-section">
      <h3 class="section-subtitle">Manage Your Job Listings</h3>

      <div v-if="jobPosts.length === 0" class="no-posts-message">
        <font-awesome-icon :icon="['fas', 'inbox']" class="text-accent text-2xl mb-2" />
        <p>No job posts found for this recruiter.</p>
      </div>

      <div v-else class="job-posts-list">
        <RecruiterJobComponent v-for="job in jobPosts" :key="job.job_post_id" :job="job" />
      </div>
    </div>
  </div>
</template>

<script>
import RecruiterJobComponent from "@/components/RecruiterJobComponent.vue";
import { useUserStore } from "@/store/user.js";
import { useJobPostStore } from "@/store/jobpostStore.js";

export default {
  name: "RecruiterDashboardView",
  components: {
    RecruiterJobComponent,
  },
  data() {
    return {
      showJobPostForm: false,
      loading: false,
    };
  },
  computed: {
    user() {
      const userStore = useUserStore();
      return userStore.user;
    },
    jobPosts() {
      const jobpostStore = useJobPostStore();
      return jobpostStore.jobPosts;
    },
  },
  methods: {
    onJobPostCreated(newJobPost) {
      const jobpostStore = useJobPostStore();
      jobpostStore.addJobPost(newJobPost);
      this.showJobPostForm = false;
      this.loading = false;
    },
    fetchJobPosts() {
      const jobpostStore = useJobPostStore();
      jobpostStore.fetchJobPosts();
    },
    startLoading() {
      this.loading = true;
    },
    stopLoading() {
      this.loading = false;
    },
  },
  created() {
    this.fetchJobPosts();
  },
};
</script>
