import axios from 'axios';
import dayjs from 'dayjs';

export function save(context, task) {
  return axios.post('/tasks', task)
    .then(() => context.commit('saveTask', task));
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
  return axios.get(`/tasks?searchCriterion=${searchParams.searchCriterion}&searchExpression=${searchParams.searchExpression}`)
    .then((response) => {
      const result = response.data;
      context.commit('replaceState', result);
    });
}
