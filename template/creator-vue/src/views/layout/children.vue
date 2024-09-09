<template>
  <div class="app-children">
    <!-- 二级菜单以及以下菜单调用次路由模板 -->
    <transition name="fade-transform" mode="out-in">
      <keep-alive :include="keepAliveValues"><router-view /></keep-alive>
    </transition>
  </div>
</template>

<script>
export default {
  computed: {
    keepAliveValues() {
      return this.$store.state.keepAliveValues
    }
  }
}
</script>
