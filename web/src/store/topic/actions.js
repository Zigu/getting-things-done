import axios from 'axios';

export function save(context, topic) {
  return axios.post('/topics', topic)
    .then(() => context.commit('saveTopic', topic));
}

export function loadAllTopics(context) {
  return axios.get('/topics').then((response) => {
    const result = response.data;
    context.commit('replaceState', result);
  });
}
