<template>
  <q-page class="q-pa-md">
    <div class="column items-start">
      <Progress/>
      <q-calendar
        v-if="view=='calendar'"
        view="month"
        locale="de-de"
        bordered
      >
        <template #day="{ timestamp }">
          <template v-for="(task, index) in getTasks(timestamp.date)">
            <q-badge
              class="bg-blue-2 text-black full-width"
              :key="index"
            >
              <q-icon v-if="isOverdue(task)" name="alarm" color="red" class="q-mr-xs" />
              <span>{{ task.summary }}</span>
            </q-badge>
          </template>
        </template>
      </q-calendar>
      <q-table
        v-if="view == 'table'"
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
            <q-icon size="2em" :name="moodIcon(requestedResolutionState)" />
            <span>
            {{ $t(`You do not have any ${requestedResolutionState} tasks.`) }} ({{ $t(message) }})
          </span>
            <q-icon size="2em" :name="filter ? 'filter_b_and_w' : icon" />
          </div>
        </template>
        <template v-slot:item="props">
          <div class="q-pa-md q-mr-xl fit grid-style-transition">
            <q-card>
              <q-card-section>
                <div class="text-h6">
                  <span class="q-pr-md">{{props.row.summary}}</span>
                  <q-icon name="alarm" color="red" v-if="isOverdue(props.row)" size="md"/>
                  <!--q-checkbox dense v-model="props.selected" :label="props.row.summary"/-->
                </div>
                <div class="text-subtitle2">{{$t('due')}} {{ props.cols[3].value }}</div>
                <div class="text-subtitle2"
                     v-if="props.row.resolution.state !== 'UNSOLVED'">{{ props.cols[4].value}}</div>
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
                <p><b>{{$t('Resolution')}}</b></p>
                <div v-html="props.row.resolution.comment" />
              </q-card-section>
              <q-separator/>
              <q-card-section class="text-deep-orange-4">
                <span v-if="props.cols[1].value !== ''">{{ props.cols[1].value}}</span>
                <span v-if="props.cols[1].value !== '' && props.cols[2].value !== ''">: </span>
                {{ props.cols[2].value}}
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
      <q-btn fab icon="add_task" color="secondary" :to="getNewLink()" />
    </q-page-sticky>
    <q-dialog v-model="resolutionPrompt" persistent full-width>
      <q-card style="min-width: 350px">
        <q-card-section>
          <div class="text-h6">{{$t('Solve task')}}</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <q-select
            filled
            v-model="modalResolution.state"
            :options="resolutionStateOptions"
            option-label="desc"
            option-value="id"
            :label="$t('Resolution State')"
            emit-value
            map-options
          />
          <GtdEditor :label="$t('Comment')" v-model="modalResolution.comment" />
        </q-card-section>

        <q-card-actions align="right" class="text-primary">
          <q-btn flat :label="$t('Cancel')" @click="resolve(false)" />
          <q-btn flat :label="$t('Solve')" @click="resolve(true)" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script>
import Progress from 'components/Progress';
import GtdEditor from 'components/GtdEditor';
import { v4 as uuidv4 } from 'uuid';
import dayjs from 'dayjs';
import QCalendar from '@quasar/quasar-ui-qcalendar';

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
    requestedResolutionState() {
      return this.$route.params.resolutionState;
    },
  },
  mounted() {
    this.$store.dispatch('task/loadAllTasks').then(() => {
      this.loading = false;
    });
  },
  data() {
    return {
      view: 'table',
      loading: true,
      selected: [],
      resolutionPrompt: false,
      modalResolution: { state: 'SOLVED', comment: '' },
      taskToSolve: null,
      resolutionStateOptions: [
        { id: 'SOLVED', desc: this.$t('Solved') },
        { id: 'DISCARDED', desc: this.$t('Discarded') },
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
          label: this.$t('Notes'),
          field: 'notes',
          sortable: false,
        },
        {
          name: 'project',
          align: 'left',
          label: this.$t('Project'),
          field: 'project',
          format: (val) => (val != null ? `${val.name}` : ''),
          sortable: false,
        },
        {
          name: 'tags',
          align: 'left',
          label: this.$t('Tags'),
          field: 'tags',
          format: (val) => (val != null ? `${val.join(',')}` : ''),
          sortable: false,
        },
        {
          name: 'due',
          align: 'right',
          label: this.$t('Due'),
          field: 'due',
          // eslint-disable-next-line no-unused-vars
          format: (val) => (val != null ? `${this.$t(val.type.toLowerCase())} ${val.date.format('DD.MM.YYYY')}` : ''),
          sortable: false,
        },
        {
          name: 'resolution',
          align: 'left',
          label: this.$t('Resolution'),
          field: 'resolution',
          // eslint-disable-next-line no-unused-vars
          format: (val) => (val != null ? `${this.$t(val.state.toLowerCase())} ${this.$t('at')} ${val.date.format('DD.MM.YYYY')}` : ''),
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
    isOverdue(task) {
      return task.due.date.isBefore(dayjs()) && task.resolution.state === 'UNSOLVED';
    },
    moodIcon(resolutionState) {
      if (resolutionState === 'solved') {
        return 'sentiment_dissatisfied';
      }

      if (resolutionState === 'discarded') {
        return 'sentiment_satisfied';
      }
      return 'sentiment_very_satisfied';
    },
    getTasks(timestamp) {
      const currentDate = QCalendar.parseTimestamp(timestamp);
      return this.data.filter((task) => task.due.date.isSame(currentDate.date, 'day'));
    },
  },
};
</script>
