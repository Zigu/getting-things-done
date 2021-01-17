<template>
  <q-linear-progress size="25px" :value="progressValue" :color="progressColor">
    <div class="absolute-full flex flex-center">
      <q-badge color="white" text-color="info" :label="progressLabel"/>
    </div>
  </q-linear-progress>
</template>

<script>
export default {
  name: 'Progress',
  computed: {
    progressValue() {
      const total = this.$store.state.task.tasks.length;
      const solved = this.$store.state.task.tasks.filter((a) => a.resolution != null && a.resolution.state !== 'UNSOLVED').length;
      return total === 0 ? 0 : solved / total;
    },
    progressLabel() {
      return `${(this.progressValue * 100).toFixed(2)}%`;
    },
    progressColor() {
      return this.progressValue === 1 ? 'positive' : 'warning';
    },
  },
};
</script>
