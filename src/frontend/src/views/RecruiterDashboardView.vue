<template>
    <div class="min-h-screen bg-gray-50 p-8">
      <div class="max-w-6xl mx-auto grid grid-cols-2 gap-8">
        <div class="space-y-6">
          <h2 class="text-3xl font-bold text-gray-800">Job Posts for {{ user.companyName }}</h2>
          <p class="text-gray-600">Manage your recruitment listings</p>
  
          <div v-if="jobPosts.length === 0" class="text-center py-12">
            <p class="text-gray-600 text-lg">No job posts found for this recruiter.</p>
          </div>
  
          <div v-else>
            <div v-for="job in jobPosts" 
                 :key="job.id"
                 class="bg-white p-6 rounded-lg shadow-lg hover:shadow-xl transition-shadow border border-gray-200">
              <div class="flex justify-between items-start">
                <div>
                  <h3 class="text-xl font-semibold text-gray-800">{{ job.title }}</h3>
                  <p class="text-gray-600 mt-1">
                    <span class="inline-flex items-center bg-gray-100 px-2.5 py-0.5 rounded-full text-sm font-medium text-gray-800">
                      📍 {{ job.location }}
                    </span>
                  </p>
                  <p class="mt-3 text-gray-700">{{ job.description }}</p>
                </div>
                <div class="flex gap-2">
                  <button 
                    @click="fetchBestCandidates(job.id)"
                    class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">
                    Best Candidates
                  </button>
                  <button 
                    @click="deleteJobPost(job.id)"
                    class="px-6 py-3 bg-purple-600 text-white text-lg font-semibold rounded-lg hover:bg-purple-700 transition-colors">
                    Delete Job Post
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
  
        <div class="flex flex-col gap-6">
          <button 
            @click="fetchJobPosts"
            class="px-6 py-3 bg-blue-600 text-white text-lg font-semibold rounded-lg hover:bg-blue-700 transition-colors">
            Refresh Job Posts
          </button>
  
          <button 
            v-if="!showJobPostForm"
            @click="toggleJobPostForm"
            class="px-6 py-3 bg-purple-600 text-white text-lg font-semibold rounded-lg hover:bg-purple-700 transition-colors">
            Create Job Post
          </button>
  
          <div v-if="showJobPostForm">
            <UploadJobView @job-post-created="onJobPostCreated" />
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import UploadJobView from './UploadJobView.vue';
  import { useUserStore } from '@/store/user.js';
  import { useJobPostStore } from '@/store/jobpostStore.js';

  export default {
    name: 'RecruiterDashboard',
    components: {
      UploadJobView
    },
    computed: {
      user() {
        const userStore = useUserStore();
        return userStore.user;
      },
      jobPosts() {
        const jobpostStore = useJobPostStore();
        return jobpostStore.jobPosts; // Directly bind to the store's state
      }
    },
    methods: {
      toggleJobPostForm() {
        this.showJobPostForm = !this.showJobPostForm;
      },
      onJobPostCreated(newJobPost) {
        const jobpostStore = useJobPostStore();
        jobpostStore.addJobPost(newJobPost); // Add to the store
        this.showJobPostForm = false;
      },
      fetchJobPosts() {
        const jobpostStore = useJobPostStore();
        jobpostStore.fetchJobPosts(); // Trigger fetching from the store
      }
    },
    created() {
      this.fetchJobPosts(); // Fetch job posts on component creation
    },
  };
  </script>

  <style scoped>
  body {
    font-family: 'Inter', sans-serif; 
    margin: 0;
    padding: 0;
    background-color: #faf5ff; 
    color: #4a4a4a; 
  }
  
  .min-h-screen {
    min-height: 100vh;
  }
  
  .max-w-6xl {
    max-width: 1280px;
  }
  
  .grid {
    display: grid;
  }
  
  .grid-cols-2 {
    grid-template-columns: 1fr 1fr;
  }
  
  .gap-8 {
    gap: 2rem;
  }
  
  .p-8 {
    padding: 2rem;
  }
  
  /* Typography */
  h2 {
    color: #5a4e8c; /* Violet for headings */
  }
  
  .text-3xl {
    font-size: 1.75rem;
    font-weight: bold;
  }
  
  .text-gray-800 {
    color: #4a4a4a;
  }
  
  .text-gray-600 {
    color: #6c6c6c;
  }
  
  .font-bold {
    font-weight: bold;
  }
  
  /* Buttons */
  button {
    font-size: 0.875rem; /* Small buttons */
    padding: 0.5rem 1rem; /* Compact size */
    cursor: pointer;
    font-family: inherit;
    border-radius: 0.25rem; /* Slightly rounded */
    font-weight: 600; /* Emphasis on text */
    border: none; /* Clean look */
    transition: transform 0.2s ease, background-color 0.2s ease;
  }
  
  button:hover {
    transform: scale(1.05); /* Slight hover effect */
  }
  
  .bg-purple-600 {
    background-color: #6a0dad; /* Deep purple */
    color: white;
  }
  
  .bg-violet-600 {
    background-color: #8a2be2; /* Bright violet */
    color: white;
  }
  
  .bg-red-600 {
    background-color: #e53e3e;
    color: white;
  }
  
  .hover\:bg-purple-700:hover {
    background-color: #5a0ca0; /* Darker purple on hover */
  }
  
  .hover\:bg-violet-700:hover {
    background-color: #7a23c0; /* Darker violet on hover */
  }
  
  .hover\:bg-red-700:hover {
    background-color: #c53030;
  }
  
  .rounded-lg {
    border-radius: 0.375rem;
  }
  
  /* Card Design */
  .bg-white {
    background-color: #ffffff;
  }
  
  .p-6 {
    padding: 1.25rem;
  }
  
  .rounded-lg {
    border-radius: 0.375rem;
  }
  
  .shadow-lg {
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  }
  
  .hover\:shadow-xl:hover {
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
  }
  
  .border {
    border: 1px solid #ddd6fe; /* Light purple border */
  }
  
  /* Spacing and Layout */
  .space-y-6 > * + * {
    margin-top: 1.5rem;
  }
  
  .text-center {
    text-align: center;
  }
  
  .inline-flex {
    display: inline-flex;
    align-items: center;
  }
  
  .items-center {
    align-items: center;
  }
  
  .bg-gray-100 {
    background-color: #f3e8ff; /* Light violet */
  }
  
  .text-sm {
    font-size: 0.875rem;
  }
  
  .font-medium {
    font-weight: 500;
  }
  
  .mt-3 {
    margin-top: 0.75rem;
  }
  
  .flex {
    display: flex;
  }
  
  .gap-2 {
    gap: 0.5rem;
  }
  
  .flex-col {
    flex-direction: column;
  }
  
  </style>