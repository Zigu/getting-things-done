import Vue from 'vue';
import VueI18n from 'vue-i18n';
import messages from 'src/i18n';
import Quasar from 'quasar';

Vue.use(VueI18n);

const i18n = new VueI18n({
  locale: 'de-de',
  fallbackLocale: 'en-us',
  messages,
});

export default ({ app, store }) => {
  // Set i18n instance on app
  app.i18n = i18n;
  store.dispatch('settings/setLanguage', Quasar.lang.getLocale());
};

export { i18n };
