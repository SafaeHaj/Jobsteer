<template>
    <div class="min-h-screen bg-gray-50 p-8">
      <div class="max-w-6xl mx-auto grid grid-cols-2 gap-8">
        <!-- Left Column: Job Posts List -->
        <div class="space-y-6">
          <h2 class="text-3xl font-bold text-gray-800">Job Posts for Recruiter {{ recruiterId }}</h2>
          <p class="text-gray-600">Manage your recruitment listings</p>
  
          <div v-if="jobPosts.length === 0" class="text-center py-12">
            <p class="text-gray-600 text-lg">No job posts found for this recruiter.</p>
          </div>
  
          <!-- Job Posts List -->
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
  
        <!-- Right Column: Buttons for Refresh & Create Job Post -->
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
  
          <!-- Job Post Form -->
          <div v-if="showJobPostForm">
            <UploadJobView @job-post-created="onJobPostCreated" />
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  import UploadJobView from './UploadJobView.vue';
  
  export default {
    name: 'RecruiterDashboard',
    components: {
      UploadJobView
    },
    data() {
      return {
        recruiterId: 1,
        jobPosts: [],
        showJobPostForm: false,
      };
    },
    methods: {
      fetchJobPosts() {
        axios.get(`http://localhost:8080/api/jobpost/recruiter/${this.recruiterId}`)
          .then(response => {
            this.jobPosts = response.data;
          })
          .catch(error => {
            console.error("There was an error fetching the job posts:", error);
          });
      },
      fetchBestCandidates(jobPostId) {
        axios.get(`http://localhost:8080/api/jobpost/${jobPostId}/best-candidates`)
          .then(response => {
            console.log("Best candidates:", response.data);
            alert(`Best candidates for job ${jobPostId}: ${JSON.stringify(response.data)}`);
          })
          .catch(error => {
            console.error("There was an error fetching best candidates:", error);
          });
      },
      deleteJobPost(jobPostId) {
        if (confirm("Are you sure you want to delete this job post?")) {
          axios.delete(`http://localhost:8080/api/jobpost/${jobPostId}`)
            .then(() => {
              this.jobPosts = this.jobPosts.filter(job => job.id !== jobPostId);
              alert("Job post deleted successfully.");
            })
            .catch(error => {
              console.error("There was an error deleting the job post:", error);
              alert("There was an error deleting the job post.");
            });
        }
      },
      toggleJobPostForm() {
        this.showJobPostForm = !this.showJobPostForm;
      },
      onJobPostCreated(newJobPost) {
        this.jobPosts.push(newJobPost);
        this.showJobPostForm = false;
      }
    },
    created() {
      this.fetchJobPosts();
    },
  };
  </script>
  <style scoped>
  /* Global Styles */
  body {
    font-family: 'Inter', sans-serif; /* Clean modern font */
    margin: 0;
    padding: 0;
    background-color: #faf5ff; /* Subtle violet background */
    color: #4a4a4a; /* Darker text for contrast */
  }
  
  /* Container for the whole view */
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