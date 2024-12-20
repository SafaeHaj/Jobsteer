<template>
    <div class="job-card-header">
        <div class="job-info">
            <h3 class="job-title">{{ job.title }}</h3>
            <p class="job-location">📍 {{ job.location }}</p>
            <p class="job-description">{{ job.description }}</p>
        </div>
        <div class="job-actions">
            <button @click="fetchBestCandidates(job.id)" class="action-button primary">
                Best Candidates
            </button>
            <button @click="deleteJobPost(job.id)" class="action-button danger">
                Delete Job Post
            </button>
        </div>
    </div>
</template>

<script>
import { useJobPostStore } from '@/store/jobpostStore';

export default {
    name: 'RecruiterJobComponent',
    props: {
        job: {
            type: Object,
            required: true
        }
    },
    setup() {
        const jobPostStore = useJobPostStore();
        const fetchBestCandidates = (jobId) => {
            jobPostStore.fetchBestCandidates(jobId);
        };
        const deleteJobPost = async (jobId) => {
            try {
                await jobPostStore.deleteJobPost(jobId);

                jobPostStore.jobPosts = jobPostStore.jobPosts.filter(
                    (job) => job.id !== jobId
                );
            } catch (error) {
                console.error('Failed to delete job post:', error);
            }
        };

        return {
            fetchBestCandidates,
            deleteJobPost
        };
    }
};

</script>