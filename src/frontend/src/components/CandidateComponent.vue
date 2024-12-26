<template>
    <div v-if="candidate" class="candidate-card">
        <div class="candidate-header">
            <div class="candidate-info">
                <h4>{{ candidate.candidate_name }}</h4>
                <span class="match-badge">{{ (candidate.total_score * 100).toFixed(1) }}% Match</span>
            </div>
            <div class="contact-info">
                <p><span class="icon">📧</span> {{ candidate.candidate_email }}</p>
                <p><span class="icon">📍</span> {{ candidate.candidate_location }}</p>
            </div>
        </div>

        <div class="match-scores">
            <div class="score-card">
                <p>Title Match</p>
                <div class="progress-bar">
                    <div class="progress" :style="{ width: (candidate.component_scores.title_match * 100) + '%' }"></div>
                </div>
                <span class="score">{{ (candidate.component_scores.title_match * 100).toFixed(1) }}%</span>
            </div>
            <div class="score-card">
                <p>Experience</p>
                <div class="progress-bar">
                    <div class="progress" :style="{ width: (candidate.component_scores.experience * 100) + '%' }"></div>
                </div>
                <span class="score">{{ (candidate.component_scores.experience * 100).toFixed(1) }}%</span>
            </div>
            <div class="score-card">
                <p>Education</p>
                <div class="progress-bar">
                    <div class="progress" :style="{ width: (candidate.component_scores.education * 100) + '%' }"></div>
                </div>
                <span class="score">{{ (candidate.component_scores.education * 100).toFixed(1) }}%</span>
            </div>
            <div class="score-card">
                <p>Language</p>
                <div class="progress-bar">
                    <div class="progress" :style="{ width: (candidate.component_scores.language * 100) + '%' }"></div>
                </div>
                <span class="score">{{ (candidate.component_scores.language * 100).toFixed(1) }}%</span>
            </div>
        </div>

        <div class="experiences" v-if="candidate.experiences.length > 0">
            <h5>Key Experiences</h5>
            <ul>
                <li v-for="(exp, idx) in candidate.experiences" :key="idx">
                    {{ exp.description }}
                </li>
            </ul>
        </div>
    </div>
    <div v-else class="candidate-header candidate-card">
        Loading Candidates...
    </div>
</template>

<script>
export default {
    name: 'CandidateComponent',
    props: {
        candidate: {
            type: Object,
            required: true,
        },
    }
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

.candidate-info h4 {
    color: #2d3748;
    margin: 0;
    font-size: 1.2rem;
}

.match-badge {
    background: #845aa4;
    color: white;
    padding: 0.4rem 0.8rem;
    border-radius: 20px;
    font-size: 0.9rem;
    font-weight: 500;
}

.contact-info {
    color: #718096;
    font-size: 0.95rem;
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

.score-card {
    background: white;
    padding: 1rem;
    border-radius: 6px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.score-card p {
    color: #4a5568;
    margin: 0 0 0.5rem 0;
    font-size: 0.9rem;
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

.experiences {
    border-top: 1px solid #e2e8f0;
    padding-top: 1rem;
    margin-top: 1rem;
}

.experiences h5 {
    color: #2d3748;
    margin: 0 0 0.8rem 0;
}

.experiences ul {
    list-style: none;
    padding: 0;
    margin: 0;
}

.experiences li {
    color: #4a5568;
    font-size: 0.95rem;
    margin: 0.5rem 0;
    padding-left: 1rem;
    position: relative;
}

.experiences li:before {
    content: "•";
    color: #845aa4;
    position: absolute;
    left: 0;
}
</style>
