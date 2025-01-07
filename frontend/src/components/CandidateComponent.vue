<template>
    <div class="candidate-card" v-if="!loading">
        <div class="candidate-header">
            <div class="candidate-info">
                <h4>{{ candidate.name }}</h4>
                <span class="match-badge">{{ (candidate.total_score * 100).toFixed(1) }}% Match</span>
            </div>
            <div class="contact-info">
                <p><span class="icon">📧</span> {{ candidate.email }}</p>
                <p><span class="icon">📍</span> {{ candidate.location }}</p>
            </div>
        </div>

        <div class="match-scores">
            <div class="score-card" v-for="(score, key) in candidate.component_scores" :key="key">
                <p>{{ formatScoreTitle(key) }}</p>
                <div class="progress-bar">
                    <div class="progress" :style="{ width: (score * 100) + '%' }"></div>
                </div>
                <span class="score">{{ (score * 100).toFixed(1) }}%</span>
            </div>
        </div>

        <div class="experiences" v-if="candidate.experiences != null">
            <h5>Key Experiences</h5>
            <ul>
                <li v-for="(exp, idx) in candidate.experiences" :key="idx">
                    {{ exp.description }}
                </li>
            </ul>
        </div>
    </div>
    <div v-else class="loading-spinner">Loading Candidate Details...</div>
</template>

<script>
export default {
    name: 'CandidateComponent',
    props: {
        candidate: {
            type: Object,
            required: false,
            default: null,
        },
        loading: {
            type: Boolean,
            required: true,
        },
    },
    methods: {
        formatScoreTitle(key) {
            return key.replace(/_/g, ' ').replace(/\b\w/g, char => char.toUpperCase());
        },
    },
};
</script>


<style scoped>
.candidate-card {
    background: #f8fafc;
    padding: 1.5rem;
    border-radius: 8px;
    margin-bottom: 1.5rem;
}

.candidate-header {
    margin-bottom: 1.5rem;
}

.candidate-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1rem;
}

.match-badge {
    background: #845aa4;
    color: white;
    padding: 0.4rem 0.8rem;
    border-radius: 20px;
    font-size: 0.9rem;
    font-weight: 500;
}

.contact-info p {
    margin: 0.3rem 0;
    display: flex;
    align-items: center;
    gap: 0.5rem;
}

.match-scores {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 1rem;
    margin-bottom: 1.5rem;
}

.progress-bar {
    background: #e2e8f0;
    height: 6px;
    border-radius: 3px;
    margin-bottom: 0.3rem;
}

.progress {
    background: #845aa4;
    height: 100%;
    border-radius: 3px;
    transition: width 0.3s ease;
}

.score {
    color: #718096;
    font-size: 0.85rem;
    font-weight: 500;
}
</style>
