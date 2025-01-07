<template>
  <div class="home">
    <div class="left">
    <h2>Connect Talent with <br> <span class="purple">Opportunity</span></h2>
    <p class="text">Whether you're looking for the perfect candidate or the ideal job, we help bridge the gap.</p>
    </div>
    <img src="../assets/asset.svg" alt="Illustration" class="home-image">
  </div>
</template>


<script>

export default {
  name: 'HomeView',
}
</script>

<style scoped>
/* Home container */
.home {
  display: flex;
  align-items: center;
  gap: 50px;
  margin: 40px 20px;
  padding: 40px;
}

/* Heading */
.home h2 {
  font-size: 3.5em; /* Increased font size */
  color: #333;
  font-weight: 600;

  margin-bottom: 30px; /* Increased margin */
}

.home .purple {
  color: #845AA4; /* Purple Accent */
}

/* Paragraph Text */
.home .text {
  font-size: 1.1em; /* Decreased font size */
  color: #666;
  font-weight: 400;
  margin-bottom: 50px; /* Increased margin */
}

.left{
  width: 50%;
}

/* Image */
.home-image {
  width: 100%;
  max-width: 400px;
  margin: 0;
  border-radius: 8px;
}

/* Responsive design for mobile */
@media (max-width: 768px) {
  .home h2 {
    font-size: 28px;
  }

  .home .text {
    font-size: 14px;
  }
}


@media (max-width: 480px) {
  .home h2 {
    font-size: 24px;
  }

  .home .text {
    font-size: 12px;
  }
}
</style>
