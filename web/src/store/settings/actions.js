import { i18n } from 'src/boot/i18n';

export function setLanguage(context, language) {
  i18n.locale = language;
  context.commit('setLanguage', language);
}
