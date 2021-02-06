const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', redirect: '/tasks/today' },
      { path: 'task/:parentId/:id', component: () => import('pages/EditTask.vue') },
      { path: 'task/:id', component: () => import('pages/EditTask.vue') },
      { path: 'tasks/today', component: () => import('pages/ListTasksToday.vue') },
      { path: 'tasks/:resolutionState', component: () => import('pages/ListTasks.vue') },
      { path: 'tasks', component: () => import('pages/ListTasks.vue') },
      { path: 'projects', component: () => import('pages/ListProjects.vue') },
      { path: 'projects/:id', component: () => import('pages/EditProject.vue') },
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
