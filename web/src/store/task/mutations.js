import dayjs from 'dayjs';

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
  const newState = tasks.map((task) => {
    let mappedDue = null;
    if (task.due != null) {
      mappedDue = {
        date: dayjs(task.due.date, 'YYYY-MM-DD'),
        type: task.due.type,
      };
    }
    let mappedResolution = null;
    if (task.resolution != null) {
      mappedResolution = {
        date: dayjs(task.resolution.date, 'YYYY-MM-DD'),
        state: task.resolution.state,
        comment: task.resolution.comment,
      };
    }
    return {
      id: task.id,
      version: task.version,
      summary: task.summary,
      notes: task.notes,
      tags: task.tags,
      project: task.project,
      due: mappedDue,
      resolution: mappedResolution,
    };
  });
  state.tasks.splice(0, state.tasks.length, ...newState);
}
