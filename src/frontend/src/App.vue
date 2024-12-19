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
body {
  font-family: sans-serif;
  margin: 0;
  padding:0;
}
</style>