<template>
  <q-page class="flex top q-pa-md">
    <q-form
      @submit="onSubmit"
      @reset="onReset"
      class="q-gutter-md fit"
    >
      <q-select filled
                dense
                borderless
                emit-value
                map-options
                options-dense
                v-model="language"
                :options="languages"
                :label="$t('Language')" />
      <div>
        <q-btn :label="$t('Submit')" type="submit" color="primary"/>
        <q-btn :label="$t('Reset')" type="reset" color="primary" flat class="q-ml-sm"/>
      </div>
    </q-form>
  </q-page>
</template>

<script>
export default {
  name: 'EditSettings',
  mounted() {
    this.language = this.$store.state.settings.language;
  },
  data() {
    return {
      loading: true,
      language: null,
      languages: [
        { value: 'de-de', label: this.$t('German') },
        { value: 'en-us', label: this.$t('English') },
      ],
    };
  },

  methods: {
    onSubmit() {
      this.$store.dispatch('settings/setLanguage', this.language)
        .then(() => {
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
      this.language = this.$store.state.settings.language;
    },
  },
};
</script>
