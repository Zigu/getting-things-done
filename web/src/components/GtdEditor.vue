<template>
  <q-editor
    min-height="5rem"
    class="fit"
    :value="value"
    @input="$emit('input', $event)"
    :toolbar="[
      ['left', 'center', 'right', 'justify'],
      ['bold', 'italic', 'strike', 'underline', 'subscript', 'superscript'],
      ['token', 'hr', 'link', 'custom_btn'],
      ['print', 'fullscreen'],
      [
        {
          label: $q.lang.editor.formatting,
          icon: $q.iconSet.editor.formatting,
          list: 'no-icons',
          options: [
            'p',
            'h1',
            'h2',
            'h3',
            'h4',
            'h5',
            'h6',
            'code'
          ]
        },
        {
          label: $q.lang.editor.fontSize,
          icon: $q.iconSet.editor.fontSize,
          fixedLabel: true,
          fixedIcon: true,
          list: 'no-icons',
          options: [
            'size-1',
            'size-2',
            'size-3',
            'size-4',
            'size-5',
            'size-6',
            'size-7'
          ]
        },
        'removeFormat'
      ],
      ['quote', 'unordered', 'ordered', 'outdent', 'indent'],
      ['undo', 'redo'],
      ['viewsource']
    ]"
  />
</template>
<script>
export default {
  name: 'GtdEditor',
  props: ['value'],
};
</script>
