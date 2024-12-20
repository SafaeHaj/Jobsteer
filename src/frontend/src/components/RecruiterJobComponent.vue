<template>
    <div class="job-card-header">
        <router-link :to="{ name: 'job-detail', params: { jobId: job.id } }" class="job-link">
            <div class="job-info">
                <h3 class="job-title">{{ job.title }}</h3>
                <p class="job-location">📍 {{ job.location }}</p>
                <p class="job-description">{{ job.description }}</p>
            </div>
        </router-link>
        <button @click="deleteJobPost(job.id)" class="delete-button">
            ✖
        </button>
    </div>
</template>

<script>
import { useJobPostStore } from '@/store/jobpostStore';

export default {
    name: 'RecruiterJobComponent',
    props: {
        job: {
            type: Object,
            required: true,
        },
    },
    data() {
        return {
            jobPostStore: useJobPostStore(),
        };
    },
    methods: {
        async deleteJobPost(jobId) {
            try {
                await this.jobPostStore.deleteJobPost(jobId);
                this.jobPostStore.jobPosts = this.jobPostStore.jobPosts.filter(
                    (job) => job.id !== jobId
                );
            } catch (error) {
                console.error('Failed to delete job post:', error);
            }
        },
    }
};
</script>

<style scoped>
.job-card-header {
    background: white;
    padding: 1.5rem;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    margin-bottom: 1rem;
    display: flex;
    flex-direction: column;
    position: relative; /* Allow for absolute positioning of the delete button */
}

.job-info {
    margin-bottom: 1rem;
}

.job-title {
    margin: 0;
    font-size: 1.25rem;
    font-weight: 600;
    color: #333;
}

.job-location,
.job-description {
    color: #666;
    font-size: 0.95rem;
    margin-top: 0.5rem;
}

.delete-button {
    position: absolute;
    top: 1rem;
    right: 1rem;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background-color: #ff4c4c;
    color: white;
    border: none;
    font-size: 0.8rem;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.delete-button:hover {
    background-color: #e63636;
}

.candidate-list {
    margin-top: 1rem;
    padding-top: 1rem;
    border-top: 1px solid #eee;
}

.candidate-title {
    font-weight: 600;
    color: #333;
}

.candidate-list ul {
    list-style: none;
    padding: 0;
    margin-top: 0.5rem;
}

.candidate-list li {
    color: #666;
    font-size: 0.95rem;
    margin: 0.25rem 0;
}
</style>
