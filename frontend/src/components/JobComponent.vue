<template>
    <div class="job-card">
        <div class="job-header">
            <h3>{{ job.job_title }}</h3>
            <div class="match-score">
                Match: {{ (job.total_score * 100).toFixed(1) }}%
            </div>
        </div>
        <p class="company" v-if="job.recruiter?.companyName">
            {{ job.recruiter.companyName }}
        </p>
        <p class="location" v-if="job.job_location">
            📍 {{ job.job_location }}
        </p>
        <p class="location" v-if="job.job_toApply">
              {{ job.job_toApply }}
        </p>
        <div class="match-details">
            <div v-if="job.component_scores">
                <p>Match Details:</p>
                <ul>
                    <li v-if="job.component_scores.title_match">
                        Title Match: {{ (job.component_scores.title_match * 100).toFixed(1) }}%
                    </li>
                    <li v-if="job.component_scores.experience">
                        Experience: {{ (job.component_scores.experience * 100).toFixed(1) }}%
                    </li>
                    <li v-if="job.component_scores.education">
                        Education: {{ (job.component_scores.education * 100).toFixed(1) }}%
                    </li>
                    <li v-if="job.component_scores.language">
                        Language: {{ (job.component_scores.language * 100).toFixed(1) }}%
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'JobComponent',
    props: {
        job: {
            type: Object,
            required: true
        }
    }
};
</script>

<style scoped>
.job-card {
    background: white;
    padding: 1.5rem;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    margin-bottom: 1rem;
}

.job-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 0.5rem;
}

.job-header h3 {
    margin: 0;
    color: #333;
    font-size: 1.2rem;
}

.match-score {
    background: #845AA4;
    color: white;
    padding: 0.25rem 0.75rem;
    border-radius: 15px;
    font-size: 0.9rem;
}

.company {
    color: #666;
    font-weight: 500;
    margin: 0.5rem 0;
}

.location {
    color: #666;
    font-size: 0.9rem;
    margin: 0.5rem 0;
}

.match-details {
    margin-top: 1rem;
    border-top: 1px solid #eee;
    padding-top: 1rem;
}

.match-details ul {
    list-style: none;
    padding: 0;
    margin: 0.5rem 0;
}

.match-details li {
    color: #666;
    font-size: 0.9rem;
    margin: 0.25rem 0;
}

.apply-btn {
    background: #845AA4;
    color: white;
    border: none;
    padding: 0.5rem 1rem;
    border-radius: 5px;
    cursor: pointer;
    margin-top: 1rem;
}

.apply-btn:hover {
    background: #6c4891;
}
</style>