<template>
  <div>
    <h1>Page Not Found</h1>
  </div>
</template>

<script>
export default {
  name: 'NotFound',
  created() {
    if (this.$store.state.menuItems.length == 0) {
      window.location.href = "/404/"
    }
  }
}
</script>