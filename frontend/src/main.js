import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { useUserStore } from './store/user';
import App from './App.vue';
import router from './router';

import { plugin as formKitPlugin, defaultConfig } from '@formkit/vue'
import { createMultiStepPlugin } from '@formkit/addons'
import '@formkit/themes/genesis'
import '@formkit/addons/css/multistep'
import { library } from '@fortawesome/fontawesome-svg-core'
import { fas } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import './style.css';

const pinia = createPinia();
const app = createApp(App);

library.add(fas)

app.component('font-awesome-icon', FontAwesomeIcon)
app.use(router);
app.use(pinia);
app.use(formKitPlugin, defaultConfig({
    plugins: [createMultiStepPlugin()]
}))

const userStore = useUserStore();
userStore.initializeUser();

app.mount('#app');
