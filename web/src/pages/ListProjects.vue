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
                <q-btn size="sm" icon="list"
                       color="secondary"
                />
              </q-card-actions>
            </q-card>
          </div>
        </template>
      </q-table>
    </div>
    <q-page-sticky position="bottom-right" :offset="[18, 18]">
      <q-btn fab icon="add" color="secondary" :to="getNewLink()" />
    </q-page-sticky>
  </q-page>
</template>

<script>
import { v4 as uuidv4 } from 'uuid';

export default {
  name: 'ListTasks',
  computed: {
    data() {
      return this.$store.state.project.projects;
    },
  },
  mounted() {
    this.$store.dispatch('project/loadAllProjects').then(() => {
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
      return `/projects/${props.row.id}`;
    },
    getNewLink() {
      return `/projects/${uuidv4()}`;
    },
  },
};
</script>
