<template>
  <half-circle-spinner class="centered"
    :animation-duration="1000"
    :size="60"
    color="#7a23c0"
  />
</template>

<script>
import { HalfCircleSpinner } from "epic-spinners";

export default {
  name: "LoadingAnimation",
  components: {
    HalfCircleSpinner
  }
};
</script>


<style scoped>
.centered{
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>