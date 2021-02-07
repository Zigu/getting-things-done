const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', redirect: '/tasks/today' },
      { path: 'task/:parentId/:id', meta: { title: 'task_with_id' }, component: () => import('pages/EditTask.vue') },
      { path: 'task/:id', meta: { title: 'task_with_id' }, component: () => import('pages/EditTask.vue') },
      { path: 'tasks/today', meta: { title: 'Open Tasks Today' }, component: () => import('pages/ListTasksToday.vue') },
      { path: 'tasks/:resolutionState', meta: { title: '$resolutionState Tasks' }, component: () => import('pages/ListTasks.vue') },
      { path: 'tasks', meta: { title: 'Tasks' }, component: () => import('pages/ListTasks.vue') },
      { path: 'topics', meta: { title: 'Topics' }, component: () => import('pages/ListTopics.vue') },
      { path: 'topics/:id', meta: { title: 'topic_with_id' }, component: () => import('pages/EditTopic.vue') },
      { path: 'settings', meta: { title: 'Settings' }, component: () => import('pages/EditSettings.vue') },
    ],
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '*',
    component: () => import('pages/Error404.vue'),
  },
];

export default routes;
