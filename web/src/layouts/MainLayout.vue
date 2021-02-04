<template>
  <q-layout view="hHh Lpr lFf">
    <q-header elevated>
      <q-toolbar class="text-green-7 bg-grey-2">
        <q-btn
          flat
          dense
          round
          icon="menu"
          aria-label="Menu"
          @click="leftDrawerOpen = !leftDrawerOpen"
        />

        <q-toolbar-title>
          Getting Things Done
        </q-toolbar-title>
        <q-space />

        <q-select dense borderless
                  v-model="searchCriterion"
                  :options="searchCriteria"
                  :option-label="option => $t('search_' + option)"
        />
        <q-input dense rounded outlined
                 v-model="searchText" input-class="text-right" class="q-ml-md">
          <template v-slot:append>
            <q-icon name="search" class="cursor-pointer" @click="submitSearch"/>
          </template>
        </q-input>
      </q-toolbar>
    </q-header>

    <q-drawer
      v-model="leftDrawerOpen"
      show-if-above
      bordered
      content-class="bg-grey-1"
    >
      <q-list>
        <EssentialLink
          v-for="link in essentialLinks"
          :key="link.title"
          v-bind="link"
        />
      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script>
import EssentialLink from 'components/EssentialLink.vue';

const linksData = [
  {
    title: 'Open Tasks',
    icon: 'list',
    link: '/tasks/unsolved',
  },
  {
    title: 'Solved Tasks',
    icon: 'list',
    link: '/tasks/solved',
  },
  {
    title: 'Discarded Tasks',
    icon: 'list',
    link: '/tasks/discarded',
  },
  {
    title: 'Projects',
    icon: 'list',
    link: '/projects',
  },
/*  {
    title: 'Docs',
    caption: 'quasar.dev',
    icon: 'school',
    link: 'https://quasar.dev',
  },
  {
    title: 'Github',
    caption: 'github.com/quasarframework',
    icon: 'code',
    link: 'https://github.com/quasarframework',
  },
  {
    title: 'Discord Chat Channel',
    caption: 'chat.quasar.dev',
    icon: 'chat',
    link: 'https://chat.quasar.dev',
  },
  {
    title: 'Forum',
    caption: 'forum.quasar.dev',
    icon: 'record_voice_over',
    link: 'https://forum.quasar.dev',
  },
  {
    title: 'Twitter',
    caption: '@quasarframework',
    icon: 'rss_feed',
    link: 'https://twitter.quasar.dev',
  },
  {
    title: 'Facebook',
    caption: '@QuasarFramework',
    icon: 'public',
    link: 'https://facebook.quasar.dev',
  },
  {
    title: 'Quasar Awesome',
    caption: 'Community Quasar projects',
    icon: 'favorite',
    link: 'https://awesome.quasar.dev',
  }, */
];

export default {
  name: 'MainLayout',
  components: { EssentialLink },
  data() {
    return {
      leftDrawerOpen: false,
      essentialLinks: linksData,
      searchText: '',
      searchCriterion: 'all',
      searchCriteria: ['all', 'tag', 'text', 'regex', 'due'],
    };
  },
  methods: {
    submitSearch() {
      const params = {
        searchExpression: this.searchText,
        searchCriterion: this.searchCriterion,
      };
      this.$store.dispatch('task/search', params);
    },
  },
};
</script>
