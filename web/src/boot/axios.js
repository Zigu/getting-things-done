import Vue from 'vue';
import axios from 'axios';

axios.defaults.baseURL = process.env.BACKEND_URL ? process.env.BACKEND_URL : 'http://localhost:8080';
axios.defaults.headers.post['Content-Type'] = 'application/json';

Vue.prototype.$axios = axios;

export default ({ store }) => {
  store.dispatch('task/loadAllTasks');
};
