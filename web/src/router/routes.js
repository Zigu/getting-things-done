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
      { path: 'projects', meta: { title: 'Projects' }, component: () => import('pages/ListProjects.vue') },
      { path: 'projects/:id', meta: { title: 'project_with_id' }, component: () => import('pages/EditProject.vue') },
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
