<template>
    <div class="container">
        <h2>{{ resume.candidate.firstName }} {{ resume.candidate.lastName }}</h2>
        <p> {{ resume.location }}</p>
        <p>{{ resume.candidate.email }}</p>
    </div>
</template>


<script>
import { mapState } from 'vuex';

export default {
    name: 'ResumeComponent',
    computed: {
        ...mapState({
            resume: state => state.UploadJobView.resume
        })
    }
};
</script>