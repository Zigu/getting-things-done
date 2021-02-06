import axios from 'axios';
import dayjs from 'dayjs';

export function save(context, task) {
  return axios.post('/tasks', task)
    .then(() => context.dispatch('loadAllTasks'))
    .then(() => context.dispatch('project/loadAllProjects', {}, { root: true }));
}

export function resolve(context, { task, resolutionValues }) {
  const resolution = {
    state: resolutionValues.state,
    comment: resolutionValues.comment,
    date: dayjs(),
  };
  return axios.post(`tasks/${task.id}/resolution`, resolution)
    .then(() => context.commit('resolve', { task, resolution }));
}

export function loadAllTasks(context) {
  return axios.get('/tasks').then((response) => {
    const result = response.data;
    context.commit('replaceState', result);
  });
}

export function search(context, searchParams) {
  const searchCriterion = searchParams.searchCriterion === 'all' ? 'default' : searchParams.searchCriterion;
  return axios.get(`/tasks?searchCriterion=${searchCriterion}&searchExpression=${searchParams.searchExpression}`)
    .then((response) => {
      const result = response.data;
      context.commit('replaceState', result);
    });
}

export function deleteTask(context, task) {
  return axios.delete(`/tasks/${task.id}`).then(() => {
    context.commit('deleteTask', task);
  });
}
