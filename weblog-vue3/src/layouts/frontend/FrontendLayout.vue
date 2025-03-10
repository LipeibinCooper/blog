<template>
  <div class="flex flex-col min-h-screen">
    <Header></Header>

    <main class="flex-grow">
      <slot></slot>
    </main>

    <Footer></Footer>
  </div>
</template>

<script setup>
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'
</script>
