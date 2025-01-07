<template>
    <div v-if="isVisible" class="modal-overlay" @click.self="closeModal">
        <div class="modal-content">
            <button class="close-button" @click="closeModal">&times;</button>
            <slot></slot>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        isVisible: {
            type: Boolean,
            required: true,
        },
    },
    emits: ['close'],
    methods: {
        closeModal() {
            this.$emit('close');
        },
    },
};
</script>

<style>
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.6);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
    overflow: hidden; 
}

.modal-content {
  overflow-y: scroll;
  background: #fff;
  max-height: 80vh;
  padding:20px 40px;
  border-radius: 20px;
}

.modal-content::-webkit-scrollbar {
  display: none; /* Hide scrollbar for Webkit browsers */
}

.modal-content {
  -ms-overflow-style: none; /* Hide scrollbar for IE and Edge */
  scrollbar-width: none; /* Hide scrollbar for Firefox */
}

.close-button {
    position: absolute;
    top: 10px;
    right: 10px;
    background: none;
    border: none;
    font-size: 20px;
    cursor: pointer;
    color: #333;
}

/* Prevent Background Scroll */
body.modal-active {
    overflow: hidden; /* Disable scrolling */
}

</style>