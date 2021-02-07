import { mapTask } from 'src/boot/utils';

export function saveTask(state, task) {
  const foundTasks = state.tasks.filter((t) => t.id === task.id);
  if (foundTasks.length === 1) {
    const index = state.tasks.indexOf(foundTasks[0]);
    state.tasks[index] = task;
  } else {
    state.tasks.push(task);
  }
}

export function resolve(state, { task, resolution }) {
  task.resolution = resolution;
  saveTask(state, task);
  return task;
}

export function replaceState(state, tasks) {
  const newState = tasks.map((task) => mapTask(task));
  state.tasks.splice(0, state.tasks.length, ...newState);
}

export function searchApplied(state, value) {
  state.searchApplied = value;
}

export function deleteTask(state, task) {
  const index = state.tasks.indexOf(task);
  if (index > -1) {
    state.tasks.splice(index, 1);
  }
}
