<template>
  <q-page class="flex top q-pa-md">

    <q-form
      @submit="onSubmit"
      @reset="onReset"
      class="q-gutter-md fit"
    >
      <q-input
        filled
        disable
        v-model="id"
        label="Id *"
      />
      <q-input
        filled
        v-model="summary"
        :label="$t('Summary *')"
        lazy-rules
        :rules="[ val => val && val.length > 0 || $t('Please type something')]"
        :loading="loading"
        :disable="loading"
      />

      <q-field :label="$t('Notes')" :loading="loading" :disable="loading" borderless>
        <template v-slot:control>
          <div class="q-pa-md full-width">
          <GtdEditor v-model="notes"
                     :loading="loading"
                     :disable="loading"/>
          </div>
        </template>
      </q-field>

      <q-field :label="$t('Due')" :loading="loading" :disable="loading" borderless>
        <div class="q-pa-md full-width">
          <div class="row q-gutter-md">
            <div class="col">
              <q-select
                filled
                v-model="dueType"
                :options="dueTypeOptions"
                :option-label="option => $t(option.toLowerCase())"
              />
            </div>
            <div class="col">
              <q-input
              filled
              type="date"
              v-model="dueDate"
              />
            </div>
          </div>
        </div>
      </q-field>
      <q-input
        filled
        v-model="tags"
        :label="$t('Tags')"
        :loading="loading"
        :disable="loading"
      />
      <div>
        <q-btn :label="$t('Submit')" type="submit" color="primary"/>
        <q-btn :label="$t('Reset')" type="reset" color="primary" flat class="q-ml-sm"/>
      </div>
    </q-form>
  </q-page>
</template>

<script>
import GtdEditor from 'components/GtdEditor';
import dayjs from 'dayjs';

export default {
  name: 'EditTask',
  components: { GtdEditor },
  mounted() {
    const foundTasks = this.$store.state.task.tasks.filter((t) => t.id === this.id);
    if (foundTasks.length === 1) {
      // slice does not work properly
      // eslint-disable-next-line prefer-destructuring
      this.foundTask = foundTasks[0];
      this.mapFoundTask();
    }
    this.loading = false;
  },
  data() {
    return {
      loading: true,
      foundTask: null,
      parentId: this.$route.params.parentId,
      id: this.$route.params.id,
      version: null,
      summary: '',
      notes: '',
      dueDate: dayjs().format('YYYY-MM-DD'),
      dueType: 'AT',
      tags: null,
      dueTypeOptions: ['AT', 'BEFORE'],
      resolution: {
        state: 'UNSOLVED',
        date: dayjs(),
        comment: '',
      },
    };
  },

  methods: {
    onSubmit() {
      const splittedTags = this.tags != null ? this.tags.split(',')
        .map((item) => item.trim()) : [];
      let parentTasks = [];
      if (this.parentId != null) {
        const foundParentTasks = this.$store.state.task.tasks.filter((t) => t.id === this.parentId);
        if (foundParentTasks.length === 1) {
          // eslint-disable-next-line prefer-destructuring
          parentTasks = foundParentTasks;
        }
      }
      const task = {
        id: this.id,
        version: this.version,
        notes: this.notes,
        summary: this.summary,
        tags: splittedTags,
        parents: parentTasks,
        due: {
          type: this.dueType,
          date: dayjs(this.dueDate, 'YYYY-MM-DD'),
        },
        resolution: this.resolution,
      };
      this.$store.dispatch('task/save', task)
        .then(() => {
          this.$router.push('/tasks/unsolved');
          this.$q.notify({
            color: 'green-4',
            textColor: 'white',
            icon: 'cloud_done',
            message: 'Submitted',
          });
        })
        .catch((error) => {
          const errorMessage = `Error occurred. ${error}`;
          this.$q.notify({
            color: 'red-4',
            textColor: 'white',
            icon: 'cloud_done',
            message: errorMessage,
          });
        });
    },

    onReset() {
      this.mapFoundTask();
    },
    mapFoundTask() {
      if (this.foundTask) {
        this.version = this.foundTask.version;
        this.summary = this.foundTask.summary;
        this.notes = this.foundTask.notes;
        this.tags = this.foundTask.tags.join(',');
        if (this.foundTask.due != null) {
          this.dueDate = dayjs(this.foundTask.due.date).format('YYYY-MM-DD');
          this.dueType = this.foundTask.due.type;
        }
        this.resolution = this.foundTask.resolution;
      } else {
        this.id = this.$route.params.id;
        this.version = null;
        this.summary = '';
        this.notes = '';
        this.dueDate = dayjs(Date.now()).format('YYYY-MM-DD');
        this.dueType = 'AT';
        this.tags = null;
        this.resolution = {
          state: 'UNSOLVED',
          date: dayjs(),
          comment: '',
        };
      }
    },
  },
};
</script>
