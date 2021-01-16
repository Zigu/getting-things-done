<template>
  <q-page class="q-pa-md">
    <div class="column items-start">
      <Progress/>
      <q-table
        grid
        hide-header
        hide-pagination
        :data="data"
        :columns="columns"
        row-key="id"
        class="fit"
        selection="multiple"
        :selected.sync="selected"
        :pagination="initialPagination"
        :loading="loading"
      >
        <template v-slot:no-data="{ icon, message, filter }">
          <div class="full-width row flex-center text-green bg-white q-gutter-sm">
            <q-icon size="2em" name="mood" />
            <span>
            You do not have any open tasks. ({{ message }})
          </span>
            <q-icon size="2em" :name="filter ? 'filter_b_and_w' : icon" />
          </div>
        </template>
        <template v-slot:item="props">
          <div
            class="q-pa-md q-mr-xl fit grid-style-transition"
            :style="props.selected ? 'transform: scale(0.95);' : ''"
          >
            <q-card :class="props.selected ? 'bg-grey-2' : ''">
              <q-card-section>
                <div class="text-h6">
                  {{props.row.summary}}
                  <!--q-checkbox dense v-model="props.selected" :label="props.row.summary"/-->
                </div>
                <div class="text-subtitle2">due {{ props.cols[2].value }}</div>
                <div class="text-subtitle2"
                     v-if="props.row.resolution.state !== 'UNSOLVED'">{{ props.cols[3].value}}</div>
              </q-card-section>
              <q-separator/>
              <q-card-section>
                <div v-html="props.cols[0].value" />
                <!-- does not work at the moment. vuex change state outside mutation -->
                <!--q-popup-edit v-model="props.row.notes" buttons>
                  <q-field filled stack-label :loading="loading" :disable="loading">
                    <template v-slot:control>
                      <GtdEditor v-model="props.row.notes" />
                    </template>
                  </q-field>
                </q-popup-edit-->
              </q-card-section>
              <q-separator v-if="resolvedWithComment(props)"/>
              <q-card-section v-if="resolvedWithComment(props)">
                <p><b>Resolution</b></p>
                <div v-html="props.row.resolution.comment" />
              </q-card-section>
              <q-separator/>
              <q-card-section class="text-deep-orange-4">
                {{ props.cols[1].value}}
              </q-card-section>
              <q-card-actions align="right">
                <q-btn size="sm" icon="edit"
                       color="primary"
                       :disable="selected.length != 0"
                       :to="getEditLink(props)"
                       v-if="props.row.resolution.state === 'UNSOLVED'"
                />
                <q-btn size="sm" icon="next_plan"
                       color="primary"
                       :disable="selected.length != 0"
                       :to="getNextLink(props)"
                />
                <q-btn size="sm" icon="undo"
                       color="secondary"
                       :disable="selected.length != 0"
                       @click="unsolve(props.row)"
                       v-if="props.row.resolution.state !== 'UNSOLVED'"
                />
                <q-btn size="sm" icon="done"
                       color="green-7"
                       @click="openResolutionDialog(props.row)"
                       :disable="selected.length != 0"
                       v-if="props.row.resolution.state === 'UNSOLVED'"/>
              </q-card-actions>
            </q-card>
          </div>
        </template>
      </q-table>
    </div>
    <q-page-sticky position="bottom-right" :offset="[18, 18]">
      <q-btn fab icon="add" color="secondary" :to="getNewLink()" />
    </q-page-sticky>
    <q-dialog v-model="resolutionPrompt" persistent full-width>
      <q-card style="min-width: 350px">
        <q-card-section>
          <div class="text-h6">Solve task</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <q-select
            filled
            v-model="modalResolution.state"
            :options="resolutionStateOptions"
            option-label="desc"
            option-value="id"
            label="Resolution State"
            emit-value
            map-options
          />
          <GtdEditor label="Comment" v-model="modalResolution.comment" />
        </q-card-section>

        <q-card-actions align="right" class="text-primary">
          <q-btn flat label="Cancel" @click="resolve(false)" />
          <q-btn flat label="Solve" @click="resolve(true)" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script>
import Progress from 'components/Progress';
import GtdEditor from 'components/GtdEditor';
import { v4 as uuidv4 } from 'uuid';

export default {
  name: 'ListTasks',
  components: { Progress, GtdEditor },
  computed: {
    data() {
      const { resolutionState } = this.$route.params;
      if (resolutionState) {
        const comparisonState = resolutionState.toUpperCase();
        return this.$store.state.task.tasks
          .filter((t) => t.resolution != null && t.resolution.state === comparisonState);
      }
      return this.$store.state.task.tasks;
    },
  },
  mounted() {
    this.$store.dispatch('task/loadAllTasks').then(() => {
      this.loading = false;
    });
  },
  data() {
    return {
      loading: true,
      selected: [],
      resolutionPrompt: false,
      modalResolution: { state: 'SOLVED', comment: '' },
      taskToSolve: null,
      resolutionStateOptions: [
        { id: 'SOLVED', desc: 'Solved' },
        { id: 'DISCARDED', desc: 'Discarded' },
      ],
      initialPagination: {
        sortBy: 'desc',
        descending: false,
        page: 1,
        rowsPerPage: 0,
        // rowsNumber: xx if getting data from a server
      },
      columns: [
        {
          name: 'notes',
          align: 'left',
          label: 'Notes',
          field: 'notes',
          sortable: false,
        },
        {
          name: 'tags',
          align: 'left',
          label: 'Tags',
          field: 'tags',
          format: (val) => (val != null ? `${val.join(',')}` : ''),
          sortable: false,
        },
        {
          name: 'due',
          align: 'right',
          label: 'Due',
          field: 'due',
          // eslint-disable-next-line no-unused-vars
          format: (val) => (val != null ? `${val.type.toLowerCase()} ${val.date.format('DD.MM.YYYY')}` : ''),
          sortable: false,
        },
        {
          name: 'resolution',
          align: 'left',
          label: 'Resolution',
          field: 'resolution',
          // eslint-disable-next-line no-unused-vars
          format: (val) => (val != null ? `${val.state.toLowerCase()} at ${val.date.format('DD.MM.YYYY')}` : ''),
          sortable: false,
        },
      ],
    };
  },
  methods: {
    getEditLink(props) {
      return `/task/${props.row.id}`;
    },
    getNewLink() {
      return `/task/${uuidv4()}`;
    },
    getNextLink(props) {
      return `/task/${props.row.id}/${uuidv4()}`;
    },
    openResolutionDialog(task) {
      this.resolutionPrompt = true;
      this.taskToSolve = task;
    },
    resolve(store) {
      if (store) {
        this.$store.dispatch('task/resolve', { task: this.taskToSolve, resolutionValues: this.modalResolution });
      }
      this.modalResolution = { state: 'SOLVED', comment: '' };
      this.taskToSolve = null;
      this.resolutionPrompt = false;
    },
    resolvedWithComment(props) {
      return props.row.resolution.state !== 'UNSOLVED'
        && !(props.row.resolution.comment === '' || props.row.resolution.comment === null);
    },
    unsolve(task) {
      const unsolvedResolution = { state: 'UNSOLVED', comment: '' };
      this.$store.dispatch('task/resolve', { task, resolutionValues: unsolvedResolution });
    },
  },
};
</script>
