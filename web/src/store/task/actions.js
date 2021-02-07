import axios from 'axios';
import dayjs from 'dayjs';

export function save(context, task) {
  return axios.post('/tasks', task)
    .then(() => context.dispatch('search', { searchCriterion: context.state.search.criterion, searchExpression: context.state.search.expression }))
    .then(() => context.dispatch('topic/loadAllPTopics', {}, { root: true }));
}

export function resolve(context, { task, resolutionValues }) {
  const resolution = {
    state: resolutionValues.state,
    comment: resolutionValues.comment,
    date: dayjs(),
  };
  return axios.post(`tasks/${task.id}/resolution`, resolution)
    .then(() => context.dispatch('search', { searchCriterion: context.state.search.criterion, searchExpression: context.state.search.expression }))
    .then(() => context.dispatch('topic/loadAllTopic', {}, { root: true }));
}

export function loadAllTasks(context) {
  return axios.get('/tasks').then((response) => {
    const result = response.data;
    context.commit('replaceState', result);
    context.commit('resetSearchState');
  });
}

export function search(context, searchParams) {
  const searchCriterion = searchParams.searchCriterion === 'all' ? 'default' : searchParams.searchCriterion;
  const newSearchState = {
    applied: (searchCriterion !== 'default'),
    criterion: searchParams.searchCriterion,
    expression: searchParams.searchExpression,
  };
  context.commit('updateSearchState', newSearchState);
  return axios.get(`/tasks?searchCriterion=${searchCriterion}&searchExpression=${searchParams.searchExpression}`)
    .then((response) => {
      const result = response.data;
      context.commit('replaceState', result);
    });
}

export function deleteTask(context, task) {
  return axios.delete(`/tasks/${task.id}`)
    .then(() => context.dispatch('search', { searchCriterion: context.state.search.criterion, searchExpression: context.state.search.expression }))
    .then(() => context.dispatch('topic/loadAllTopics', {}, { root: true }));
}
