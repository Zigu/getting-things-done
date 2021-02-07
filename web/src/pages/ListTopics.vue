<template>
  <q-page class="q-pa-md">
    <div class="column items-start">
      <q-table
        grid
        hide-header
        hide-pagination
        :data="data"
        :columns="columns"
        row-key="id"
        class="fit"
        :pagination="initialPagination"
        :loading="loading"
      >
        <!--template v-slot:no-data="{ icon, message, filter }">
          <div class="full-width row flex-center text-green bg-white q-gutter-sm">
            <q-icon size="2em" :name="moodIcon(requestedResolutionState)" />
            <span>
            {{ $t(`You do not have any ${requestedResolutionState} tasks.`) }} ({{ $t(message) }})
          </span>
            <q-icon size="2em" :name="filter ? 'filter_b_and_w' : icon" />
          </div>
        </template-->
        <template v-slot:item="props">
          <div class="q-pa-md q-mr-xl fit grid-style-transition">
            <q-card>
              <q-card-section>
                <div class="text-h6">
                  <span class="q-pr-md">{{props.row.name}}</span>
                </div>
              </q-card-section>
              <q-separator/>
              <q-card-actions align="right">
                <q-btn size="sm" icon="edit"
                       color="primary"
                       :to="getEditLink(props)"
                />
                <q-btn size="sm" icon="pending_actions"
                       color="secondary"
                       :title="$t('Open Tasks')"
                       @click.stop="searchTasks(props.row.name)"
                />
              </q-card-actions>
            </q-card>
          </div>
        </template>
      </q-table>
    </div>
    <q-page-sticky position="bottom-right" :offset="[18, 18]">
      <q-btn fab icon="post_add" color="secondary" :to="getNewLink()" />
    </q-page-sticky>
  </q-page>
</template>

<script>
import { v4 as uuidv4 } from 'uuid';

export default {
  name: 'ListTopics',
  computed: {
    data() {
      return this.$store.state.topic.topics;
    },
  },
  mounted() {
    this.$store.dispatch('topic/loadAllTopics').then(() => {
      this.loading = false;
    });
  },
  data() {
    return {
      loading: true,
      initialPagination: {
        sortBy: 'desc',
        descending: false,
        page: 1,
        rowsPerPage: 0,
        // rowsNumber: xx if getting data from a server
      },
      columns: [],
    };
  },
  methods: {
    getEditLink(props) {
      return `/topics/${props.row.id}`;
    },
    getNewLink() {
      return `/topics/${uuidv4()}`;
    },
    searchTasks(topicName) {
      this.$root.$emit('search', { searchText: topicName, searchCriterion: 'topic' });
      this.$router.push('/tasks/unsolved');
    },
  },
};
</script>
