<template>
  <div>
    <AppHeader />
    <div class="container" style="padding-top:2rem;padding-bottom:2rem"><router-view /></div>
  </div>
</template>

<script setup lang="ts">
import AppHeader from './AppHeader.vue'
</script>
