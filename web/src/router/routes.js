const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/Index.vue') },
      { path: 'task/:parentId/:id', component: () => import('pages/EditTask.vue') },
      { path: 'task/:id', component: () => import('pages/EditTask.vue') },
      { path: 'tasks/:resolutionState', component: () => import('pages/ListTasks.vue') },
      { path: 'tasks', component: () => import('pages/ListTasks.vue') },
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
