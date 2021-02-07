import Vue from 'vue';
import dayjs from 'dayjs';

export function mapTask(task) {
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
  let mappedPreviousTasks = [];
  if (task.previousTasks != null) {
    mappedPreviousTasks = task.previousTasks.map((pt) => mapTask(pt));
  }
  return {
    id: task.id,
    version: task.version,
    summary: task.summary,
    notes: task.notes,
    tags: task.tags,
    topic: task.topic,
    due: mappedDue,
    resolution: mappedResolution,
    previousTasks: mappedPreviousTasks,
  };
}

Vue.prototype.$mapTask = mapTask;
