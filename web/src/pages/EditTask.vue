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
        label="Summary *"
        lazy-rules
        :rules="[ val => val && val.length > 0 || 'Please type something']"
        :loading="loading"
        :disable="loading"
      />

      <q-field filled label="Notes" stack-label :loading="loading" :disable="loading">
        <template v-slot:control>
          <GtdEditor v-model="notes"
                     :loading="loading"
                     :disable="loading"/>
        </template>
      </q-field>

      <q-select
        filled
        v-model="dueType"
        :options="dueTypeOptions"
        :option-label="option => option.toLowerCase()"
        label="Due Type"
        :loading="loading"
        :disable="loading"
      />

      <q-input
        filled
        type="date"
        v-model="dueDate"
        label="Due Date"
        :loading="loading"
        :disable="loading"
      />
      <q-input
        filled
        v-model="tags"
        label="Tags"
        :loading="loading"
        :disable="loading"
      />
      <div>
        <q-btn label="Submit" type="submit" color="primary"/>
        <q-btn label="Reset" type="reset" color="primary" flat class="q-ml-sm"/>
      </div>
    </q-form>
  </q-page>
</template>

<script>
import dayjs from 'dayjs';
import GtdEditor from 'components/GtdEditor';

export default {
  name: 'EditTask',
  components: { GtdEditor },
  mounted() {
    const foundTasks = this.$store.state.task.tasks.filter((t) => t.id === this.id);
    if (foundTasks.length === 1) {
      const foundTask = foundTasks[0];
      this.version = foundTask.version;
      this.summary = foundTask.summary;
      this.notes = foundTask.notes;
      this.tags = foundTask.tags.join(',');
      if (foundTask.due != null) {
        this.dueDate = dayjs(foundTask.due.date)
          .format('YYYY-MM-DD');
        this.dueType = foundTask.due.type;
      }
      this.resolution = foundTask.resolution;
    }
    this.loading = false;
  },
  data() {
    return {
      loading: true,
      parentId: this.$route.params.parentId,
      id: this.$route.params.id,
      version: null,
      summary: '',
      notes: '',
      dueDate: dayjs().format('YYYY-MM-DD'),
      dueType: 'AT',
      tags: null,
      dueTypeOptions: ['AT', 'BEFORE'],
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
      const action = {
        id: this.id,
        version: this.version,
        notes: this.notes,
        summary: this.summary,
        tags: splittedTags,
        parents: parentTasks,
        due: {
          type: this.dueType,
          date: dayjs(this.dueDate, 'MM-DD-YYYY'),
        },
        resolution: {
          state: 'UNSOLVED',
          date: dayjs(),
          comment: '',
        },
      };
      this.$store.dispatch('task/save', action)
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
      this.id = this.$route.params.id;
      this.version = null;
      this.summary = '';
      this.notes = '';
      this.dueDate = dayjs()
        .format('YYYY-MM-DD');
      this.dueType = 'AT';
      this.tags = null;
    },
  },
};
</script>
