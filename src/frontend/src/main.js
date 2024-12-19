import { createApp } from 'vue';
import { createPinia } from 'pinia'; 
import App from './App.vue';
import router from './router';
import { useUserStore } from './store/user';

const pinia = createPinia();
const app = createApp(App);

app.use(router);
app.use(pinia);

const userStore = useUserStore();
userStore.initializeUser(); 
app.mount('#app');
