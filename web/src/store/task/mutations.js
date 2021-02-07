import { mapTask } from 'src/boot/utils';

export function replaceState(state, tasks) {
  const newState = tasks.map((task) => mapTask(task));
  state.tasks.splice(0, state.tasks.length, ...newState);
}

export function updateSearchState(state, newState) {
  state.search = newState;
}

export function resetSearchState(state) {
  state.search = {
    applied: false,
    criterion: 'all',
    expression: '',
  };
}
