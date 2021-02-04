import axios from 'axios';

export function save(context, project) {
  return axios.post('/projects', project)
    .then(() => context.commit('saveProject', project));
}

export function loadAllProjects(context) {
  return axios.get('/projects').then((response) => {
    const result = response.data;
    context.commit('replaceState', result);
  });
}
