<template>
    <div class="job-detail">
        <div class="job-left-column">
            <h3 class="job-title">{{ job.title }}</h3>
            <p class="job-location">📍 {{ job.location }}</p>
            <p class="job-description">{{ job.description }}</p>
        </div>
        <div class="job-right-column">
            <h4 class="candidate-title">Best Candidates:</h4>
            <div v-if="loading" class="loading-spinner">Loading Candidates...</div>
            <ul v-else class="candidate-list">
                <CandidateComponent v-for="candidate in candidates" :key="candidate.resume_id" :candidate="candidate"
                    :loading="loading" />
            </ul>
        </div>
    </div>
</template>

<script>
import CandidateComponent from '@/components/CandidateComponent.vue';
import { useJobPostStore } from '@/store/jobpostStore';

export default {
    name: 'JobDetailView',
    components: {
        CandidateComponent,
    },
    data() {
        return {
            jobPostStore: useJobPostStore(),
            jobId: this.$route.params.jobId,
            loading: true,
        };
    },
    computed: {
        job() {
            return this.jobPostStore.jobPosts.find((job) => job.id == this.jobId) || {};
        },
        candidates() {
            return this.jobPostStore.candidates[this.jobId] || [];
        },
    },
    mounted() {
        if (!this.jobPostStore.candidates[this.jobId]) {
            this.jobPostStore
                .fetchBestCandidates(this.jobId)
                .then(() => {
                    this.loading = false;
                })
                .catch(() => {
                    this.loading = false;
                });
        } else {
            this.loading = false;
        }
    }
};
</script>



<style scoped>
.loading-spinner {
    text-align: center;
    color: #4a5568;
    font-size: 1.2rem;
    padding: 2rem 0;
}

.job-detail {
    display: flex;
    flex-wrap: wrap;
    padding: 2rem;
    gap: 2rem;
    background-color: #f9f9f9;
}

.job-left-column {
    flex: 1;
    background: white;
    padding: 2rem;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    max-width: 500px;
}

.job-right-column {
    flex: 2;
    background: white;
    padding: 2rem;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    max-width: 800px;
    min-width: 500px;
}

.job-title {
    margin: 0;
    font-size: 2rem;
    font-weight: 600;
    color: #333;
    margin-bottom: 1rem;
}

.job-location,
.job-description {
    color: #666;
    font-size: 1.1rem;
    margin-top: 0.5rem;
}

.job-location {
    font-weight: 500;
}

.candidate-title {
    font-weight: 700;
    color: #2c3e50;
    margin-bottom: 1rem;
    font-size: 1.4rem;
    border-bottom: 2px solid #ecf0f1;
    padding-bottom: 0.5rem;
}

.candidate-list {
    list-style: none;
    padding: 0;
}

.candidate-item {
    background-color: #ffffff;
    border-radius: 10px;
    padding: 1.5rem;
    margin-bottom: 1.5rem;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
    transition: all 0.3s ease;
}

.candidate-item:hover {
    transform: translateY(-5px);
    box-shadow: 0 5px 20px rgba(0, 0, 0, 0.15);
}

.candidate-header h5 {
    margin: 0;
    font-size: 1.4rem;
    font-weight: bold;
    color: #34495e;
    margin-bottom: 0.5rem;
}

.candidate-header p {
    color: #7f8c8d;
    font-size: 1rem;
    margin: 0.2rem 0;
}

.candidate-header p strong {
    font-weight: 600;
    color: #2c3e50;
}

.experience-list {
    list-style: none;
    padding-left: 0;
    margin-top: 1rem;
}

.experience-list li {
    color: #34495e;
    font-size: 1rem;
    margin-bottom: 0.8rem;
    line-height: 1.4;
    padding-left: 1.2rem;
    position: relative;
}

.experience-list li::before {
    content: "•";
    position: absolute;
    left: 0;
    top: 0;
    color: #3498db;
    font-size: 1.5rem;
    line-height: 1;
}

.experience-list li:last-child {
    margin-bottom: 0;
}
</style>
