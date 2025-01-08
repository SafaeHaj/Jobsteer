<template>
  <NavBar />
  <router-view/>
</template>

<script>
import NavBar from './components/NavBar.vue';

export default {
  components: {
    NavBar,
  }
}
</script>

<style>
:root {
  --primary-color: #845AA4;
  --secondary-color:  #853899;
  --accent-color: #b286ec;
  --background-color: #f8f8f6;
  --text-color: #110420;
}

#app {
  margin: 0;
  padding: 0;
  font-family: Avenir, Helvetica, Arial, sans-serif;
  position: relative;
}

*,
*::before,
*::after {
  box-sizing: border-box;
}

* {
  margin: 0;
}

body {
  line-height: 1.5;
  -webkit-font-smoothing: antialiased;
}

img,
picture,
video,
canvas,
svg {
  display: block;
  max-width: 100%;
}

input,
button,
textarea,
select {
  font: inherit;
}

p,
h1,
h2,
h3,
h4,
h5,
h6 {
  overflow-wrap: break-word;
}

p {
  text-wrap: pretty;
}

h1,
h2,
h3,
h4,
h5,
h6 {
  text-wrap: balance;
}

#root,
#__next {
  isolation: isolate;
}
</style>