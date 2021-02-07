<template>
  <q-layout view="hHh Lpr lFf">
    <q-header elevated>
      <q-toolbar class="text-green-7 bg-grey-2">
        <q-avatar>
          <q-icon name="assignment_turned_in" />
        </q-avatar>
        <q-toolbar-title>
          <span>Getting Things Done</span>
        </q-toolbar-title>
        <q-space />

        <q-select dense borderless
                  v-model="searchCriterion"
                  :options="searchCriteria"
                  :option-label="option => $t('search_' + option)"
        />
        <q-input dense rounded outlined
                 :disable="searchCriterion === 'all'"
                 v-model="searchText" input-class="text-right" class="q-ml-md">
          <template v-slot:append>
            <q-icon v-if="searchText" name="cancel"
                    @click.stop="searchAll()" class="cursor-pointer" />
            <q-icon name="search" class="cursor-pointer" @click="submitSearch"/>
          </template>
        </q-input>
      </q-toolbar>
    </q-header>

    <q-drawer
      show-if-above
      bordered
      content-class="bg-grey-1"
      :mini="miniState"
    >
      <q-scroll-area class="fit">
        <q-list>
          <EssentialLink
            v-for="link in entityLinks"
            :key="link.title"
            v-bind="link"
          />
          <q-separator />
          <EssentialLink
            v-for="link in appLinks"
            :key="link.title"
            v-bind="link"
          />
        </q-list>
      </q-scroll-area>
      <div class="absolute" style="top: 50%; right: -17px">
        <q-btn
          dense
          flat
          unelevated
          color="grey"
          icon="drag_indicator"
          @click="miniState = !miniState"
        />
      </div>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
    <q-footer elevated class="bg-white">
      <div class="q-pa-md text-right text-grey-6">
          v{{ appVersion }}
      </div>
    </q-footer>
  </q-layout>
</template>

<script>
import EssentialLink from 'components/EssentialLink.vue';

const entityLinks = [
  {
    title: 'Open Tasks Today',
    icon: 'alarm',
    link: '/tasks/today',
  },
  {
    title: 'Open Tasks',
    icon: 'pending_actions',
    link: '/tasks/unsolved',
  },
  {
    title: 'Solved Tasks',
    icon: 'fact_check',
    link: '/tasks/solved',
  },
  {
    title: 'Discarded Tasks',
    icon: 'remove_done',
    link: '/tasks/discarded',
  },
  {
    title: 'Topics',
    icon: 'topic',
    link: '/topics',
  },
];

const appLinks = [
  {
    title: 'Settings',
    icon: 'settings',
    link: '/settings',
  },
];

export default {
  name: 'MainLayout',
  components: { EssentialLink },
  computed: {
    appVersion() {
      return process.env.APP_VERSION;
    },
  },
  data() {
    return {
      leftDrawerOpen: true,
      miniState: true,
      entityLinks,
      appLinks,
      searchText: '',
      searchCriterion: 'all',
      searchCriteria: ['all', 'tag', 'text', 'regex', 'due', 'topic'],
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
    searchAll() {
      this.searchText = '';
      this.searchCriterion = 'all';
      this.submitSearch();
    },
  },
};
</script>
