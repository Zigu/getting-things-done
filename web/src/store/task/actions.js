import { v4 as uuidv4 } from 'uuid';
import dayjs from 'dayjs';
import axios from 'axios';

export function save(context, task) {
  if (task.due != null) {
    task.due.id = uuidv4();
  }
  if (task.resolution != null) {
    task.resolution.id = uuidv4();
  }
  return axios.post('/tasks', task)
    .then(() => context.commit('saveTask', task));
}

export function resolve(context, { task, resolutionValues }) {
  const resolution = {
    id: task.resolution.id,
    version: task.resolution.version,
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
    console.log(result);
    context.commit('replaceState', result);
  });
}
