import Vue from 'vue';
import axios from 'axios';
import dayjs from 'dayjs';

axios.defaults.baseURL = process.env.BACKEND_URL ? process.env.BACKEND_URL : 'http://localhost:8082';
axios.defaults.headers.post['Content-Type'] = 'application/json';

function requestToJson(data) {
  function dateFormat(date) {
    if (date === null || date === undefined) return '';
    return dayjs(date).format('YYYY-MM-DD');
  }

  // eslint-disable-next-line no-extend-native,func-names
  Date.prototype.toJSON = function () {
    return dateFormat(this);
  };
  // eslint-disable-next-line func-names
  dayjs.prototype.toJSON = function () {
    return dateFormat(this);
  };
  data = JSON.stringify(data);
  return data;
}

axios.defaults.transformRequest = [requestToJson];

Vue.prototype.$axios = axios;

export default ({ store }) => {
  store.dispatch('task/loadAllTasks');
};
