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
        v-model="name"
        :label="$t('Name *')"
        lazy-rules
        :rules="[ val => val && val.length > 0 || $t('Please type something')]"
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
export default {
  name: 'EditProject',
  mounted() {
    const foundProjects = this.$store.state.project.projects.filter((t) => t.id === this.id);
    if (foundProjects.length === 1) {
      // slice does not work properly
      // eslint-disable-next-line prefer-destructuring
      this.foundProject = foundProjects[0];
      this.mapFoundProject();
    }
    this.loading = false;
  },
  data() {
    return {
      loading: true,
      foundProject: null,
      id: this.$route.params.id,
      version: null,
      name: '',
    };
  },

  methods: {
    onSubmit() {
      const project = {
        id: this.id,
        version: this.version,
        name: this.name,
      };
      this.$store.dispatch('project/save', project)
        .then(() => {
          this.$router.push('/projects');
          const translatedMessage = this.$t('Submitted');
          this.$q.notify({
            color: 'green-4',
            textColor: 'white',
            icon: 'cloud_done',
            message: translatedMessage,
          });
        })
        .catch((error) => {
          const errorMessage = `${this.$t('Error occurred.')} ${error}`;
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
    mapFoundProject() {
      if (this.foundProject) {
        this.version = this.foundProject.version;
        this.name = this.foundProject.name;
      } else {
        this.id = this.$route.params.id;
        this.version = null;
        this.name = '';
      }
    },
  },
};
</script>
