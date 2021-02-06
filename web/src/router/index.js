import Vue from 'vue';
import VueRouter from 'vue-router';
import { i18n } from 'src/boot/i18n';

import routes from './routes';

Vue.use(VueRouter);

/*
 * If not building with SSR mode, you can
 * directly export the Router instantiation;
 *
 * The function below can be async too; either use
 * async/await or return a Promise which resolves
 * with the Router instance.
 */

export default function (/* { store, ssrContext } */) {
  const Router = new VueRouter({
    scrollBehavior: () => ({ x: 0, y: 0 }),
    routes,

    // Leave these as they are and change in quasar.conf.js instead!
    // quasar.conf.js -> build -> vueRouterMode
    // quasar.conf.js -> build -> publicPath
    mode: process.env.VUE_ROUTER_MODE,
    base: process.env.VUE_ROUTER_BASE,
  });

  Router.afterEach((to) => {
    // Use next tick to handle router history correctly
    // see: https://github.com/vuejs/vue-router/issues/914#issuecomment-384477609
    Vue.nextTick(() => {
      let documentTitle = to.meta.title;
      if (documentTitle.indexOf('$') > -1) {
        const paramKeys = Object.keys(to.params);
        paramKeys.forEach((key) => {
          documentTitle = documentTitle.replace(`$${key}`, to.params[key]);
        });
      }
      document.title = i18n.t(documentTitle, to.params) || process.env.APP_NAME;
    });
  });

  return Router;
}
